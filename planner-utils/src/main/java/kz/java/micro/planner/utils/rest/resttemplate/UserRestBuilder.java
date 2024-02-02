package kz.java.micro.planner.utils.rest.resttemplate;

import kz.java.micro.planner.entity.User;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class UserRestBuilder {

    private final String URL = "http://localhost:8765/planner-users/user";

    public boolean userExists(String userId) {
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity request = new HttpEntity(userId);

        ResponseEntity<User> response = null;
        try {
            response = restTemplate.exchange(URL + "/id", HttpMethod.POST, request, User.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
