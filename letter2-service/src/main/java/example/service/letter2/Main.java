package example.service.letter2;

import com.netifi.broker.BrokerClient;
import com.netifi.common.tags.Tags;
import example.service.letter.protobuf.LetterServiceServer;

import java.util.Optional;
import java.util.UUID;

public class Main {

    public static void main(String... args) throws Exception {
        String serviceName = "letterservice-" + UUID.randomUUID().toString();

        // Build Netifi Broker Connection
        BrokerClient netifi =
                BrokerClient.tcp()
                        .group("example.service.letter")                // Group name of service
                        .destination(serviceName)
                        .accessKey(9007199254740991L)
                        .accessToken("kTBDVtfRBO4tHOnZzSyY5ym2kfY=")
                        .host("localhost")                              // Netifi Broker Host
                        .port(8001)                                     // Netifi Broker Port
                        .disableSsl()
                        .build();

        // Add Service to Respond to Requests
        netifi.addService(new LetterServiceServer(new LetterServiceImpl(), Optional.empty(), Optional.empty()));

        // Connect to Netifi Platform
        netifi.groupServiceSocket("example.service.letter", Tags.of("version", "2"));

        // Keep the Service Running
        Thread.currentThread().join();
    }
}
