package services

import (
	"studygroup_api/database"
	"studygroup_api/models"

	"cloud.google.com/go/firestore"
	"google.golang.org/api/iterator"
	"google.golang.org/grpc/codes"
	"google.golang.org/grpc/status"
)

func GetGroupByID(postID string) (models.Group, error) {
	db, dbErr := database.DB()
	if dbErr != nil {
		return models.Group{}, dbErr
	}

	iter := db.Client.Collection("groups").Where("postID", "==", postID).Limit(1).Documents(db.Ctx)
	defer iter.Stop()

	document, documentErr := iter.Next()
	if documentErr != nil {
		if status.Code(documentErr) == codes.NotFound {
			return models.Group{}, nil
		}
		if documentErr == iterator.Done {
			return models.Group{}, nil
		}
		return models.Group{}, documentErr
	}

	var group models.Group
	if userErr := document.DataTo(&group); userErr != nil {
		return models.Group{}, userErr
	}

	group.GroupID = document.Ref.ID

	return group, nil
}

func CheckIfAnyAssistingUsers(responses []models.UserResponse) bool {
	for _, r := range responses {
		if r.Response == "assist" {
			return true
		}
	}
	return false
}

func CreateGroup(post models.Post, dittoUsers, assistUsers, participants []string) error {
	db, dbErr := database.DB()
	if dbErr != nil {
		return dbErr
	}

	group := models.Group{
		PostID:         post.PostID,
		Participants:   participants,
		AssistingUsers: assistUsers,
		DittoUsers:     dittoUsers,
		Messages:       []string{},
	}

	docRef, _, docRefErr := db.Client.Collection("groups").Add(db.Ctx, group)
	if docRefErr != nil {
		return docRefErr
	}

	_, docErr := docRef.Get(db.Ctx)

	return docErr
}

func AddNewGroupMember(groupID, userID, answer string) error {
	db, dbErr := database.DB()
	if dbErr != nil {
		return dbErr
	}

	var responseType string
	switch answer {
	case "ditto":
		responseType = "dittoUsers"
	case "assist":
		responseType = "assistingUsers"
	}

	_, insertUserTypeErr := db.Client.Collection("groups").Doc(groupID).Update(db.Ctx, []firestore.Update{
		{Path: responseType, Value: firestore.ArrayUnion(userID)},
	})

	if insertUserTypeErr != nil {
		return insertUserTypeErr
	}

	_, insertParticipant := db.Client.Collection("groups").Doc(groupID).Update(db.Ctx, []firestore.Update{
		{Path: "participants", Value: firestore.ArrayUnion(userID)},
	})

	return insertParticipant
}
