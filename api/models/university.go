package models

import "cloud.google.com/go/firestore"

// struct to use for db for university info
type University struct {
	UniversityID  string                   `firestore:"universityID,omitempty"`
	Name          string                   `firestore:"name"`
	StudyPrograms []*firestore.DocumentRef `firestore:"studyPrograms"`
	Abbr          string                   `firestore:"abbr"`
}

// struct to use for db for studyprogram info
type StudyProgram struct {
	StudyProgramID string                   `firestore:"studyProgramID,omitempty"`
	UniversityID   *firestore.DocumentRef   `firestore:"universityID"`
	Posts          []*firestore.DocumentRef `firestore:"posts"`
	Name           string                   `firestore:"name"`
	Abbr           string                   `firestore:"abbr"`
}

// struct to use for db for in "Subject"'s "studyPrograms" field
type SubjectStudyProgram struct {
	StudyProgramID *firestore.DocumentRef `firestore:"studyProgramID"`
	Obligatory     bool                   `firestore:"obligatory"`
}

// struct to use for db for subjects info
type Subject struct {
	SubjectID     string                   `firestore:"subjectID,omitempty"`
	UniversityID  *firestore.DocumentRef   `firestore:"universityID"`
	StudyPrograms []SubjectStudyProgram    `firestore:"studyPrograms"`
	Posts         []*firestore.DocumentRef `firestore:"posts"`
	Name          string                   `firestore:"name"`
	Code          string                   `firestore:"code"`
}
