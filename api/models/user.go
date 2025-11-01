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
	PostID   *firestore.DocumentRef `firestore:"postID,omitempty"`
	UserID   *firestore.DocumentRef `firestore:"userID,omitempty"`
	Response string                 `firestore:"response"`
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
	StudyProgram   StudyProgram             `firestore:"studyProgram"`
	Subjects       []Subject                `firestore:"subjects"`
	Responses      []UserResponse           `firestore:"responses"`
	Posts          []*firestore.DocumentRef `firestore:"posts"`
}

// struct to use with db for groups made when matches have been created from a post
// GroupID should be empty when inserting/updating in db
type Group struct {
	GroupID        string                   `firestore:"groupID,omitempty"`
	PostID         *firestore.DocumentRef   `firestore:"postID"`
	Participants   []*firestore.DocumentRef `firestore:"participants"`
	AssistingUsers []*firestore.DocumentRef `firestore:"assistingUsers"`
	DittoUsers     []*firestore.DocumentRef `firestore:"dittoUsers"`
	Messages       []*firestore.DocumentRef `firestore:"messages"`
}

// struct to use with db for storing messages sent in groups
// MessageID should be empty when inserting/updating in db
type Message struct {
	MessageID    string                   `firestore:"messageID,omitempty"`
	GroupID      *firestore.DocumentRef   `firestore:"groupID"`
	PostID       *firestore.DocumentRef   `firestore:"postID"`
	Time         time.Time                `firestore:"time"`
	Location     string                   `firestore:"location"`
	Building     string                   `firestore:"building"`
	Room         string                   `firestore:"room"`
	Comment      string                   `firestore:"comment"`
	UsersAgreed  []*firestore.DocumentRef `firestore:"usersAgreed"`
	UsersDecline []*firestore.DocumentRef `firestore:"usersDecline"`
	IsSelected   bool                     `firestore:"isSelected"`
}

// struct to use with db for tracking user sessions (signed in/signed out users)
type Session struct {
	SessionID  string                 `firestore:"sessionID,omitempty"`
	UserID     *firestore.DocumentRef `firestore:"userID"`
	IsSignedIn bool                   `firestore:"isSignedIn"`
	SignedInAt time.Time              `firestore:"signedInAt"`
}
