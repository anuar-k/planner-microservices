package kz.java.micro.planner.todo.mq.func;

import kz.java.micro.planner.todo.service.TestDataService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;

import java.util.function.Consumer;

@Configuration
@AllArgsConstructor
public class MessageFunc {

    private TestDataService testDataService;

    @Bean
    public Consumer<Message<Long>> newUserActionConsume(){
        return message -> testDataService.initTestData(message.getPayload());
    }
}