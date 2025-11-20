package database

import (
	"context"
	"errors"
	"os"
	"fmt"
	"sync"

	"cloud.google.com/go/firestore"
	firebase "firebase.google.com/go/v4"
	"google.golang.org/api/option"
	"google.golang.org/api/iterator"
)

type Firestore struct {
	Client *firestore.Client
	Ctx    context.Context
}

var (
	instance *Firestore
	once     sync.Once
)

//connects to database, to connect to db, run
/*

	db, dbErr := database.DB()
	if dbErr != nil {
		//handle error here
	}

*/
func DB() (*Firestore, error) {
	var initErr error

	once.Do(func() {

		credsPath := os.Getenv("FIREBASE_CREDENTIALS")
		fmt.Println(credsPath)
		ctx := context.Background()
		opt := option.WithCredentialsFile(credsPath)
		firebaseApp, firebaseErr := firebase.NewApp(ctx, nil, opt)

		if firebaseErr != nil {
			initErr = errors.New("failed to load firebase app")
			return
		}

		client, clientErr := firebaseApp.Firestore(ctx)
		if clientErr != nil {
			initErr = errors.New("failed to create firestore client")
			return
		}
		// Test Firestore connection by listing any collection
        iter := client.Collections(ctx)
        _, err := iter.Next()
        if err != nil && err != iterator.Done {
            initErr = fmt.Errorf("firestore test query failed: %v", err)
            return
        }

        fmt.Println("Connected to Firestore")
		instance = &Firestore{Client: client, Ctx: ctx}
	})

	if initErr != nil {
		return nil, initErr
	}

	return instance, nil

}
