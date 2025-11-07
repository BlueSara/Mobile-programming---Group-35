package services

import (
	"errors"
	"studygroup_api/database"
	"studygroup_api/models"

	"cloud.google.com/go/firestore"
	"google.golang.org/grpc/codes"
	"google.golang.org/grpc/status"
)

func GetUniversityWithID(universityID string) (models.University, error) {
	db, dbErr := database.DB()
	if dbErr != nil {
		return models.University{}, dbErr
	}

	doc, docErr := db.Client.Collection("university").Doc(universityID).Get(db.Ctx)
	if docErr != nil {
		if status.Code(docErr) == codes.NotFound {
			return models.University{}, errors.New("referenced university not found")
		}
		return models.University{}, errors.New("internal server error")
	}

	var university models.University
	if universityErr := doc.DataTo(&university); universityErr != nil {
		return models.University{}, universityErr
	}

	return university, nil
}

func UpdateUniversityStudyProgram(studyProgramID, universityID string) error {
	db, dbErr := database.DB()
	if dbErr != nil {
		return dbErr
	}

	studyProgramRef := db.Client.Collection("studyProgram").Doc(studyProgramID)

	_, insertErr := db.Client.Collection("university").Doc(universityID).Update(db.Ctx, []firestore.Update{
		{Path: "studyprogram", Value: firestore.ArrayUnion(studyProgramRef)},
	})

	return insertErr
}
