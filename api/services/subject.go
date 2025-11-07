package services

import (
	"errors"
	"fmt"
	"studygroup_api/database"
	"studygroup_api/models"

	"cloud.google.com/go/firestore"
	"google.golang.org/grpc/codes"
	"google.golang.org/grpc/status"
)

func GetSubjectWithID(subjectID string) (models.Subject, error) {
	db, dbErr := database.DB()
	if dbErr != nil {
		return models.Subject{}, dbErr
	}

	doc, docErr := db.Client.Collection("subjects").Doc(subjectID).Get(db.Ctx)
	if docErr != nil {
		if status.Code(docErr) == codes.NotFound {
			return models.Subject{}, fmt.Errorf("referenced subject not found")
		}
		return models.Subject{}, errors.New("internal server error")
	}

	var subject models.Subject
	if universityErr := doc.DataTo(&subject); universityErr != nil {
		return models.Subject{}, universityErr
	}

	subject.SubjectID = subjectID

	return subject, nil
}

func CreateNewSubject(subject models.Subject) (models.Subject, error) {
	db, dbErr := database.DB()
	if dbErr != nil {
		return models.Subject{}, dbErr
	}

	docRef, _, docRefErr := db.Client.Collection("subjects").Add(db.Ctx, subject)
	if docRefErr != nil {
		return models.Subject{}, docRefErr
	}

	doc, docErr := docRef.Get(db.Ctx)
	if docErr != nil {
		return models.Subject{}, docErr
	}

	var newSubject models.Subject
	if insertErr := doc.DataTo(&newSubject); insertErr != nil {
		return models.Subject{}, insertErr
	}

	newSubject.SubjectID = docRef.ID

	return newSubject, nil
}

func SubjectInsertNewStudyProgram(subjectID string, studyProgram models.SubjectStudyProgram) error {
	db, dbErr := database.DB()
	if dbErr != nil {
		return dbErr
	}

	_, insertErr := db.Client.Collection("subjects").Doc(subjectID).Update(db.Ctx, []firestore.Update{
		{Path: "studyPrograms", Value: firestore.ArrayUnion(studyProgram)},
	})
	return insertErr
}
