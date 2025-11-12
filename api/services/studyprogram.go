package services

import (
	"errors"
	"studygroup_api/database"
	"studygroup_api/models"
	"studygroup_api/structs"

	"cloud.google.com/go/firestore"
	"google.golang.org/grpc/codes"
	"google.golang.org/grpc/status"
)

func GetStudyprogramWithID(studyProgramID string) (models.StudyProgram, error) {
	db, dbErr := database.DB()
	if dbErr != nil {
		return models.StudyProgram{}, dbErr
	}

	doc, docErr := db.Client.Collection("studyProgram").Doc(studyProgramID).Get(db.Ctx)
	if docErr != nil {
		if status.Code(docErr) == codes.NotFound {
			return models.StudyProgram{}, errors.New("referenced studyprogram not found")
		}
		return models.StudyProgram{}, errors.New("internal server error")
	}

	var studyProgram models.StudyProgram
	if universityErr := doc.DataTo(&studyProgram); universityErr != nil {
		return models.StudyProgram{}, universityErr
	}

	studyProgram.StudyProgramID = studyProgramID

	return studyProgram, nil
}

func CreateNewStudyProgram(credentials structs.UserSignup, universityID string) (models.StudyProgram, error) {
	db, dbErr := database.DB()
	if dbErr != nil {
		return models.StudyProgram{}, dbErr
	}

	universityRef := db.Client.Collection("university").Doc(credentials.UniversityID)

	studyProgram := models.StudyProgram{
		UniversityID: universityRef,
		Posts:        []*firestore.DocumentRef{},
		Name:         credentials.StudyProgram.StudyProgramName,
		Abbr:         credentials.StudyProgram.StudyProgramAbbr,
	}

	docRef, _, docRefErr := db.Client.Collection("studyProgram").Add(db.Ctx, studyProgram)
	if docRefErr != nil {
		return models.StudyProgram{}, docRefErr
	}

	doc, docErr := docRef.Get(db.Ctx)
	if docErr != nil {
		return models.StudyProgram{}, docErr
	}

	var newStudyProgram models.StudyProgram
	if insertErr := doc.DataTo(&newStudyProgram); insertErr != nil {
		return models.StudyProgram{}, insertErr
	}

	newStudyProgram.StudyProgramID = docRef.ID

	return newStudyProgram, nil
}
