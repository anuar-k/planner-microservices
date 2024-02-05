package kz.java.micro.planner.users.controller;

//import kz.java.micro.planner.entity.User;

import kz.java.micro.planner.users.dto.UserDTO;
import kz.java.micro.planner.users.keycloak.KeycloakUtils;
import kz.java.micro.planner.users.mq.func.MessageFuncActions;
//import kz.java.micro.planner.utils.rest.webclient.UserWebClientBuilder;
import kz.java.micro.planner.users.search.UserSearchValues;
import lombok.RequiredArgsConstructor;
//import org.springframework.dao.DataIntegrityViolationException;
//import org.springframework.dao.EmptyResultDataAccessException;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Sort;
import org.keycloak.admin.client.CreatedResponseUtil;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
//import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/admin/user")
@RequiredArgsConstructor
public class AdminController {
    private final String ID_COLUMN = "id";
    private final static String TOPIC_NAME = "test-topic";
    private final static Integer CONFLICT = 409;
    private final static String DEFAULT_USER_ROLE = "user";

    private final KeycloakUtils keycloakUtils;

//    private final UserWebClientBuilder webClientBuilder;

//    private final MessageProducer messageProducer;

    private final MessageFuncActions messageFuncActions;

//    private final KafkaTemplate<String, Long> kafkaTemplate;

    @PostMapping("/add")
    public ResponseEntity<UserDTO> add(@RequestBody UserDTO userDTO) {
//        User newUser = null;
//        if (userDTO.getId() != null && userDTO.getId().length() != 0) {
//            return new ResponseEntity("id must be null", HttpStatus.NOT_ACCEPTABLE);
//        }

        if (userDTO.getEmail() == null || userDTO.getEmail().trim().length() == 0) {
            return new ResponseEntity("missed param :email", HttpStatus.NOT_ACCEPTABLE);
        }

        if (userDTO.getUsername() == null || userDTO.getUsername().trim().length() == 0) {
            return new ResponseEntity("missed param :username", HttpStatus.NOT_ACCEPTABLE);
        }

        if (userDTO.getPassword() == null || userDTO.getPassword().trim().length() == 0) {
            return new ResponseEntity("missed param :password", HttpStatus.NOT_ACCEPTABLE);
        }
//
//        try {
//            user = userService.save(user);
//        } catch (DataIntegrityViolationException e) {
//            e.printStackTrace();
//            return new ResponseEntity("user with email or username already exists", HttpStatus.NOT_ACCEPTABLE);
//        }


        Response createdResponse = keycloakUtils.createKeycloakUser(userDTO);

        if (createdResponse.getStatus() == CONFLICT) {
            return new ResponseEntity("user or email already exists " + userDTO.getEmail(), HttpStatus.CONFLICT);
        }

        // получаем его ID
        String userId = CreatedResponseUtil.getCreatedId(createdResponse);

        System.out.printf("User created with userId: %s%n", userId);

        if (userDTO != null) {
//            webClientBuilder.initData(user.getId()).subscribe(result -> System.out.println("user populated: " + result));
//            messageProducer.newUserAction(user.getId());
            messageFuncActions.setNewUserMessage(userId);
//            kafkaTemplate.send(TOPIC_NAME, user.getId());
        }


        List<String> defaultRoles = new ArrayList<>();
        defaultRoles.add(DEFAULT_USER_ROLE); // эта роль должна присутствовать в KC на уровне Realm

        keycloakUtils.addRoles(userId, defaultRoles);

        return ResponseEntity.status(createdResponse.getStatus()).build();
    }

