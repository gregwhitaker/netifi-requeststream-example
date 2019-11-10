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
package example.service.letter2;

import example.service.letter.protobuf.LetterRequest;
import example.service.letter.protobuf.LetterResponse;
import example.service.letter.protobuf.LetterService;
import io.netty.buffer.ByteBuf;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.Random;

/**
 * Service that implements the same interface as letter-service, but returns randomly generated
 * capital letters.
 */
public class LetterServiceImpl implements LetterService {
    private static final Logger LOG = LoggerFactory.getLogger(LetterServiceImpl.class);
    private static final Random RAND = new Random(System.currentTimeMillis());

    @Override
    public Flux<LetterResponse> getLetters(LetterRequest message, ByteBuf metadata) {
        return Flux.range(1, message.getNumLetters())
                .map(i -> String.valueOf((char) (RAND.nextInt(26) + 'a')).toUpperCase())
                .map(letter -> LetterResponse.newBuilder()
                        .setLetter(letter)
                        .build())
                .delayElements(Duration.ofSeconds(1))
                .doOnNext(letterResponse -> LOG.info("Generated Letter: {}", letterResponse.getLetter()));
    }
}
