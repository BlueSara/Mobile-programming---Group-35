package structs

import "time"

// struct used when a user is fetching posts
type FetchPost struct {
	Page   int     `json:"page"`
	XCoord float64 `json:"xCoord" validate:"required,min=-180,max=180"`
	YCoord float64 `json:"yCoord" validate:"required,min=-90,max=90"`
}

// struct used when a user is creating/editing a post
type Post struct {
	Title          string    `json:"title" validate:"min=5,max=200,required"`
	SubjectID      string    `json:"subjectID" validate:"len=20,required"`
	Topic          string    `json:"topic" validate:"min=4,max=30"`
	UseProximity   bool      `json:"useProximity"`
	ExpidationDate time.Time `json:"expirationDate" validate:"len=20,required"`
	XCoord         float64   `json:"xCoord" validate:"min=-180,max=180"`
	YCoord         float64   `json:"yCoord" validate:"min=-90,max=90"`
}

// struct used for formatting posts returned to used.
// Using this instead of "Posts" struct due to that there are
// some fundamental structural differences and requirements
type ReturnPost struct {
	PostID         string `json:"postID"`
	Title          string `json:"title"`
	Subject        string `json:"subject"`
	SubjectCode    string `json:"subjectCode"`
	ExpirationDate string `json:"expirationDate"`
	Topic          string `json:"topic"`
}

// struct used when a user is creating a meetup suggestion
type Meetup struct {
	Time     time.Time `json:"time" validate:"len=20,required"`
	Location string    `json:"location" validate:"min=4,max=75,required"`
	Building string    `json:"building" validate:"min=0,max=30"`
	Room     string    `json:"room" validate:"min=0,max=30"`
	Comment  string    `json:"comment" validate:"min=0,max=150"`
}
