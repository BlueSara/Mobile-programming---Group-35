package structs

// struct used for study-program fields
type StudyProgram struct {
	StudyProgramID   string `json:"studyProgramID" validation:"len=20"`
	StudyProgramName string `json:"studyProgramName" validation:"min=5,max=100,required"`
	StudyProgramAbbr string `json:"studyProgramAbbr" validation:"min=3,max=30,required"`
}

// struct used for subjects that a user has
type Subject struct {
	SubjectID          string `json:"subjectID" validation:"len=20"`
	SubjectName        string `json:"subjectName" validation:"min=5,max=50,required"`
	Obligatory         bool   `json:"obligatory"`
	PartOfStudyProgram bool   `json:"partOfStudyProgram"`
}
