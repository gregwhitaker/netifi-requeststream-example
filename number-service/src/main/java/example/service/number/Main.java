package example.service.number;

import com.netifi.broker.BrokerClient;
import com.netifi.common.tags.Tags;
import example.service.number.protobuf.NumberServiceServer;

import java.util.Optional;
import java.util.UUID;

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
