package example.client;

import com.netifi.broker.BrokerClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Client that calls the letter and number services and aggregates their data.
 */
public class Main {
    private static final Logger LOG = LoggerFactory.getLogger(Main.class);

    public static void main(String... args) {
        // Build Netifi Broker Connection
        BrokerClient netifi =
                BrokerClient.tcp()
                        .group("example.client")                        // Group name of client
                        .destination("client1")                         // Name of this client instance
                        .accessKey(9007199254740991L)
                        .accessToken("kTBDVtfRBO4tHOnZzSyY5ym2kfY=")
                        .host("localhost")                              // Netifi Broker Host
                        .port(8001)                                     // Netifi Broker Port
                        .disableSsl()
                        .build();

        
    }
}
