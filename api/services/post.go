package services

import (
	"studygroup_api/database"
	"studygroup_api/models"

	"cloud.google.com/go/firestore"
	"google.golang.org/api/iterator"
)

func InsertNewPost(post models.Post) (string, error) {
	db, dbErr := database.DB()
	if dbErr != nil {
		return "", dbErr
	}

	docRef, _, docRefErr := db.Client.Collection("posts").Add(db.Ctx, post)
	if docRefErr != nil {
		return "", docRefErr
	}

	doc, docErr := docRef.Get(db.Ctx)
	if docErr != nil {
		return "", docErr
	}

	var newPost models.Post
	if insertErr := doc.DataTo(&newPost); insertErr != nil {
		return "", insertErr
	}

	newPost.PostID = docRef.ID

	return newPost.PostID, nil
}

// GetAllPosts loads all posts from Firestore and returns them.
func GetAllPosts() ([]models.Post, error) {

	db, dbErr := database.DB()
	if dbErr != nil {

		return nil, dbErr
	}

	iter := db.Client.Collection("posts").Documents(db.Ctx)
	var posts []models.Post

	for {
		doc, err := iter.Next()
		if err == iterator.Done {
			break // Stop iterating
		}
		if err != nil {
			return nil, err
		}

		var p models.Post
		// Decode Firestore document into struct
		if err := doc.DataTo(&p); err != nil {
			return nil, err
		}

		p.PostID = doc.Ref.ID
		posts = append(posts, p)
	}

	return posts, nil
}

func GetPostByID(postID string) (models.Post, error) {
	db, dbErr := database.DB()
	if dbErr != nil {
		return models.Post{}, dbErr
	}

	postRef := db.Client.Collection("posts").Doc(postID)
	userSnap, userErr := postRef.Get(db.Ctx)
	if userErr != nil {
		return models.Post{}, userErr
	}

	var post models.Post
	if userSnapErr := userSnap.DataTo(&post); userErr != nil {
		return models.Post{}, userSnapErr
	}

	post.PostID = postRef.ID

	return post, nil
}

func UpdatePostReply(userID, answer string, post models.Post, user models.User) error {
	db, dbErr := database.DB()
	if dbErr != nil {
		return dbErr
	}

	var postResponses []models.UserResponse
	for _, r := range post.Responses {
		if r.UserID == userID {
			postResponses = append(postResponses, models.UserResponse{
				UserID:   userID,
				Response: answer,
			})
		} else if r.UserID != userID {
			postResponses = append(postResponses, r)
		}
	}

	var userResponses []models.PostResponse
	for _, r := range user.Responses {
		if r.PostID == post.PostID {
			userResponses = append(userResponses, models.PostResponse{
				PostID:   post.PostID,
				Response: answer,
			})
		} else if r.PostID != post.PostID {
			userResponses = append(userResponses, r)
		}
	}

	postRef := db.Client.Collection("posts").Doc(post.PostID)
	userRef := db.Client.Collection("users").Doc(userID)

	if _, updatePostErr := postRef.Update(db.Ctx, []firestore.Update{
		{Path: "responses", Value: postResponses},
	}); updatePostErr != nil {
		return updatePostErr
	}

	if _, updateUserErr := userRef.Update(db.Ctx, []firestore.Update{
		{Path: "responses", Value: userResponses},
	}); updateUserErr != nil {
		return updateUserErr
	}

	return nil
}

func InsertPostReply(postID, userID, answer string) error {
	db, dbErr := database.DB()
	if dbErr != nil {
		return dbErr
	}

	userResponse := models.UserResponse{
		UserID:   userID,
		Response: answer,
	}

	_, insertPostErr := db.Client.Collection("posts").Doc(postID).Update(db.Ctx, []firestore.Update{
		{Path: "responses", Value: firestore.ArrayUnion(userResponse)},
	})

	if insertPostErr != nil {
		return insertPostErr
	}

	postResponse := models.PostResponse{
		PostID:   postID,
		Response: answer,
	}

	_, insertUserErr := db.Client.Collection("users").Doc(userID).Update(db.Ctx, []firestore.Update{
		{Path: "responses", Value: firestore.ArrayUnion(postResponse)},
	})

	return insertUserErr
}




// A function that edit a document with a given id in a given collection
// Set overwrites the document completely. Which is what we want !
func OverwriteDoc(collection, id string, json interface{}) error {
	db, dbErr := database.DB()
	if dbErr != nil {
		return dbErr
	}
	
	// Overwriting the content of the document with the provided ID, if not, then we return an error
	_, errOverWrite := db.Client.Collection(collection).Doc(id).Set(db.Ctx, json)
	return errOverWrite
}