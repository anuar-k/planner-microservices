package kz.java.micro.planner.todo.mq;

import kz.java.micro.planner.todo.service.TestDataService;
import lombok.AllArgsConstructor;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
@EnableBinding(TodoBinding.class)
public class MessageConsumer {

    TestDataService testDataService;

    @StreamListener(target = TodoBinding.INPUT_CHANNEL)
    public void initUserData(Long userId) throws Exception{
//        testDataService.initTestData(userId);
         throw new Exception("test DLQ");
    }
}
