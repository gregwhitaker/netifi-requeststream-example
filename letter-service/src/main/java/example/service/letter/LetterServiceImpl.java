package example.service.letter;

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
 * Service that returns randomly generated lowercase letters.
 */
public class LetterServiceImpl implements LetterService {
    private static final Logger LOG = LoggerFactory.getLogger(LetterServiceImpl.class);
    private static final Random RAND = new Random(System.currentTimeMillis());

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
