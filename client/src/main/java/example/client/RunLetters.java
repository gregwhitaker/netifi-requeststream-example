package example.client;

import com.netifi.broker.BrokerClient;
import com.netifi.broker.rsocket.BrokerSocket;
import example.service.letter.protobuf.LetterRequest;
import example.service.letter.protobuf.LetterResponse;
import example.service.letter.protobuf.LetterServiceClient;
import example.service.number.protobuf.NumberRequest;
import example.service.number.protobuf.NumberResponse;
import example.service.number.protobuf.NumberServiceClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;

import java.util.concurrent.CountDownLatch;

/**
 * Client that calls the letter (regardless of version) and number services and aggregates their data.
 */
public class RunLetters {
    private static final Logger LOG = LoggerFactory.getLogger(RunLetters.class);

    public static void main(String... args) throws Exception {
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

        // Connect to Netifi Platform
        BrokerSocket letterConn = netifi.groupServiceSocket("example.service.letter");
        BrokerSocket numberConn = netifi.groupServiceSocket("example.service.number");

        LetterServiceClient letterClient = new LetterServiceClient(letterConn);
        NumberServiceClient numberClient = new NumberServiceClient(numberConn);

        // Create Requests
        Flux<LetterResponse> letters = letterClient.getLetters(LetterRequest.newBuilder()
                .setNumLetters(100)
                .build());

        Flux<NumberResponse> numbers = numberClient.getNumbers(NumberRequest.newBuilder()
                .setNumNumbers(100)
                .build());

        CountDownLatch latch = new CountDownLatch(100);

        // Aggregate the Responses
        Flux.zip(letters, numbers)
                .map(objects -> objects.getT1().getLetter() + objects.getT2().getNumber())
                .doOnNext(s -> {
                    LOG.info(s);
                    latch.countDown();
                })
                .doOnComplete(() -> LOG.info("Done"))
                .subscribe();

        latch.await();
    }
}
