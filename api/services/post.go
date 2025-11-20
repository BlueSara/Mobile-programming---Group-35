package services

import (
	"context"
	"studygroup_api/database"
	"studygroup_api/models"
	"studygroup_api/structs"
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

func GetAllPosts(collection structs.FetchPost) ([]structs.ReturnPost, error) {

	db, dbErr := database.DB()
	if dbErr != nil {

		return nil, dbErr
	}

	iter := db.Client.Collection(collection).Documents(db.Ctx)

}
