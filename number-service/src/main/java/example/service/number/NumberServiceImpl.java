package example.service.number;

import example.service.number.protobuf.NumberRequest;
import example.service.number.protobuf.NumberResponse;
import example.service.number.protobuf.NumberService;
import io.netty.buffer.ByteBuf;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.Random;

/**
 * Service that generates random numbers between 1 and 100.
 */
public class NumberServiceImpl implements NumberService {
    private static final Logger LOG = LoggerFactory.getLogger(NumberServiceImpl.class);
    private static final Random RAND = new Random(System.currentTimeMillis());

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
