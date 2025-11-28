package utils

import (
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
