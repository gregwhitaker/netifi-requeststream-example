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

import com.google.protobuf.Empty;
import example.service.letter.protobuf.LetterRequest;
import example.service.letter.protobuf.LetterResponse;
import example.service.letter.protobuf.LetterService;
import io.netty.buffer.ByteBuf;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Random;

/**
 * Service that returns randomly generated lowercase letters.
 */
public class LetterServiceImpl implements LetterService {
    private static final Logger LOG = LoggerFactory.getLogger(LetterServiceImpl.class);
    private static final Random RAND = new Random(System.currentTimeMillis());

    @Override
    public Mono<LetterResponse> getLetter(Empty message, ByteBuf metadata) {
        return Mono.fromSupplier(() -> LetterResponse.newBuilder()
                .setLetter(String.valueOf((char) (RAND.nextInt(26) + 'a')).toLowerCase())
                .build());
    }

    @Override
    public Flux<LetterResponse> getLetters(LetterRequest message, ByteBuf metadata) {
        return Flux.range(1, message.getNumLetters())
                .map(i -> String.valueOf((char) (RAND.nextInt(26) + 'a')).toLowerCase())
                .map(letter -> LetterResponse.newBuilder()
                        .setLetter(letter)
                        .build())
                .delayElements(Duration.ofSeconds(1))
                .doOnNext(letterResponse -> LOG.info("Generated Letter: {}", letterResponse.getLetter()));
    }
}
