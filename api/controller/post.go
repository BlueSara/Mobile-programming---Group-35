package controller

import (
	"net/http"
	"studygroup_api/response"
	"studygroup_api/services"
	"studygroup_api/structs"
	"studygroup_api/utils"
)

func CreatePost(r *http.Request, w http.ResponseWriter, token *structs.Token, postStruct *structs.Post) {

	subject, subjectErr := services.GetSubjectWithID(postStruct.SubjectID)
	if subjectErr != nil {
		response.Error(http.StatusNotFound, "The referenced subject was not found", w)
		return
	}

	post := utils.CreatePostModel(postStruct, &subject, token.UserID)

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
