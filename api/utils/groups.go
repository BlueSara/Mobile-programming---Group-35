package utils

import "studygroup_api/models"

func CreateMembersNewGroup(responses []models.UserResponse) ([]string, []string, []string) {
	var dittoUsers []string
	var assistUsers []string
	var participants []string
	for _, r := range responses {
		if r.Response == "ditto" {
			dittoUsers = append(dittoUsers, r.UserID)
			participants = append(participants, r.UserID)
		}
		if r.Response == "assist" {
			assistUsers = append(assistUsers, r.UserID)
			participants = append(participants, r.UserID)
		}
	}

	return dittoUsers, assistUsers, participants
}
