package controller

import (
	"net/http"
	"reflect"
	"slices"
	"studygroup_api/models"
	"studygroup_api/response"
	"studygroup_api/services"
	"studygroup_api/structs"
	"studygroup_api/utils"
	"time"
)

func CreatePost(r *http.Request, w http.ResponseWriter, token *structs.Token, postStruct *structs.Post) {

	subject, subjectErr := services.GetSubjectWithID(postStruct.SubjectID)
	if subjectErr != nil {
		response.Error(http.StatusNotFound, "The referenced subject was not found", w)
		return
	}

	user, userErr := services.FindUserByID(token.UserID)
	if userErr != nil {
		response.Error(http.StatusInternalServerError, userErr.Error(), w)
		return
	}

	post := utils.CreatePostModel(postStruct, &subject, token.UserID, user.UniversityID.ID)

	postID, insertErr := services.InsertNewPost(post)
	if insertErr != nil {
		response.Error(http.StatusInternalServerError, insertErr.Error(), w)
		return
	}

	updateUserErr := services.UpdateUsersPosts(token.UserID, postID)
	if updateUserErr != nil {
		response.Error(http.StatusInternalServerError, "Unable to update list of users posts", w)
		return
	}

	response.Object(http.StatusOK, post, w)
}

func AnswerPost(r *http.Request, w http.ResponseWriter, token *structs.Token, postID, answer string) {
	user, userErr := services.FindUserByID(token.UserID)
	if userErr != nil {
		response.Error(http.StatusInternalServerError, userErr.Error(), w)
		return
	}

	if slices.Contains(user.Posts, postID) {
		response.Error(http.StatusConflict, "You have already answered this post.", w)
		return
	}

	for _, r := range user.Responses {
		if r.PostID == postID {
			response.Error(http.StatusConflict, "You have already answered this post.", w)
			return
		}
	}

	post, postErr := services.GetPostByID(postID)
	if postErr != nil {
		response.Error(http.StatusInternalServerError, postErr.Error(), w)
		return
	}

	if post.ExpirationDate.Before(time.Now().Local()) {
		response.Error(http.StatusConflict, "This post has expired", w)
		return
	}

	if post.UniversityID != user.UniversityID.ID {
		response.Error(http.StatusNotFound, "Post not found within your affiliated university", w)
		return
	}

	if insertErr := services.InsertPostReply(postID, token.UserID, answer); insertErr != nil {
		response.Error(http.StatusInternalServerError, "Internal server error", w)
		return
	}

	updatedPost, updatedPostErr := services.GetPostByID(postID)
	if updatedPostErr != nil {
		response.Error(http.StatusInternalServerError, "Internal server error", w)
		return
	}

	if answer == "skip" {
		response.Message(http.StatusOK, "Answer submitted!", w)
		return
	}

	//check if any of the users have set that they can assist
	if hasAssistingUsers := services.CheckIfAnyAssistingUsers(updatedPost.Responses); !hasAssistingUsers {
		response.Message(http.StatusOK, "Answer submitted!", w)
		return
	}

	group, groupErr := services.GetGroupByPostID(postID)
	if groupErr != nil {
		response.Error(http.StatusInternalServerError, "Internal server error finding groupd", w)
		return
	}

	dittoUsers, assistUsers, participants := utils.CreateMembersNewGroup(updatedPost.Responses)

	//create group if not already existing
	if reflect.DeepEqual(group, models.Group{}) {
		createGroupErr := services.CreateGroup(updatedPost, dittoUsers, assistUsers, participants)
		if createGroupErr != nil {
			response.Error(http.StatusInternalServerError, createGroupErr.Error(), w)
			return
		}

		response.Message(http.StatusOK, "Post has been answered, and group created!", w)
		return
	}

	if updateGroupErr := services.AddNewGroupMember(group.GroupID, token.UserID, answer); updateGroupErr != nil {
		response.Error(http.StatusInternalServerError, "Internal server error", w)
		return
	}

	response.Message(http.StatusOK, "Answer submitted!", w)

}

