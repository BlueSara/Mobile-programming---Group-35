package structs

import (
	"studygroup_api/models"

	"cloud.google.com/go/firestore"
	"github.com/golang-jwt/jwt/v4"
)

type Token struct {
	jwt.RegisteredClaims
	UserID       string                   `json:"userID"`
	University   *firestore.DocumentRef   `json:"university"`
	Subjects     []*firestore.DocumentRef `json:"subjects"`
	StudyProgram *firestore.DocumentRef   `json:"studyProgram"`
	Posts        []*firestore.DocumentRef `json:"posts"`
	Responses    []models.UserResponse    `json:"responses"`
}
