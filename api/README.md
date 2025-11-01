# General project structure information

1. main.go (sets up server etc)
2. main.go calls the router function(s) 
3. router function(s) calls the handlers 
4. Handler function(s) calls middleware function(s) and returns err if relevant
5. handler function calls controller function(s)
6. controller calls service function(s) 
7. service function(s) utilizes utils function(s) 
8. controller function returns response from api

## Other info

### Package status
Package "status" is used to simplify the process of returning status-code + content to front-end

### package database
package database will contain functions to establish connections with the database to simplify how it is done