package validation

import (
	"studygroup_api/structs"

	"github.com/go-playground/validator/v10"
)

func Signup(credentials *structs.UserSignup) error {
	validate := validator.New()
	err := validate.Struct(credentials)
	return err
}

func Signin(credentials *structs.UserSignin) error {
	validate := validator.New()
	err := validate.Struct(credentials)
	return err
}
