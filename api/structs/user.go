package structs

import "time"

// struct used for when a user is signing up
type UserSignup struct {
	FirstName         string       `json:"firstName" validation:"min=2,max=20,required"`
	LastName          string       `json:"lastName" validation:"min=2,max=30,required"`
	Email             string       `json:"email" validation:"min=6,max=100,required"`
	Password          string       `json:"password" validation:"min=8,max=150,required"`
	ConfirmedPAssword string       `json:"confirmedPassword" validation:"min=8,max=150,required"`
	StartDate         time.Time    `json:"startDate" validation:"len=20,required"`
	InitialEndDate    time.Time    `json:"initialEndDate" validation:"len=20,required"`
	UniversityID      string       `json:"universityID" validation:"len=20,required"`
	StudyProgram      StudyProgram `json:"studyProgram"`
	Subjects          []Subject    `json:"subjects"`
}

// struct used for when a user updates their user-account
type UserEdit struct {
	FirstName      string       `json:"firstName" validation:"min=2,max=20,required"`
	LastName       string       `json:"lastName" validation:"min=2,max=30,required"`
	StartDate      time.Time    `json:"startDate" validation:"len=20,required"`
	UpdatedEndDate time.Time    `json:"updatedEndDate" validation:"len=20,required"`
	StudyProgram   StudyProgram `json:"studyProgram"`
	Subjects       []Subject    `json:"subjects"`
}

// struct used for when a user signs into their account
type UserSignin struct {
	Email    string `json:"email" validation:"min=6,max=100,required"`
	Password string `json:"password" validation:"min=8,max=150,required"`
}
