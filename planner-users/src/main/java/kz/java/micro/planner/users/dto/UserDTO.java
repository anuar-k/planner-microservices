package kz.java.micro.planner.users.dto;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDTO {

    private String id;

    private String email;

    private String username;

    private String password;
}