package routes

import (
	"net/http"
	"strings"
	"studygroup_api/response"
)

// for mapping params, such as ":postID"
type Param map[string]string

// The handler function, for handling middleware etc before, and then calling
type Handler func(*http.Request, http.ResponseWriter, map[string]string)

type Route struct {
	Children map[string]*Route
	Param    string
	Handler  map[string]Handler
}

// holds all routes so that they can be reached by mapping instead of looping
var routes = &Route{Children: make(map[string]*Route)}

// registers a single route at a time
func Register(path string, method string, handler Handler) {
	parts := strings.Split(strings.Trim(path, "/"), "/")
	node := routes

	for _, p := range parts {
		key := p
		if strings.HasPrefix(p, ":") {
			key = "*"
		}

		if node.Children[key] == nil {
			node.Children[key] = &Route{Children: make(map[string]*Route)}
			if key == "*" {
				node.Children[key].Param = p[1:]
			}
		}
		node = node.Children[key]
	}

	if node.Handler == nil {
		node.Handler = make(map[string]Handler)
	}

	node.Handler[method] = handler
}

// is called in main.go
func Router(path, method string, r *http.Request, w http.ResponseWriter) {

	parts := strings.Split(strings.Trim(path, "/"), "/")
	node := routes
	params := make(Param)

	for _, p := range parts {
		if node.Children[p] != nil {
			node = node.Children[p]
		} else if node.Children["*"] != nil {
			node = node.Children["*"]
			params[node.Param] = p
		} else {
			response.Error(http.StatusNotFound, "Not found", w)
			return
		}
	}

	handler, handlerOK := node.Handler[method]
	if !handlerOK {
		response.Error(http.StatusMethodNotAllowed, "Method not allowed", w)
		return
	}

	handler(r, w, params)
}
