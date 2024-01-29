package kz.java.micro.planner.users.mq.func;

import lombok.Getter;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class MessageFunc {

//    private Sinks.Many<Message<Long>> innerBus = Sinks.many().multicast().onBackpressureBuffer(Queues.SMALL_BUFFER_SIZE, false);

//    @Bean
//    public Supplier<Flux<Message<Long>>> newUserActionProduce() {
//        return () -> innerBus.asFlux();
//    }
}