package structs

import "time"

// struct used for when a user is signing up
type UserSignup struct {
	FirstName         string       `json:"firstName" validate:"min=2,max=20,required"`
	LastName          string       `json:"lastName" validate:"min=2,max=30,required"`
	Email             string       `json:"email" validate:"email,min=6,max=100,required"`
	Password          string       `json:"password" validate:"min=8,max=150,required"`
	ConfirmedPAssword string       `json:"confirmedPassword" validate:"min=8,max=150,required"`
	StartDate         time.Time    `json:"startDate" validate:"required"`
	InitialEndDate    time.Time    `json:"initialEndDate" validate:"required"`
	UniversityID      string       `json:"universityID" validate:"len=20,required"`
	StudyProgram      StudyProgram `json:"studyProgram"`
	Subjects          []Subject    `json:"subjects"`
}

// struct used for when a user updates their user-account
type UserEdit struct {
	FirstName      string       `json:"firstName" validate:"min=2,max=20,required"`
	LastName       string       `json:"lastName" validate:"min=2,max=30,required"`
	StartDate      time.Time    `json:"startDate" validate:"len=20,required"`
	UpdatedEndDate time.Time    `json:"updatedEndDate" validate:"len=20,required"`
	StudyProgram   StudyProgram `json:"studyProgram"`
	Subjects       []Subject    `json:"subjects"`
}

// struct used for when a user signs into their account
type UserSignin struct {
	Email    string `json:"email" validate:"email,min=6,max=100,required"`
	Password string `json:"password" validate:"min=8,max=150,required"`
}
