package validation

import (
	"studygroup_api/structs"

	"github.com/go-playground/validator/v10"
)

func Post(credentials *structs.Post) error {
	validate := validator.New()
	err := validate.Struct(credentials)
	return err
}
