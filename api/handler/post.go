package handler

import (
	"encoding/json"
	"fmt"
	"net/http"
	"studygroup_api/controller"
	"studygroup_api/middleware/auth"
	"studygroup_api/middleware/ratelimiting"
	"studygroup_api/middleware/validation"
	"studygroup_api/response"
	"studygroup_api/structs"
)

func CreatePost(r *http.Request, w http.ResponseWriter, params map[string]string) {

	if limiter := ratelimiting.RateLimiter(); !limiter.Allow() {
		response.Error(http.StatusTooManyRequests, "Too many requests", w)
		return
	}

	//unmarshalling, returning err if fields are invalid
	var post *structs.Post
	unmarshalErr := json.NewDecoder(r.Body).Decode(&post)
	if unmarshalErr != nil {
		response.Error(http.StatusBadRequest, unmarshalErr.Error(), w)
		return
	}

	if validationErr := validation.Post(post); validationErr != nil {
		response.Error(http.StatusBadRequest, "One or more fields are invalid or missing", w)
		return
	}

	token, tokenErr := auth.IsUserAuth(r)
	if tokenErr != nil {
		response.Error(http.StatusUnauthorized, "Unauthorized access", w)
		return
	}

	controller.CreatePost(r, w, &token, post)
}

func AnswerPost(r *http.Request, w http.ResponseWriter, params map[string]string) {
	if limiter := ratelimiting.RateLimiter(); !limiter.Allow() {
		response.Error(http.StatusTooManyRequests, "Too many requests", w)
		return
	}

	var rawAnswer map[string]string
	if unmarshalErr := json.NewDecoder(r.Body).Decode(&rawAnswer); unmarshalErr != nil {
		response.Error(http.StatusBadRequest, "Invalid input", w)
		return
	}

	answer := rawAnswer["answer"]

	if answer != "ditto" && answer != "assist" && answer != "skip" {
		response.Error(http.StatusBadRequest, "Invalid input", w)
		return
	}

	//unmarshalling, returning err if fields are invalid
	postID := params["postID"]
	if len(postID) > 22 || len(postID) < 19 {
		response.Error(http.StatusBadRequest, "Invalid post indentification", w)
		return
	}

	token, tokenErr := auth.IsUserAuth(r)
	if tokenErr != nil {
		response.Error(http.StatusUnauthorized, "Unauthorized access", w)
		return
	}

	controller.AnswerPost(r, w, &token, postID, answer)
}

func UpdateAnswer(r *http.Request, w http.ResponseWriter, params map[string]string) {
	if limiter := ratelimiting.RateLimiter(); !limiter.Allow() {
		response.Error(http.StatusTooManyRequests, "Too many requests", w)
		return
	}

	var rawAnswer map[string]string
	if unmarshalErr := json.NewDecoder(r.Body).Decode(&rawAnswer); unmarshalErr != nil {
		response.Error(http.StatusBadRequest, "Invalid input", w)
		return
	}

	answer := rawAnswer["answer"]

	if answer != "ditto" && answer != "assist" && answer != "skip" {
		response.Error(http.StatusBadRequest, "Invalid input", w)
		return
	}

	//unmarshalling, returning err if fields are invalid
	postID := params["postID"]
	if len(postID) > 22 || len(postID) < 19 {
		response.Error(http.StatusBadRequest, "Invalid post indentification", w)
		return
	}

	token, tokenErr := auth.IsUserAuth(r)
	if tokenErr != nil {
		response.Error(http.StatusUnauthorized, "Unauthorized access", w)
		return
	}

	controller.UpdateAnswer(r, w, &token, postID, answer)
}

// GetAllPosts handles requests to fetch all posts available to the authenticated
// user. It applies rate limiting, verifies authentication, and then delegates
// the request to the controller.
//
// Parameters:
//   - r: incoming HTTP request
//   - w: HTTP response writer
//   - params: route parameters (unused)
func GetAllPosts(r *http.Request, w http.ResponseWriter, params map[string]string) {
	if limiter := ratelimiting.RateLimiter(); !limiter.Allow() {
		response.Error(http.StatusTooManyRequests, "Too many requests", w)
		return
	}

	token, tokenErr := auth.IsUserAuth(r)
	if tokenErr != nil {
		fmt.Print(tokenErr)
		response.Error(http.StatusUnauthorized, "Unauthorized access", w)
		return
	}

	controller.GetAllPosts(r, w, &token)

}

// DeletePost handles requests for deleting a post.
// Using rate limiting, validates the post ID, checks user authentication, and then forwards the
// request to the controller layer.
//
// @Param r: the incoming HTTP request
// @Param w: the HTTP response writer
// @Params: route parameters containing the postID
func DeletePost(r *http.Request, w http.ResponseWriter, params map[string]string) {
	// Rate limit check
	if limiter := ratelimiting.RateLimiter(); !limiter.Allow() {
		response.Error(http.StatusTooManyRequests, "Too many requests", w)
		return
	}
	// Extract postID
	postID := params["postID"]
	// Sanity validation of Firestore document ID lenght
	if len(postID) > 22 || len(postID) < 19 {
		response.Error(http.StatusBadRequest, "Invalid post indentification", w)
		return
	}
	// AUthenticate the user making the request
	token, tokenErr := auth.IsUserAuth(r)
	if tokenErr != nil {
		response.Error(http.StatusUnauthorized, "Unauthorized access", w)
		return
	}
	// Deletion logic from the controller
	controller.DeletePost(r, w, &token, postID)
}
