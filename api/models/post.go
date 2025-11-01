package models

import (
	"time"

	"cloud.google.com/go/firestore"
)

// struct to use with db for creating/updating a post in the db
type Post struct {
	PostID         string                 `firestore:"postID,omitEmpty"`
	Title          string                 `firestore:"title"`
	CreatedAt      time.Time              `firestore:"createdAt"`
	Subject        string                 `firestore:"subject"`
	SubjectCode    string                 `firestore:"subjectCode"`
	SubjectID      *firestore.DocumentRef `firestore:"subjectID"`
	ExpirationDate time.Time              `firestore:"expirationDate"`
	UseProximity   bool                   `firestore:"useProximity"`
	Responses      []UserResponse         `firestore:"responses"`
	Topic          string                 `firestore:"topic"`
	XCoord         float64                `firestore:"xCoord"`
	YCoord         float64                `firestore:"yCoord"`
	UserID         *firestore.DocumentRef `firestore:"userID"`
}
