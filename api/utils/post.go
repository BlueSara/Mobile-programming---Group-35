package utils

import (
	"studygroup_api/models"
	"studygroup_api/structs"
	"time"
)

func CreatePostModel(postStruct *structs.Post, subject *models.Subject, userID, universityID string) models.Post {

	return models.Post{
		Title:          postStruct.Title,
		CreatedAt:      time.Now(),
		Subject:        subject.Name,
		SubjectCode:    subject.Code,
		SubjectID:      subject.SubjectID,
		ExpirationDate: postStruct.ExpidationDate,
		UseProximity:   postStruct.UseProximity,
		Responses: []models.UserResponse{{
			UserID:   userID,
			Response: "ditto",
		}},
		Topic:        postStruct.Topic,
		XCoord:       postStruct.XCoord,
		YCoord:       postStruct.YCoord,
		UniversityID: universityID,
		UserID:       &userID,
	}
}
