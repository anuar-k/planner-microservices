package kz.java.micro.planner.users.search;

import lombok.Data;

@Data
public class UserSearchValues {

    private String email;
    private String username;

    private Integer pageNumber;
    private Integer pageSize;

    private String sortColumn;
    private String sortDirection;
}