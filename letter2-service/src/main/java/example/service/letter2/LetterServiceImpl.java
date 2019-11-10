package example.service.letter2;

import example.service.letter.protobuf.LetterRequest;
import example.service.letter.protobuf.LetterResponse;
import example.service.letter.protobuf.LetterService;
import io.netty.buffer.ByteBuf;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;

public class LetterServiceImpl implements LetterService {
    private static final Logger LOG = LoggerFactory.getLogger(LetterServiceImpl.class);

    @Override
    public Flux<LetterResponse> getLetters(LetterRequest message, ByteBuf metadata) {
        return null;
    }
}
