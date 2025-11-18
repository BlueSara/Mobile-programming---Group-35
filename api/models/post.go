package models

import (
	"time"
)

// struct to use with db for creating/updating a post in the db
type Post struct {
	PostID         string         `firestore:"postID,omitempty"`
	Title          string         `firestore:"title"`
	CreatedAt      time.Time      `firestore:"createdAt"`
	Subject        string         `firestore:"subject"`
	SubjectCode    string         `firestore:"subjectCode"`
	SubjectID      string         `firestore:"subjectID"`
	ExpirationDate time.Time      `firestore:"expirationDate"`
	UseProximity   bool           `firestore:"useProximity"`
	Responses      []UserResponse `firestore:"responses"`
	Topic          string         `firestore:"topic"`
	XCoord         float64        `firestore:"xCoord"`
	YCoord         float64        `firestore:"yCoord"`
	UserID         *string        `firestore:"userID"`
}
