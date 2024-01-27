package kz.java.micro.planner.users.controller;

import kz.java.micro.planner.entity.User;
import kz.java.micro.planner.users.mq.MessageProducer;
import kz.java.micro.planner.users.search.UserSearchValues;
import kz.java.micro.planner.users.service.UserService;
import kz.java.micro.planner.utils.rest.webclient.UserWebClientBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;
import java.util.Optional;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final String ID_COLUMN = "id";

    private final UserService userService;

    private final UserWebClientBuilder webClientBuilder;

    private final MessageProducer messageProducer;

    @PostMapping("/add")
    public ResponseEntity<User> add(@RequestBody User user) {
        User newUser = null;
        if (user.getId() != null && user.getId() != 0) {
            return new ResponseEntity("id must be null", HttpStatus.NOT_ACCEPTABLE);
        }

        if (user.getEmail() == null || user.getEmail().trim().length() == 0) {
            return new ResponseEntity("missed param :email", HttpStatus.NOT_ACCEPTABLE);
        }

        if (user.getUsername() == null || user.getUsername().trim().length() == 0) {
            return new ResponseEntity("missed param :username", HttpStatus.NOT_ACCEPTABLE);
        }

        if (user.getPassword() == null || user.getPassword().trim().length() == 0) {
            return new ResponseEntity("missed param :password", HttpStatus.NOT_ACCEPTABLE);
        }

        try {
            user = userService.save(user);
        } catch (DataIntegrityViolationException e) {
            e.printStackTrace();
            return new ResponseEntity("user with email or username already exists", HttpStatus.NOT_ACCEPTABLE);
        }

        if (user != null) {
//            webClientBuilder.initData(user.getId()).subscribe(result -> System.out.println("user populated: " + result));
            messageProducer.newUserAction(user.getId());
        }

        return ResponseEntity.ok(user);
    }

    @PutMapping("/update")
    public ResponseEntity<User> update(@RequestBody User user) {
        if (user.getId() == null || user.getId() == 0) {
            return new ResponseEntity("missed param :id", HttpStatus.NOT_ACCEPTABLE);
        }

        if (user.getEmail() == null || user.getEmail().trim().length() == 0) {
            return new ResponseEntity("missed param :email", HttpStatus.NOT_ACCEPTABLE);
        }

        if (user.getUsername() == null || user.getUsername().trim().length() == 0) {
            return new ResponseEntity("missed param :username", HttpStatus.NOT_ACCEPTABLE);
        }

//        if (user.getPassword() == null || user.getPassword().trim().length() == 0) {
//            return new ResponseEntity("missed param :password", HttpStatus.NOT_ACCEPTABLE);
//        }
        return ResponseEntity.ok(userService.update(user));
    }

    @PostMapping("/deletebyid")
    public ResponseEntity deleteByUserId(@RequestBody Long userId) {
        try {
            userService.deleteByUserId(userId);
        } catch (EmptyResultDataAccessException e) {
            e.printStackTrace();
            return new ResponseEntity("userId = " + userId + " not found", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping("/deletebyemail")
    public ResponseEntity deleteByUserEmail(@RequestBody String email) {
        try {
            userService.deleteByUserEmail(email);
        } catch (EmptyResultDataAccessException e) {
            e.printStackTrace();
            return new ResponseEntity("email = " + email + " not found", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity(HttpStatus.OK);
    }


    @PostMapping("/id")
    public ResponseEntity<User> findById(@RequestBody Long userId) {
        User user = null;
        try {
            Optional<User> userOptional = userService.findById(userId);
            if (userOptional.isPresent()) {
                return ResponseEntity.ok(userOptional.get());
            }
        } catch (NoSuchElementException e) {
            e.printStackTrace();
        }
        return new ResponseEntity("user by id: " + userId + " not found", HttpStatus.NO_CONTENT);
    }

    @PostMapping("/email")
    public ResponseEntity<User> findById(@RequestBody String email) {
        User user = null;
        try {
            user = userService.findByEmail(email);
        } catch (NoSuchElementException e) {
            e.printStackTrace();
            return new ResponseEntity("email by id: " + email + " not found", HttpStatus.NOT_ACCEPTABLE);
        }
        return ResponseEntity.ok(user);
    }

    @PostMapping("/search")
    public ResponseEntity<Page<User>> findByParam(@RequestBody UserSearchValues userSearchValues) {

        String email = userSearchValues.getEmail() != null ? userSearchValues.getEmail() : null;
        String username = userSearchValues.getUsername() != null ? userSearchValues.getUsername() : null;

        String sortColumn = userSearchValues.getSortColumn() != null ? userSearchValues.getSortColumn() : null;
        String sortDirection = userSearchValues.getSortDirection() != null ? userSearchValues.getSortDirection() : null;

        Integer pageNumber = userSearchValues.getPageNumber() != null ? userSearchValues.getPageNumber() : null;
        Integer pageSize = userSearchValues.getPageSize() != null ? userSearchValues.getPageSize() : null;

        Sort.Direction direction = sortDirection == null || sortDirection.trim().length() == 0 || sortDirection.equals("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;

        Sort sort = Sort.by(direction, sortColumn, ID_COLUMN);

        PageRequest pageRequest = PageRequest.of(pageNumber - 1, pageSize, sort);

        Page<User> result = userService.findByParams(email, username, pageRequest);

        return ResponseEntity.ok(result);
    }
}
