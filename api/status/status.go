package status

import (
	"encoding/json"
	"net/http"
)

func headers(w http.ResponseWriter, code int) {
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(code)
}

// usage: "status.Error(http.statusNotFound, "Could not find resource", w)"
func Error(code int, message string, w http.ResponseWriter) {
	headers(w, code)
	response := map[string]string{"message": message}
	json.NewEncoder(w).Encode(response)
}

// usage: "status.Message(http.statusOk, "Here is some message", w)"
func Message(code int, message string, w http.ResponseWriter) {
	headers(w, code)
	response := map[string]any{"message": message}
	json.NewEncoder(w).Encode(response)
}

// usage: "status.Empty(w)"
func Empty(w http.ResponseWriter) {
	w.WriteHeader(http.StatusNoContent)
	json.NewEncoder(w)
}

// usage: "status.Object(http.statusOk, <someMap>, w)"
func Object(code int, content any, w http.ResponseWriter) {
	headers(w, code)
	json.NewEncoder(w).Encode(content)
}
