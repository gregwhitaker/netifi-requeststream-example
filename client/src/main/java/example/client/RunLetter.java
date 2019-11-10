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

import com.google.protobuf.Empty;
import com.netifi.broker.BrokerClient;
import com.netifi.broker.rsocket.BrokerSocket;
import example.service.letter.protobuf.LetterResponse;
import example.service.letter.protobuf.LetterServiceClient;
import example.service.number.protobuf.NumberResponse;
import example.service.number.protobuf.NumberServiceClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.concurrent.CountDownLatch;

public class RunLetter {
    private static final Logger LOG = LoggerFactory.getLogger(RunLettersStream.class);

    public static void main(String... args) throws Exception {
        // Build Netifi Broker Connection
        BrokerClient netifi =
                BrokerClient.tcp()
                        .group("example.client")                        // Group name of client
                        .destination("client1")                         // Name of this client instance
                        .accessKey(8833333111127534L)
                        .accessToken("Ih+hNsSdxLxAtHceTeEia2MGXSc=")
                        .host("localhost")                              // Netifi Broker Host
                        .port(8001)                                     // Netifi Broker Port
                        .disableSsl()
                        .build();

        // Connect to Netifi Platform
        BrokerSocket letterConn = netifi.groupServiceSocket("example.service.letter");
        BrokerSocket numberConn = netifi.groupServiceSocket("example.service.number");

        LetterServiceClient letterClient = new LetterServiceClient(letterConn);
        NumberServiceClient numberClient = new NumberServiceClient(numberConn);

        Mono<LetterResponse> letter = letterClient.getLetter(Empty.newBuilder().build());
        Mono<NumberResponse> number = numberClient.getNumber(Empty.newBuilder().build());

        CountDownLatch latch = new CountDownLatch(100);

        for (int i = 1; i <= 100; i++) {
            Flux.zip(letter, number)
                    .map(objects -> objects.getT1().getLetter() + objects.getT2().getNumber())
                    .doOnNext(s -> {
                        LOG.info(s);
                        latch.countDown();
                    })
                    .subscribe();
        }

        LOG.info("Done");

        latch.await();
    }
}
