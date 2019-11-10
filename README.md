# netifi-requeststream-example
An example of the request-stream interaction model and aggregating streams with [Netifi](https://www.netifi.com) and [RSocket](http://rsocket.io).

## Project Structure
This example contains the following projects:

* [client](client) - Client application that streams and aggregated data from the backend services.
* [letter-service](letter-service) - Service that generates random letters.
* [letter-service-idl](letter-service-idl) - Contract that defines the `letter-service` API.
* [letter2-service](letter2-service) - Version 2 of the `letter-service` that adds capital letters.
* [letter2-service-idl](letter-service-idl) - Contrat that defines the `letter2-service` API.
* [number-service](number-service) - Service that generates random numbers.
* [number-service-idl](number-service-idl) - Contract that defines the `number-service` API.

## Prerequisites
This example requires a running instance of the Netifi Broker.

Run the following command to download the [Netifi Community Edition Broker](https://www.netifi.com/netifi-ce) as a Docker container:

    docker pull netifi/broker:1.6.9

## Building the Example
Run the following command to build the example:

    ./gradlew clean build
    
## Running the Example
Follow the steps below to run the example:

TBD

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