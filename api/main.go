package main

import (
	"fmt"
	"net/http"
	"studygroup_api/handler"
	"studygroup_api/routes"
)

func main() {
	/*
		router usage example:
		routes.Register("/test/:testID/path", "GET", handler.SomeHandler)
	*/

	routes.Register("/auth/signup", "POST", handler.Signup)
	routes.Register("/auth/signin", "POST", handler.Signin)

	port := ":3000"

	fmt.Println("Server running on port", port)

	http.ListenAndServe(port, http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		routes.Router(r.URL.Path, r.Method, r, w)
	}))
}
