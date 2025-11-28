package models

import (
	"time"

	"cloud.google.com/go/firestore"
)

// struct to use with db to track what a user responded to a post/meetup suggestion
// is used in "User" and "Post" struct. When using in "Post", field "PostID" should be empty
// when using in "User", field "UserID" should be empty
// -> or else these just refer to the document itself
type UserResponse struct {
	UserID   string `firestore:"userID,omitempty"`
	Response string `firestore:"response"`
}

type UserSubject struct {
	SubjectID  []*firestore.DocumentRef `firestore:"subjectID"`
	Obligatory bool                     `firestore:"obligatory"`
}

// struct to use with db for inserting/extracting user-information
// userID should be empty when inserting/updating in db
type User struct {
	UserID         string                   `firestore:"userID,omitempty"`
	FirstName      string                   `firestore:"firstName"`
	LastName       string                   `firestore:"lastName"`
	Password       string                   `firestore:"password"`
	StartDate      time.Time                `firestore:"startDate"`
	Email          string                   `firestore:"email"`
	InitialEndDate time.Time                `firestore:"initialEndDate"`
	UpdatedEndDate time.Time                `firestore:"updatedEndDate"`
	UniversityID   *firestore.DocumentRef   `firestore:"universityID"`
	StudyProgram   *firestore.DocumentRef   `firestore:"studyProgram"`
	Subjects       []*firestore.DocumentRef `firestore:"subjects"`
	Responses      []PostResponse           `firestore:"responses"`
	Posts          []string                 `firestore:"posts"`
}

// struct to use with db for groups made when matches have been created from a post
// GroupID should be empty when inserting/updating in db
type Group struct {
	GroupID        string   `firestore:"groupID,omitempty"`
	PostID         string   `firestore:"postID"`
	Participants   []string `firestore:"participants"`
	AssistingUsers []string `firestore:"assistingUsers"`
	DittoUsers     []string `firestore:"dittoUsers"`
	Messages       []string `firestore:"messages"`
}

// struct to use with db for storing messages sent in groups
// MessageID should be empty when inserting/updating in db
type Message struct {
	MessageID    string    `firestore:"messageID,omitempty"`
	UserID       string    `firestore:"userID"`
	GroupID      string    `firestore:"groupID"`
	Time         time.Time `firestore:"time"`
	Location     string    `firestore:"location"`
	Building     string    `firestore:"building"`
	Room         string    `firestore:"room"`
	Comment      string    `firestore:"comment"`
	UsersAgreed  []string  `firestore:"usersAgreed"`
	UsersDecline []string  `firestore:"usersDecline"`
	IsSelected   bool      `firestore:"isSelected"`
}
