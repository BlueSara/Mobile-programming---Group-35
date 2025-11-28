package services

import (
	"studygroup_api/database"
	"studygroup_api/models"

	"cloud.google.com/go/firestore"
	"google.golang.org/api/iterator"
)

func GetUserByEmail(email string) (*models.User, error) {
	db, dbErr := database.DB()
	if dbErr != nil {
		return nil, dbErr
	}

	iter := db.Client.Collection("users").Where("email", "==", email).Limit(1).Documents(db.Ctx)
	defer iter.Stop()

	document, documentErr := iter.Next()
	if documentErr != nil {
		if documentErr == iterator.Done {
			return nil, nil
		}
		return nil, documentErr
	}

	var user models.User
	if userErr := document.DataTo(&user); userErr != nil {
		return nil, userErr
	}

	user.UserID = document.Ref.ID

	return &user, nil
}

func CreateUser(user models.User) error {
	db, dbErr := database.DB()
	if dbErr != nil {
		return dbErr
	}

	_, _, docRefErr := db.Client.Collection("users").Add(db.Ctx, user)

	return docRefErr
}

func UpdateUsersPosts(userID, postID string) error {
	db, dbErr := database.DB()
	if dbErr != nil {
		return dbErr
	}

	_, insertErr := db.Client.Collection("users").Doc(userID).Update(db.Ctx, []firestore.Update{
		{Path: "posts", Value: firestore.ArrayUnion(postID)},
	})

	return insertErr
}

func FindUserByID(userID string) (models.User, error) {
	db, dbErr := database.DB()
	if dbErr != nil {
		return models.User{}, dbErr
	}

	userRef := db.Client.Collection("users").Doc(userID)
	userSnap, userErr := userRef.Get(db.Ctx)
	if userErr != nil {
		return models.User{}, userErr
	}

	var user models.User
	if userSnapErr := userSnap.DataTo(&user); userErr != nil {
		return models.User{}, userSnapErr
	}

	return user, nil
}
