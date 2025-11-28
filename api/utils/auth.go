package utils

import (
	"encoding/hex"
	"errors"
	"net/http"
	"os"
	"strings"
	"studygroup_api/models"
	"studygroup_api/structs"
	"time"

	"github.com/golang-jwt/jwt/v4"
)

func CreateToken(user models.User) (string, error) {
	tokenIssuer := os.Getenv("TOKEN_ISSUER")
	tokenHex := os.Getenv("TOKEN_HEX")
	secret, secretErr := hex.DecodeString(tokenHex)
	if secretErr != nil {
		return "", errors.New("an issue occured while signing in")
	}

	claims := structs.Token{
		UserID:       user.UserID,
		University:   user.UniversityID,
		Subjects:     user.Subjects,
		StudyProgram: user.StudyProgram,
		RegisteredClaims: jwt.RegisteredClaims{
			ExpiresAt: jwt.NewNumericDate(time.Now().AddDate(0, 1, 0)),
			Issuer:    tokenIssuer,
			Subject:   user.UserID,
			IssuedAt:  jwt.NewNumericDate(time.Now()),
		},
	}

	token := jwt.NewWithClaims(jwt.SigningMethodHS256, claims)
	signedToken, signedErr := token.SignedString(secret)
	if signedErr != nil {
		return "", errors.New("an issue occured while generating a token for your session")
	}

	return signedToken, nil
}

func GetAuthHeader(r *http.Request) (string, error) {
	header := r.Header.Get("Authorization")
	if header == "" || !strings.HasPrefix(header, "Bearer ") {
		return "", errors.New("unauthorized access")
	}

	token := strings.TrimPrefix(header, "Bearer ")
	if token == "" {
		return "", errors.New("unauthorized access")
	}

	return token, nil
}

func DecryptToken(tokenStr string) (structs.Token, error) {
	tokenClaims := &structs.Token{}
	issuer := os.Getenv("TOKEN_ISSUER")
	tokenHex := os.Getenv("TOKEN_HEX")

	secret, _ := hex.DecodeString(tokenHex)

	token, tokenErr := jwt.ParseWithClaims(tokenStr, tokenClaims, func(t *jwt.Token) (any, error) {
		if _, ok := t.Method.(*jwt.SigningMethodHMAC); !ok {
			return nil, errors.New("invalid token")
		}
		return secret, nil
	})

	if tokenErr != nil {
		return structs.Token{}, errors.New("invalid token")
	}

	if !token.Valid || tokenClaims.Issuer != issuer {
		return structs.Token{}, errors.New("invalid token")
	}

	return *tokenClaims, nil

}
