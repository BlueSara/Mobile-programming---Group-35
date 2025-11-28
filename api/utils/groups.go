package utils

import (
	"slices"
	"studygroup_api/models"
)

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

func FindDecliningUsers(group models.Group, message models.Message, post models.Post, userID string) (bool, bool, bool) {
	var assistAgreed bool

	userAgreed := slices.Contains(message.UsersAgreed, userID)
	ownerAgreed := slices.Contains(message.UsersAgreed, *post.UserID)

	for _, a := range group.AssistingUsers {
		assistAgreed = slices.Contains(message.UsersAgreed, a)
	}

	return userAgreed, ownerAgreed, assistAgreed
}

func MeetingIsSelected(ownerAgreed, assistAgreed bool, group models.Group, message models.Message) bool {
	return len(group.Participants)/2 >= len(message.UsersAgreed) && ownerAgreed && assistAgreed
}