    @PutMapping("/update")
    public ResponseEntity<UserDTO> update(@RequestBody UserDTO user) {

        if (user.getId().isBlank()) {
            return new ResponseEntity("missed param :id", HttpStatus.NOT_ACCEPTABLE);
        }
//
//        if (user.getEmail() == null || user.getEmail().trim().length() == 0) {
//            return new ResponseEntity("missed param :email", HttpStatus.NOT_ACCEPTABLE);
//        }
//
//        if (user.getUsername() == null || user.getUsername().trim().length() == 0) {
//            return new ResponseEntity("missed param :username", HttpStatus.NOT_ACCEPTABLE);
//        }

//        if (user.getPassword() == null || user.getPassword().trim().length() == 0) {
//            return new ResponseEntity("missed param :password", HttpStatus.NOT_ACCEPTABLE);
//        }
        keycloakUtils.updateKeycloakUser(user);
        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping("/deletebyid")
    public ResponseEntity deleteByUserId(@RequestBody String userId) {
//        try {
//            userService.deleteByUserId(userId);
//        } catch (EmptyResultDataAccessException e) {
//            e.printStackTrace();
//            return new ResponseEntity("userId = " + userId + " not found", HttpStatus.NOT_FOUND);
//        }
        keycloakUtils.deleteKeycloakUser(userId);
        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping("/deletebyemail")
    public ResponseEntity deleteByUserEmail(@RequestBody String email) {
//        try {
//            userService.deleteByUserEmail(email);
//        } catch (EmptyResultDataAccessException e) {
//            e.printStackTrace();
//            return new ResponseEntity("email = " + email + " not found", HttpStatus.NOT_FOUND);
//        }
//        keycloakUtils.deleteKeycloakUser(email);
        return new ResponseEntity(HttpStatus.OK);
    }


    @PostMapping("/id")
    public ResponseEntity<UserRepresentation> findById(@RequestBody String userId) {
//        UserDTO user = null;
//        try {
//            Optional<UserDTO> userOptional = userService.findById(userId);
//            if (userOptional.isPresent()) {
//                return ResponseEntity.ok(userOptional.get());
//            }
//        } catch (NoSuchElementException e) {
//            e.printStackTrace();
//        }

//        return new ResponseEntity("user by id: " + userId + " not found", HttpStatus.NO_CONTENT);
        return ResponseEntity.ok(keycloakUtils.findUserById(userId));
    }

//    @PostMapping("/email")
//    public ResponseEntity<UserDTO> findById(@RequestBody String email) {
//        User user = null;
//        try {
//            user = userService.findByEmail(email);
//        } catch (NoSuchElementException e) {
//            e.printStackTrace();
//            return new ResponseEntity("email by id: " + email + " not found", HttpStatus.NOT_ACCEPTABLE);
//        }
//        return ResponseEntity.ok(null);
//    }

    @PostMapping("/search")
    public ResponseEntity<List<UserRepresentation>> findByParam(@RequestBody String email) {
//    public ResponseEntity<Page<UserDTO>> findByParam(@RequestBody UserSearchValues userSearchValues) {
//
//        String email = userSearchValues.getEmail() != null ? userSearchValues.getEmail() : null;
//        String username = userSearchValues.getUsername() != null ? userSearchValues.getUsername() : null;
//
//        String sortColumn = userSearchValues.getSortColumn() != null ? userSearchValues.getSortColumn() : null;
//        String sortDirection = userSearchValues.getSortDirection() != null ? userSearchValues.getSortDirection() : null;
//
//        Integer pageNumber = userSearchValues.getPageNumber() != null ? userSearchValues.getPageNumber() : null;
//        Integer pageSize = userSearchValues.getPageSize() != null ? userSearchValues.getPageSize() : null;

//        Sort.Direction direction = sortDirection == null || sortDirection.trim().length() == 0 || sortDirection.equals("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
//
//        Sort sort = Sort.by(direction, sortColumn, ID_COLUMN);
//
//        PageRequest pageRequest = PageRequest.of(pageNumber - 1, pageSize, sort);
//
//        Page<User> result = userService.findByParams(email, username, pageRequest);
//
//        return ResponseEntity.ok(result);
        return ResponseEntity.ok(keycloakUtils.searchKeycloakUsers("email:" + email));
    }
}
