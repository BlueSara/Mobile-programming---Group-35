package utils

import (
	"encoding/hex"
	"errors"
	"fmt"
	"os"
	"studygroup_api/models"
	"studygroup_api/structs"
	"time"

	"github.com/golang-jwt/jwt/v4"
	"golang.org/x/crypto/bcrypt"
)

func HashPwd(pwd string) (string, error) {
	bytes, bytesErr := bcrypt.GenerateFromPassword([]byte(pwd), bcrypt.DefaultCost)

	if bytesErr != nil {
		return "", bytesErr
	}

	return string(bytes), nil
}

func CheckPassword(cleanPwd, hashedPwd string) error {
	return bcrypt.CompareHashAndPassword([]byte(hashedPwd), []byte(cleanPwd))
}

func CreateToken(user models.User) (string, error) {
	tokenIssuer := os.Getenv("TOKEN_ISSUER")
	tokenHex := os.Getenv("TOKEN_HEX")
	secret, secretErr := hex.DecodeString(tokenHex)
	if secretErr != nil {
		return "", errors.New("an issue occured while signing in")
	}
	fmt.Print(user)

	claims := structs.Token{
		UserID:       user.UserID,
		University:   user.UniversityID,
		Subjects:     user.Subjects,
		StudyProgram: user.StudyProgram,
		Posts:        user.Posts,
		Responses:    user.Responses,
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
