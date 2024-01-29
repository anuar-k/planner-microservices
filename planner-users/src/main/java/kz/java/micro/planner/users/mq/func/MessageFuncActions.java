package kz.java.micro.planner.users.mq.func;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.stereotype.Service;

@Service
@Getter
@AllArgsConstructor
public class MessageFuncActions {

    private MessageFunc messageFunc;

    public void setNewUserMessage(Long userId) {
//        messageFunc.getInnerBus().emitNext(MessageBuilder.withPayload(userId).build(), Sinks.EmitFailureHandler.FAIL_FAST);
//        System.out.println("Request to emit: " + userId);
    }
}