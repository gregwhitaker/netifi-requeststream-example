# client
Client application that streams and aggregated data from the backend services.

## Running the Client
From the root project, run the following command to start the client and aggregate via the request-stream interaction model:

    ./gradlew :client:runLetters
    
If you wish to see it load-balance each letter request across both the `letter-service` and `letter2-service` then run the following command:

    ./gradlew :client:runLetter