package handler

import (
	"encoding/json"
	"fmt"
	"net/http"
	"studygroup_api/controller"
	"studygroup_api/middleware/auth"
	"studygroup_api/middleware/ratelimiting"
	"studygroup_api/response"
	"studygroup_api/structs"

	"github.com/go-playground/validator/v10"
)

func GetGroups(r *http.Request, w http.ResponseWriter, params map[string]string) {

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

	controller.GetGroups(r, w, &token)
}

func CreateMeetupSuggestion(r *http.Request, w http.ResponseWriter, params map[string]string) {

	var message structs.Message
	if unmarshalErr := json.NewDecoder(r.Body).Decode(&message); unmarshalErr != nil {
		response.Error(http.StatusBadRequest, "Invalid input", w)
		return
	}

	message.GroupID = params["groupID"]
	v := validator.New()
	if validationErr := v.Struct(message); validationErr != nil {
		response.Error(http.StatusBadRequest, "Invalid input", w)
		return
	}

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

	message.UserID = token.UserID

	controller.CreateMeetupSuggestion(r, w, &token, message)
}
