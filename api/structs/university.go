package structs

// struct used for study-program fields
type StudyProgram struct {
	StudyProgramID   string `json:"studyProgramID"`
	StudyProgramName string `json:"studyProgramName" validate:"min=5,max=100,required"`
	StudyProgramAbbr string `json:"studyProgramAbbr" validate:"min=3,max=30,required"`
}

// struct used for subjects that a user has
type Subject struct {
	SubjectID          string `json:"subjectID" validate:"len=20"`
	SubjectCode        string `json:"subjectCode" validate:"min=6,max=10,required"`
	SubjectName        string `json:"subjectName" validate:"min=5,max=50,required"`
	Obligatory         bool   `json:"obligatory"`
	PartOfStudyProgram bool   `json:"partOfStudyProgram"`
}
