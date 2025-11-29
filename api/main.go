package main

import (
	"fmt"
	"log"
	"net/http"
	"os"
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
		//return
	}

	routes.Register("/auth/signup", "POST", handler.Signup)
	routes.Register("/auth/signin", "POST", handler.Signin)
	routes.Register("/post/create", "POST", handler.CreatePost)
	routes.Register("/posts", "GET", handler.GetAllPosts)
	routes.Register("/post/groups", "GET", handler.GetGroups)
	routes.Register("/post/:postID/answer", "PATCH", handler.AnswerPost)
	routes.Register("/post/:postID/update", "PATCH", handler.UpdateAnswer)
	routes.Register("/post/:postID", "DELETE", handler.DeletePost)
	routes.Register("/posts/:postID", "PUT", handler.EditPost)
	routes.Register("groups/:groupID", "POST", handler.CreateMeetupSuggestion)
	routes.Register("groups/:groupID/messages/:messageID", "PATCH", handler.AnswerMeetupSuggestion)
	routes.Register("/groups/:groupID", "GET", handler.GetSingleGroupData) // row 17
	routes.Register("/posts/replied", "GET", handler.GetRepliedPosts)      // row 14

	port := os.Getenv("PORT")
	if port == "" {
		port = "3000"
	}

	fmt.Println("Server running on port", port)

	http.ListenAndServe("0.0.0.0:"+port, http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		routes.Router(r.URL.Path, r.Method, r, w)
	}))
}
