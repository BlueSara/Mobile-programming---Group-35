package services

import (
	"studygroup_api/database"
	"studygroup_api/models"

	"cloud.google.com/go/firestore"
)

func InsertNewPost(post models.Post) (string, error) {
	db, dbErr := database.DB()
	if dbErr != nil {
		return "", dbErr
	}

	docRef, _, docRefErr := db.Client.Collection("posts").Add(db.Ctx, post)
	if docRefErr != nil {
		return "", docRefErr
	}

	doc, docErr := docRef.Get(db.Ctx)
	if docErr != nil {
		return "", docErr
	}

	var newPost models.Post
	if insertErr := doc.DataTo(&newPost); insertErr != nil {
		return "", insertErr
	}

	newPost.PostID = docRef.ID

	return newPost.PostID, nil
}

func GetPostByID(postID string) (models.Post, error) {
	db, dbErr := database.DB()
	if dbErr != nil {
		return models.Post{}, dbErr
	}

	postRef := db.Client.Collection("posts").Doc(postID)
	userSnap, userErr := postRef.Get(db.Ctx)
	if userErr != nil {
		return models.Post{}, userErr
	}

	var post models.Post
	if userSnapErr := userSnap.DataTo(&post); userErr != nil {
		return models.Post{}, userSnapErr
	}

	post.PostID = postRef.ID

	return post, nil
}

func InsertPostReply(postID, userID, answer string) error {
	db, dbErr := database.DB()
	if dbErr != nil {
		return dbErr
	}

	userResponse := models.UserResponse{
		UserID:   userID,
		Response: answer,
	}

	_, insertPostErr := db.Client.Collection("posts").Doc(postID).Update(db.Ctx, []firestore.Update{
		{Path: "responses", Value: firestore.ArrayUnion(userResponse)},
	})

	if insertPostErr != nil {
		return insertPostErr
	}

	postResponse := models.PostResponse{
		PostID:   postID,
		Response: answer,
	}

	_, insertUserErr := db.Client.Collection("users").Doc(userID).Update(db.Ctx, []firestore.Update{
		{Path: "responses", Value: firestore.ArrayUnion(postResponse)},
	})

	return insertUserErr
}
