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
package example.client;

import com.netifi.broker.BrokerClient;
import com.netifi.broker.rsocket.BrokerSocket;
import com.netifi.common.tags.Tags;
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
 * Client that calls the letter (version 2 only) and number services and aggregates their data.
 */
public class RunLetter2 {
    private static final Logger LOG = LoggerFactory.getLogger(RunLetter2.class);

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

        // Connect to Netifi Platform (Tag-based routing only works in Enterprise edition)
        BrokerSocket letterConn = netifi.groupServiceSocket("example.service.letter", Tags.of("version", "2"));
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
