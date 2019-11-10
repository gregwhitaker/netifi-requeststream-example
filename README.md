# netifi-requeststream-example
An example of the request-stream interaction model and aggregating streams, as well as tag-based routing, with [Netifi](https://www.netifi.com) and [RSocket](http://rsocket.io).

## Project Structure
This example contains the following projects:

* [client](client) - Client application that streams and aggregated data from the backend services.
* [letter-service-idl](letter-service-idl) - Contract that defines the `letter-service` and `letter2-service` APIs.
* [letter-service](letter-service) - Service that generates random letters.
* [letter2-service](letter2-service) - Version 2 of the `letter-service` that adds capital letters.
* [number-service-idl](number-service-idl) - Contract that defines the `number-service` API.
* [number-service](number-service) - Service that generates random numbers.

## Prerequisites
This example requires a running instance of the Netifi Broker.

Run the following command to download the [Netifi Community Edition Broker](https://www.netifi.com/netifi-ce) as a Docker container:

    docker pull netifi/broker:1.6.9

## Building the Example
Run the following command to build the example:

    ./gradlew clean build
    
## Running the Example
Follow the steps below to run the example:

1. Run the following command to start a local Netifi Broker:

        docker run -p 8001:8001 -p 8101:8101 -p 7001:7001 -p 6001:6001 \
        -e BROKER_SERVER_OPTS="'-Dnetifi.broker.admin.accessKey=8833333111127534' \
        '-Dnetifi.broker.admin.accessToken=Ih+hNsSdxLxAtHceTeEia2MGXSc=' \
        '-Dnetifi.authentication.0.accessKey=8833333111127534' \
        '-Dnetifi.authentication.0.accessToken=Ih+hNsSdxLxAtHceTeEia2MGXSc=' \
        '-Dnetifi.broker.ssl.disabled=true'" \
        netifi/broker:1.6.9
        
2. In a new terminal, run the following command to start the `number-service`:

        ./gradlew :number-service:run
        
3. In a new terminal, run the following command to start the `letter-service`:

        ./gradlew :letter-service:run
        
4. In a new terminal, run the following command to start the client and stream from both the `letter-service` and `number-service`:

        ./gradlew :client:runLetters

## License
Copyright 2019 Greg Whitaker

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.