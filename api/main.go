package main

import (
	"fmt"
	"log"
	"net/http"
	"studygroup_api/handler"
	"studygroup_api/routes"

	"github.com/joho/godotenv"
)

func main() {
	/*
		router usage example:
		routes.Register("/test/:testID/path", "GET", handler.SomeHandler)
	*/

	if envErr := godotenv.Load(); envErr != nil {
		log.Fatalf("Failed to load environment")
		return
	}

	routes.Register("/auth/signup", "POST", handler.Signup)
	routes.Register("/auth/signin", "POST", handler.Signin)
	routes.Register("/post/create", "POST", handler.CreatePost)
	routes.Register("/post/groups", "GET", handler.GetGroups)
	routes.Register("/post/:postID/answer", "PATCH", handler.AnswerPost)
	routes.Register("/post/:postID/update", "PATCH", handler.UpdateAnswer)
	routes.Register("/posts/:postID", "PUT", handler.EditPost)

	port := ":3000"

	fmt.Println("Server running on port", port)

	http.ListenAndServe(port, http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		routes.Router(r.URL.Path, r.Method, r, w)
	}))
}
