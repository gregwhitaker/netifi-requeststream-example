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
package example.service.number;

import com.netifi.broker.BrokerClient;
import com.netifi.common.tags.Tags;
import example.service.number.protobuf.NumberServiceServer;

import java.util.Optional;
import java.util.UUID;

/**
 * Starts the number-service application.
 */
public class Main {

    public static void main(String... args) throws Exception {
        String serviceName = "numberservice-" + UUID.randomUUID().toString();

        // Build Netifi Broker Connection
        BrokerClient netifi =
                BrokerClient.tcp()
                        .group("example.service.number")                // Group name of service
                        .destination(serviceName)
                        .accessKey(9007199254740991L)
                        .accessToken("kTBDVtfRBO4tHOnZzSyY5ym2kfY=")
                        .host("localhost")                              // Netifi Broker Host
                        .port(8001)                                     // Netifi Broker Port
                        .disableSsl()
                        .build();

        // Add Service to Respond to Requests
        netifi.addService(new NumberServiceServer(new NumberServiceImpl(), Optional.empty(), Optional.empty()));

        // Connect to Netifi Platform
        netifi.groupServiceSocket("example.service.number", Tags.empty());

        // Keep the Service Running
        Thread.currentThread().join();
    }
}
