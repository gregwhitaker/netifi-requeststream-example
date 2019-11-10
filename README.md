# netifi-requeststream-example
An example of the request-stream interaction model and aggregating streams, as well as tag-based routing, with [Netifi](https://www.netifi.com) and [RSocket](http://rsocket.io).

This example aggregates two different microservices, one that emits random letters, and one that emits random numbers into letter-number pairs.

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
Follow the steps below to run the example.

### Request-Stream
Follow the steps below to run the example that shows aggregating two request-streams:

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
        
    If successful, you will see letters and numbers aggregated together every second similar to the following:

        2019-11-10 14:46:57,494 INFO e.c.RunLettersStream [reactor-tcp-nio-4] a38
        2019-11-10 14:46:58,496 INFO e.c.RunLettersStream [reactor-tcp-nio-4] o75
        2019-11-10 14:46:59,501 INFO e.c.RunLettersStream [reactor-tcp-nio-4] x32
        2019-11-10 14:47:00,504 INFO e.c.RunLettersStream [reactor-tcp-nio-4] m84
        2019-11-10 14:47:01,508 INFO e.c.RunLettersStream [reactor-tcp-nio-4] u26
        2019-11-10 14:47:02,515 INFO e.c.RunLettersStream [reactor-tcp-nio-4] f41
        2019-11-10 14:47:03,519 INFO e.c.RunLettersStream [reactor-tcp-nio-4] z25

### Request-Response
Follow the steps below to run the example that shows aggregating request-response interactions where the letter data is load-balanced between `letter-service` and `letter2-service`:
    
1. In a new terminal, run the following command to start the `letter2-service`:

        ./gradlew :letter2-service:run
        
2. In a new terminal, run the following command to start the client and load-balance between both `letter-service` instances and the `number-service`:

        ./gradlew :client:runLetter
        
    If successful, you will see letters and numbers aggregated and note that the letters are both uppercase and lowercase similar to the following:

        2019-11-10 14:51:31,564 INFO e.c.RunLettersStream [reactor-tcp-nio-4] N51
        2019-11-10 14:51:31,564 INFO e.c.RunLettersStream [reactor-tcp-nio-4] I84
        2019-11-10 14:51:31,565 INFO e.c.RunLettersStream [reactor-tcp-nio-4] X27
        2019-11-10 14:51:31,566 INFO e.c.RunLettersStream [reactor-tcp-nio-4] P97
        2019-11-10 14:51:31,566 INFO e.c.RunLettersStream [reactor-tcp-nio-4] D67
        2019-11-10 14:51:31,567 INFO e.c.RunLettersStream [reactor-tcp-nio-4] l1
        2019-11-10 14:51:31,567 INFO e.c.RunLettersStream [reactor-tcp-nio-4] A16
        2019-11-10 14:51:31,567 INFO e.c.RunLettersStream [reactor-tcp-nio-4] l32
        2019-11-10 14:51:31,567 INFO e.c.RunLettersStream [reactor-tcp-nio-4] s19
        2019-11-10 14:51:31,568 INFO e.c.RunLettersStream [reactor-tcp-nio-4] Y89
        2019-11-10 14:51:31,568 INFO e.c.RunLettersStream [reactor-tcp-nio-4] O3
        2019-11-10 14:51:31,568 INFO e.c.RunLettersStream [reactor-tcp-nio-4] K88
        2019-11-10 14:51:31,569 INFO e.c.RunLettersStream [reactor-tcp-nio-4] o94
        2019-11-10 14:51:31,569 INFO e.c.RunLettersStream [reactor-tcp-nio-4] I17

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