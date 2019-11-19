/**
 * Copyright 2019 Greg Whitaker
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package example.service.letter;

import com.netifi.broker.BrokerClient;
import com.netifi.common.tags.Tags;
import example.service.letter.protobuf.LetterServiceServer;

import java.util.Optional;
import java.util.UUID;

/**
 * Starts the letter-service application.
 */
public class Main {

    public static void main(String... args) throws Exception {
        String serviceName = "letterservice-" + UUID.randomUUID().toString();

        // Build Netifi Broker Connection
        BrokerClient netifi =
                BrokerClient.tcp()
                        .group("example.service.letter")                // Group name of service
                        .destination(serviceName)
                        .accessKey(8919305387847438L)
                        .accessToken("SaSnfcqhUGbjRqE0lxCkFybieoY=")
                        .host("localhost")                              // Netifi Broker Host
                        .port(8001)                                     // Netifi Broker Port
                        .disableSsl()
                        .build();

        // Add Service to Respond to Requests
        netifi.addService(new LetterServiceServer(new LetterServiceImpl(), Optional.empty(), Optional.empty()));

        // Connect to Netifi Platform
        netifi.groupServiceSocket("example.service.letter", Tags.of("version", "1"));

        // Keep the Service Running
        Thread.currentThread().join();
    }
}
