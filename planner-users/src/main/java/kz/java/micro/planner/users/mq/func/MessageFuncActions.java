package kz.java.micro.planner.users.mq.func;

import lombok.AllArgsConstructor;
import lombok.Getter;

import org.springframework.integration.support.MessageBuilder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Sinks;

@Service
@Getter
@AllArgsConstructor
public class MessageFuncActions {

    private MessageFunc messageFunc;

    public void setNewUserMessage(String userId) {
        messageFunc.getInnerBus().emitNext(MessageBuilder.withPayload(userId).build(), Sinks.EmitFailureHandler.FAIL_FAST);
        System.out.println("Request to emit: " + userId);
    }
}