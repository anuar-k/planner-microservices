package kz.java.micro.planner.utils.rest.webclient;

import kz.java.micro.planner.entity.User;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

@Component
public class UserWebClientBuilder {

    private final String baseUrlUser = "http://localhost:8765/planner-users/user/";
    private final String baseUrlData = "http://localhost:8765/planner-todo/data/";

    public boolean userExists(Long userId) {
        try {
            User user = WebClient.create(baseUrlUser)
                    .post()
                    .uri("id")
                    .bodyValue(userId)
                    .retrieve()
                    .bodyToFlux(User.class)
                    .blockFirst();

            if (user != null) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public Flux<Boolean> initData(Long userId) {
        Flux<Boolean> flux = WebClient.create(baseUrlData)
                .post()
                .uri("init")
                .bodyValue(userId)
                .retrieve()
                .bodyToFlux(Boolean.class);
        return flux;
    }
}