func UpdateAnswer(r *http.Request, w http.ResponseWriter, token *structs.Token, postID, answer string) {
	post, postErr := services.GetPostByID(postID)
	if postErr != nil {
		response.Error(http.StatusInternalServerError, postErr.Error(), w)
		return
	}

	if post.ExpirationDate.Before(time.Now().Local()) {
		response.Error(http.StatusConflict, "This post has expired", w)
		return
	}

	var userHasAnswered bool
	userHasAnswered = false
	for _, p := range post.Responses {
		if p.UserID == token.UserID {
			userHasAnswered = true
			break
		}
	}

	if !userHasAnswered {
		response.Error(http.StatusConflict, "You cannot update a reply to a post you have not given an initial answer", w)
		return
	}

	user, userErr := services.FindUserByID(token.UserID)
	if userErr != nil {
		response.Error(http.StatusInternalServerError, userErr.Error(), w)
		return
	}

	if updatePostReplyErr := services.UpdatePostReply(token.UserID, answer, post, user); updatePostReplyErr != nil {
		response.Error(http.StatusInternalServerError, "Internal server error updating the reply", w)
		return
	}

	updatedPost, updatedPostErr := services.GetPostByID(postID)
	if updatedPostErr != nil {
		response.Error(http.StatusInternalServerError, "Internal server error 1", w)
		return
	}

	//response.Object(http.StatusOK, updatedPost, w)

	group, groupErr := services.GetGroupByPostID(postID)
	if groupErr != nil {
		response.Error(http.StatusInternalServerError, "Internal server error finding groupd", w)
		return
	}

	//check if any of the users have set that they can assist
	hasAssistingUsers := services.CheckIfAnyAssistingUsers(updatedPost.Responses)
	dittoUsers, assistUsers, participants := utils.CreateMembersNewGroup(updatedPost.Responses)

	//create group if not already existing
	if reflect.DeepEqual(group, models.Group{}) && hasAssistingUsers {
		createGroupErr := services.CreateGroup(updatedPost, dittoUsers, assistUsers, participants)
		if createGroupErr != nil {
			response.Error(http.StatusInternalServerError, createGroupErr.Error(), w)
			return
		}

		response.Message(http.StatusOK, "Post has been answered, and group created!", w)
		return
	}

	if updateGroupErr := services.UpdateGroupMembers(group.GroupID, dittoUsers, assistUsers, participants); updateGroupErr != nil {
		response.Error(http.StatusInternalServerError, updateGroupErr.Error(), w)
		return
	}

	response.Message(http.StatusOK, "Answer updated!", w)
}

// GetAllPosts returns all posts the authenticated user has not yet responded to.
// It retrieves all posts from Firestore, filters out those containing a response
// from the requesting user, and returns the remaining posts.
//
// @Params r: the incoming HTTP request
// @Params w: the HTTP response writer used to return data to the client
// @Params token: the authenticated user's token, containing the user's ID
func GetAllPosts(r *http.Request, w http.ResponseWriter, token *structs.Token) {
	token.UserID = token.UserID
	allPosts, err := services.GetAllPosts()
	if err != nil {
		response.Error(http.StatusInternalServerError, err.Error(), w)
		return
	}

	var posts []models.Post
	for _, p := range allPosts {
		var shouldAppend bool
		shouldAppend = true
		for _, res := range p.Responses {
			if res.UserID == token.UserID {
				shouldAppend = false
			}
		}

		if shouldAppend {
			posts = append(posts, p)
		}
	}

	var returnPosts []structs.ReturnPost
	for _, post := range posts {
		returnPosts = append(returnPosts, structs.ReturnPost{
			PostID:         post.PostID,
			Topic:          post.Topic,
			Title:          post.Title,
			Subject:        post.Subject,
			SubjectCode:    post.SubjectCode,
			ExpirationDate: post.ExpirationDate,
		})
	}

	response.Object(http.StatusOK, returnPosts, w)
}

func DeletePost(r *http.Request, w http.ResponseWriter, token *structs.Token, postID string) {

	// Load the selected post
	post, err := services.GetPostByID(postID)
	if err != nil {
		response.Error(http.StatusNotFound, "Post not found", w)
		return
	}

	// Authorization if the user owns the post
	if *post.UserID != token.UserID {
		response.Error(http.StatusForbidden, "You cannot delete this post", w)
		return
	}

	// Delete the post
	if err := services.DeletePost(post.PostID); err != nil {
		response.Error(http.StatusInternalServerError, "Failed to delete post", w)
	}

	response.Message(http.StatusOK, "Post deleted!", w)

}
