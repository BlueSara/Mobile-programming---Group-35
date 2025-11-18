package auth

import (
	"net/http"
	"studygroup_api/structs"
	"studygroup_api/utils"
)

func IsUserAuth(r *http.Request) (structs.Token, error) {
	tokenStr, headerErr := utils.GetAuthHeader(r)
	if headerErr != nil {
		return structs.Token{}, headerErr
	}

	token, tokenErr := utils.DecryptToken(tokenStr)
	if tokenErr != nil {
		return structs.Token{}, tokenErr
	}
	return token, nil
}
