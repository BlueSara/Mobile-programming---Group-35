package controller

import (
	"net/http"
	"strings"
	"studygroup_api/database"
	"studygroup_api/models"
	"studygroup_api/response"
	"studygroup_api/services"
	"studygroup_api/structs"
	"studygroup_api/utils"

	"cloud.google.com/go/firestore"
	"github.com/golang-jwt/jwt/v4"
)

func Signup(r *http.Request, w http.ResponseWriter, credentials *structs.UserSignup) {
	university, universityErr := services.GetUniversityWithID(credentials.UniversityID)
	if universityErr != nil {
		response.Error(http.StatusInternalServerError, universityErr.Error(), w)
		return
	}

	//checking that submitted email is under a affiliated university's domain
	emailAffiliation := strings.Split(strings.Split(credentials.Email, "@")[1], ".")[0]
	if !strings.EqualFold(emailAffiliation, university.Abbr) {
		response.Error(http.StatusConflict, "Submitted email is not a valid university email", w)
		return
	}

	existingUser, existingUserErr := services.GetUserByEmail(credentials.Email)
	if existingUserErr != nil {
		response.Error(http.StatusInternalServerError, "Internal server error", w)
		return
	}
	if existingUser != nil {
		response.Error(http.StatusConflict, "Email already in use", w)
		return
	}

	if credentials.Password != credentials.ConfirmedPAssword {
		response.Error(http.StatusBadRequest, "Password and confirmed password does not match", w)
		return
	}

	var studyProgram *models.StudyProgram
	studyProgramID := credentials.StudyProgram.StudyProgramID
	//checking if user has submitted that they are part of studyprogram or not
	if studyProgramID != "" {
		//checks that ID is valid, e.g. studyprogram exists
		existingStudyProgram, studyProgramErr := services.GetStudyprogramWithID(studyProgramID)
		if studyProgramErr != nil {
			response.Error(http.StatusBadRequest, studyProgramErr.Error(), w)
			return
		}

		//Set as selected study-program
		studyProgram = &existingStudyProgram
	} else {
		newStudyProgram, studyProgramErr := services.CreateNewStudyProgram(*credentials, credentials.UniversityID)
		if studyProgramErr != nil {
			response.Error(http.StatusInternalServerError, studyProgramErr.Error(), w)
			return
		}
		universityUpdateErr := services.UpdateUniversityStudyProgram(newStudyProgram.StudyProgramID, credentials.UniversityID)
		if universityUpdateErr != nil {
			response.Error(http.StatusInternalServerError, "Internal server error", w)
			return
		}
		studyProgram = &newStudyProgram
	}

	var newSubjectsStruct []structs.Subject
	var existingSubjectStruct []structs.Subject
	var userSubjects models.UserSubject

	db, dbErr := database.DB()
	if dbErr != nil {
		response.Error(http.StatusInternalServerError, "Internal server error", w)
		return
	}

	studyProgramRef := db.Client.Collection("studyProgram").Doc(studyProgram.StudyProgramID)
	if len(credentials.Subjects) > 0 {
		for _, s := range credentials.Subjects {
			if s.SubjectID == "" {
				newSubjectsStruct = append(newSubjectsStruct, structs.Subject{
					SubjectName:        s.SubjectName,
					SubjectCode:        s.SubjectCode,
					Obligatory:         s.Obligatory,
					PartOfStudyProgram: s.PartOfStudyProgram,
				})
				continue
			}
			existingSubject, subjectErr := services.GetSubjectWithID(s.SubjectID)
			if subjectErr != nil {
				response.Error(http.StatusNotFound, subjectErr.Error(), w)
				return
			}

			found := false
			for _, sp := range existingSubject.StudyPrograms {
				if sp.StudyProgramID.ID == credentials.StudyProgram.StudyProgramID {
					found = true
					break
				}
			}

			if !found && s.PartOfStudyProgram {
				existingSubject.StudyPrograms = append(existingSubject.StudyPrograms, models.SubjectStudyProgram{
					StudyProgramID: studyProgramRef,
					Obligatory:     s.Obligatory,
				})
				existingSubjectStruct = append(existingSubjectStruct, structs.Subject{
					SubjectID:          s.SubjectID,
					SubjectCode:        s.SubjectCode,
					SubjectName:        s.SubjectName,
					Obligatory:         s.Obligatory,
					PartOfStudyProgram: s.PartOfStudyProgram,
				})
			}

			subjectID := db.Client.Collection("subjects").Doc(s.SubjectID)
			userSubjects.SubjectID = append(userSubjects.SubjectID, subjectID)
		}
	}

	universityRef := db.Client.Collection("university").Doc(credentials.UniversityID)
	var nonExistingSubjects []models.Subject
	for _, ns := range newSubjectsStruct {
		if ns.PartOfStudyProgram {
			nonExistingSubjects = append(nonExistingSubjects, models.Subject{
				UniversityID: universityRef,
				StudyPrograms: []models.SubjectStudyProgram{{
					StudyProgramID: studyProgramRef,
					Obligatory:     ns.Obligatory,
				}},
				Posts: []*firestore.DocumentRef{},
				Name:  ns.SubjectName,
				Code:  ns.SubjectCode,
			})
		}
	}

	for _, nsm := range nonExistingSubjects {
		newSubject, newSubjectErr := services.CreateNewSubject(nsm)
		if newSubjectErr != nil {
			response.Error(http.StatusInternalServerError, "Internal server error", w)
			return
		}
		subjectID := db.Client.Collection("subjects").Doc(newSubject.SubjectID)
		userSubjects.SubjectID = append(userSubjects.SubjectID, subjectID)
	}

	for _, s := range existingSubjectStruct {
		subjectEntry := models.SubjectStudyProgram{
			StudyProgramID: studyProgramRef,
			Obligatory:     s.Obligatory,
		}

		subjectErr := services.SubjectInsertNewStudyProgram(s.SubjectID, subjectEntry)
		if subjectErr != nil {
			response.Error(http.StatusInternalServerError, "Internal server error", w)
			return
		}
	}

	password, passwordErr := utils.HashPwd(credentials.Password)
	if passwordErr != nil {
		response.Error(http.StatusInternalServerError, "Internal server error", w)
		return
	}

	user := models.User{
		FirstName:      credentials.FirstName,
		LastName:       credentials.LastName,
		Password:       password,
		Email:          credentials.Email,
		StartDate:      credentials.StartDate,
		InitialEndDate: credentials.InitialEndDate,
		UpdatedEndDate: credentials.InitialEndDate,
		UniversityID:   universityRef,
		StudyProgram:   studyProgramRef,
		Responses:      []models.PostResponse{},
		Posts:          []string{},
		Subjects:       userSubjects.SubjectID,
	}

	userErr := services.CreateUser(user)
	if userErr != nil {
		response.Error(http.StatusInternalServerError, userErr.Error(), w)
		return
	}

	response.Object(http.StatusOK, "New user successfully", w)
}

func Signin(r *http.Request, w http.ResponseWriter, credentials *structs.UserSignin) {
	//checking that user exists, and getting user credentials
	user, userErr := services.GetUserByEmail(credentials.Email)
	if userErr != nil {
		response.Error(http.StatusNotFound, "Invalid email or password", w)
		return
	}

	//making sure password is correct
	if passwordErr := utils.CheckPassword(credentials.Password, user.Password); passwordErr != nil {
		response.Error(http.StatusNotFound, "Invalid email or password", w)
		return
	}

	//generating token for user
	token, tokenErr := utils.CreateToken(*user)
	if tokenErr != nil {
		response.Error(http.StatusInternalServerError, tokenErr.Error(), w)
		return
	}

	//creating response-object
	responseContent := jwt.MapClaims{
		"token": token,
	}

	//returning response
	response.Object(http.StatusOK, responseContent, w)

}
