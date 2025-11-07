package services

import (
	"studygroup_api/database"
	"studygroup_api/models"
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
