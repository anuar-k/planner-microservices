package kz.java.micro.planner.todo.controller;

import kz.java.micro.planner.entity.Priority;
import kz.java.micro.planner.todo.search.PrioritySearchValues;
import kz.java.micro.planner.todo.service.PriorityService;
import kz.java.micro.planner.utils.rest.resttemplate.UserRestBuilder;
import lombok.AllArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/priority")
@AllArgsConstructor
public class PriorityController {

    private final PriorityService priorityService;
    private final UserRestBuilder userRestBuilder;

    @PostMapping("/all")
    public List<Priority> getById(@RequestBody Long userId) {
        return priorityService.findAll(userId);
    }

    @PostMapping("/id")
    public ResponseEntity<Priority> findById(@RequestBody Long id) {
        Priority priority = null;

        try {
            priority = priorityService.findById(id);
        } catch (NoSuchElementException e) {
            e.printStackTrace();
            return new ResponseEntity(String.format("id: %s not found", id), HttpStatus.NOT_ACCEPTABLE);
        }

        return ResponseEntity.ok(priority);
    }

    @PostMapping("/add")
    public ResponseEntity<Priority> add(@RequestBody Priority priority) {

        if (priority.getId() != null && priority.getId() != 0) {
            return new ResponseEntity("id must be null", HttpStatus.NOT_ACCEPTABLE);
        }

        if (priority.getTitle() == null || priority.getTitle().trim().length() == 0) {
            return new ResponseEntity("missed param: title must be not null", HttpStatus.NOT_ACCEPTABLE);
        }

        if (priority.getColor() == null || priority.getColor().trim().length() == 0) {
            return new ResponseEntity("missed param: color must be not null", HttpStatus.NOT_ACCEPTABLE);
        }

        if (userRestBuilder.userExists(priority.getUserId())) {
            return ResponseEntity.ok(priorityService.add(priority));
        }
        return new ResponseEntity("user with id: " + priority.getUserId() + " not found", HttpStatus.NOT_ACCEPTABLE);
    }

    @PutMapping("/update")
    public ResponseEntity update(@RequestBody Priority priority) {
        if (priority.getId() == null || priority.getId() == 0) {
            return new ResponseEntity("id must be null", HttpStatus.NOT_ACCEPTABLE);
        }

        if (priority.getTitle() == null || priority.getTitle().trim().length() == 0) {
            return new ResponseEntity("missed param: title must be not null", HttpStatus.NOT_ACCEPTABLE);
        }

        if (priority.getColor() == null || priority.getColor().trim().length() == 0) {
            return new ResponseEntity("missed param: color must be not null", HttpStatus.NOT_ACCEPTABLE);
        }
        priorityService.update(priority);
        return new ResponseEntity(HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity delete(@PathVariable Long id) {
        try {
            priorityService.delete(id);
        } catch (EmptyResultDataAccessException e) {
            e.printStackTrace();
            return new ResponseEntity<>(String.format("priority with id: %s not found ", id), HttpStatus.NOT_ACCEPTABLE);
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping("/search")
    public ResponseEntity search(@RequestBody PrioritySearchValues prioritySearchValues) {
        if (prioritySearchValues.getUserId()== null || prioritySearchValues.getUserId() == 0) {
            return new ResponseEntity("email must be not null", HttpStatus.NOT_ACCEPTABLE);
        }

        List<Priority> list = priorityService.findByTitleAndId(prioritySearchValues.getTitle(), prioritySearchValues.getUserId());
        return ResponseEntity.ok(list);
    }
}