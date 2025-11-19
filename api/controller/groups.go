package controller

import (
	
	"encoding/json"
//	"fmt"
	"net/http"
	"studygroup_api/response"
	"studygroup_api/services"
	"studygroup_api/structs"
//	"studygroup_api/utils"
)

func GetPosts(r *http.Request, w http.ResponseWriter, token *structs.Token) {

	unparsedGroups, getErr := services.GetAllDocs("groups")
	if getErr != nil {
		response.Error(http.StatusNotFound, "The referenced subject was not found", w)
		return
	}

	// Convert the retrieved document to json ...
	temp, err2 := json.Marshal(unparsedGroups)
	if err2 != nil {
		http.Error(w, "Something went wrong when parsing document", http.StatusInternalServerError)
		return
	}

	// ... and convert it back into a formatted struct
	var groups []struct {Participants []string `json:"participants"`}
	json.Unmarshal(temp, &groups)
	
	// real data from database for testing without tokens
	//var dummy string = "dFECXP03rIxMvdortonf"

	// output data
	var out []map[string]interface{}
	
	// for all indices and group in the list of groups (all group documents from firebase) ...
	for i, group := range groups {

		// ... loop over all participants in the group ...
		for _, id := range group.Participants {

			// ... and add the group to the output object if the participant is the current user
			if id == token.UserID {//dummy {
				out = append(out, unparsedGroups[i])
				break // break out of inner for loop, we do not care about other participants
			}
		}
	}

	// return error if no groups
	if len(out) == 0 {
		http.Error(w, "no groups found", http.StatusNotFound)
		return
	}
	response.Object(http.StatusOK, out, w)
}