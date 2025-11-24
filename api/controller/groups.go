package controller

import (

	//	"fmt"

	"net/http"
	"reflect"
	"slices"
	"studygroup_api/models"
	"studygroup_api/response"
	"studygroup_api/services"
	"studygroup_api/structs"
	"time"

	"github.com/golang-jwt/jwt/v4"
	// "studygroup_api/utils"
)

func GetGroups(r *http.Request, w http.ResponseWriter, token *structs.Token) {

	groups, getErr := services.GetGroups(token.UserID)
	if getErr != nil {
		response.Error(http.StatusInternalServerError, "The referenced subject was not found", w)
		return
	}

	//getting the post for each of the groups
	var posts []models.Post
	for _, g := range groups {
		post, postErr := services.GetPostByID(g.PostID)
		if postErr != nil {
			response.Error(http.StatusInternalServerError, postErr.Error(), w)
			return
		}
		posts = append(posts, post)
	}

	//iterating over post and structuring content to return
	var out []map[string]any
	for i, post := range posts {
		group := groups[i]
		out = append(out, jwt.MapClaims{
			"postID":  group.PostID,
			"groupID": group.GroupID,
			"topic":   post.Topic,
			"subject": post.Subject,
			"title":   post.Title,
		})
	}

	response.Object(http.StatusOK, out, w)
}

func CreateMeetupSuggestion(r *http.Request, w http.ResponseWriter, token *structs.Token, message structs.Message) {
	if message.Time.Before(time.Now()) {
		response.Error(http.StatusConflict, "Meetup needs to be after current time", w)
		return
	}

	group, groupErr := services.GetGroupByID(message.GroupID)
	if groupErr != nil {
		response.Error(http.StatusInternalServerError, "Internal server error", w)
		return
	}

	if !slices.Contains(group.Participants, token.UserID) {
		response.Error(http.StatusForbidden, "You are not part of this group", w)
		return
	}

	var usersAgreed []string
	usersAgreed = append(usersAgreed, token.UserID)

	messageModel := models.Message{
		UserID:       message.UserID,
		GroupID:      message.GroupID,
		Time:         message.Time,
		Location:     message.Location,
		Building:     message.Building,
		Room:         message.Room,
		Comment:      message.Comment,
		IsSelected:   false,
		UsersAgreed:  usersAgreed,
		UsersDecline: []string{},
	}

	if insertErr := services.InsertMessage(messageModel); insertErr != nil {
		response.Error(http.StatusInternalServerError, "Internal server error", w)
		return
	}

	response.Message(http.StatusOK, "Meetup-suggestion created", w)

}

func AnswerMeetupSuggestion(r *http.Request, w http.ResponseWriter, token *structs.Token, accept bool, groupID, messageID string) {
	userID := token.UserID

	group, groupErr := services.GetGroupByID(groupID)
	if groupErr != nil {
		response.Error(http.StatusInternalServerError, "Internal server error", w)
		return
	}

	if !slices.Contains(group.Participants, userID) {
		response.Error(http.StatusConflict, "You are not part of this group", w)
		return
	}

	message, messageErr := services.GetMessageByID(messageID)
	if messageErr != nil {
		response.Error(http.StatusInternalServerError, "Internal server error", w)
		return
	}

	if reflect.DeepEqual(message, models.Message{}) {
		response.Error(http.StatusNotFound, "Message not found", w)
		return
	}

	var usersAgreed []string
	var UsersDecline []string

	for _, au := range message.UsersAgreed {
		if au != userID {
			usersAgreed = append(usersAgreed, au)
		}
	}

	for _, ud := range message.UsersDecline {
		if ud != userID {
			UsersDecline = append(UsersDecline, ud)
		}
	}

	if accept {
		usersAgreed = append(usersAgreed, userID)
	} else {
		UsersDecline = append(UsersDecline, userID)
	}

	message.UsersAgreed = usersAgreed
	message.UsersDecline = UsersDecline

	if updateErr := services.UpdateMessageReply(messageID, message); updateErr != nil {
		response.Error(http.StatusInternalServerError, "Internal server error", w)
		return
	}

	response.Message(http.StatusOK, "Answer submitted!", w)

}



// row 17 in docs
func GetSingleGroupData(r *http.Request, w http.ResponseWriter, token *structs.Token, groupID string) {
	
	docs, errGetDocs := services.GetAllDocs("messages")
	if errGetDocs != nil {
		response.Error(http.StatusInternalServerError, "Internal server error", w)
	}


	// gets all documents that contains groupID
	var output []map[string]interface{}
	for _, doc := range docs {
		if doc["groupID"] == groupID {
			output = append(output, doc)
		}
	}
	
	// return no content on empty list
	if len(output) == 0 {
		response.Empty(w)
		return 
	}
	
	response.Object(http.StatusOK, output, w)
}
