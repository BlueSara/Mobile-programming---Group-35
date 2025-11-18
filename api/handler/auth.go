package handler

import (
	"encoding/json"
	"net/http"
	"studygroup_api/controller"
	"studygroup_api/middleware/ratelimiting"
	"studygroup_api/middleware/validation"
	"studygroup_api/response"
	"studygroup_api/structs"
)

func Signup(r *http.Request, w http.ResponseWriter, params map[string]string) {
	if limiter := ratelimiting.RateLimiter(); !limiter.Allow() {
		response.Error(http.StatusTooManyRequests, "Too many requests", w)
		return
	}

	//unmarshalling, returning err if fields are invalid
	var credentials *structs.UserSignup
	unmarshalErr := json.NewDecoder(r.Body).Decode(&credentials)
	if unmarshalErr != nil {
		response.Error(http.StatusBadRequest, unmarshalErr.Error(), w)
		return
	}

	//throws panic here
	if validationErr := validation.Signup(credentials); validationErr != nil {
		response.Error(http.StatusBadRequest, "One or more fields are invalid", w)
		return
	}

	controller.Signup(r, w, credentials)
}

func Signin(r *http.Request, w http.ResponseWriter, params map[string]string) {
	if limiter := ratelimiting.RateLimiter(); !limiter.Allow() {
		response.Error(http.StatusTooManyRequests, "Too many requests", w)
		return
	}

	//unmarshalling, returning err if fields are invalid
	var credentials *structs.UserSignin
	unmarshalErr := json.NewDecoder(r.Body).Decode(&credentials)
	if unmarshalErr != nil {
		response.Error(http.StatusBadRequest, unmarshalErr.Error(), w)
		return
	}

	//throws panic here
	if validationErr := validation.Signin(credentials); validationErr != nil {
		response.Error(http.StatusBadRequest, "One or more fields are invalid", w)
		return
	}

	controller.Signin(r, w, credentials)
}
