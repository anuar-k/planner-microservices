package kz.java.micro.planner.users.mq;

import lombok.AllArgsConstructor;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

@Component
@EnableBinding(TodoBinding.class)
@AllArgsConstructor
public class MessageProducer {

    private TodoBinding todoBinding;

    public void newUserAction(Long userId) {
        Message message = MessageBuilder.withPayload(userId).build();
        todoBinding.todoOutputChannel().send(message);
    }
}
