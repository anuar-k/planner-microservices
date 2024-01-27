package kz.java.micro.planner.users.mq;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

public interface TodoBinding {
    String OUTPUT_CHANNEL = "todoOutputChannel";

    @Output(OUTPUT_CHANNEL)
    MessageChannel todoOutputChannel();
}
