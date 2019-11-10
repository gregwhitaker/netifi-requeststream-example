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

import com.google.protobuf.Empty;
import example.service.number.protobuf.NumberRequest;
import example.service.number.protobuf.NumberResponse;
import example.service.number.protobuf.NumberService;
import io.netty.buffer.ByteBuf;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Random;

/**
 * Service that generates random numbers between 1 and 100.
 */
public class NumberServiceImpl implements NumberService {
    private static final Logger LOG = LoggerFactory.getLogger(NumberServiceImpl.class);
    private static final Random RAND = new Random(System.currentTimeMillis());

    @Override
    public Mono<NumberResponse> getNumber(Empty message, ByteBuf metadata) {
        return Mono.just(NumberResponse.newBuilder()
                .setNumber(RAND.nextInt(100 - 1 + 1) + 1)
                .build());
    }

    @Override
    public Flux<NumberResponse> getNumbers(NumberRequest message, ByteBuf metadata) {
        return Flux.range(1, message.getNumNumbers())
                .map(i -> RAND.nextInt(100 - 1 + 1) + 1)
                .map(i -> NumberResponse.newBuilder()
                        .setNumber(i)
                        .build())
                .delayElements(Duration.ofSeconds(1))
                .doOnNext(numberResponse -> LOG.info("Generated Number: {}", numberResponse.getNumber()));
    }
}
