package controller

import (

	//	"fmt"
	"net/http"
	"studygroup_api/models"
	"studygroup_api/response"
	"studygroup_api/services"
	"studygroup_api/structs"

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
