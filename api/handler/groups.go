package handler

import (
	"fmt"
	"net/http"
	"studygroup_api/controller"
	"studygroup_api/middleware/auth"
	"studygroup_api/middleware/ratelimiting"
	"studygroup_api/response"
)

func GetPosts(r *http.Request, w http.ResponseWriter, params map[string]string) {

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

	controller.GetPosts(r, w, &token)
}
