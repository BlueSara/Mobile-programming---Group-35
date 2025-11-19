package services

import "studygroup_api/database"

// GetAllDocs retrieves all documents from a Firestore collection.
// Returns a slice of maps containing the data from each document.
func GetAllDocs(collection string) ([]map[string]interface{}, error) {

	db, dbErr := database.DB()
	if dbErr != nil {
		return nil, dbErr
	}
	iter := db.Client.Collection(collection).Documents(db.Ctx)
	var registrations []map[string]interface{}

	for {
		doc, err := iter.Next()
		if err != nil {
			break // Stop iterating if no more documents
		}
		
		registrations = append(registrations, doc.Data()) // Add the document data to the result slice
	}
	// Returning all the documents that have been added to registrations.
	return registrations, nil
}