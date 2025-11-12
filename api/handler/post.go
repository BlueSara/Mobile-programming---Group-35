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
		fmt.Print(tokenErr)
		response.Error(http.StatusUnauthorized, "Unauthorized access", w)
		return
	}

	controller.CreatePost(r, w, &token, post)
}
