package kz.java.micro.planner.todo.controller;

import kz.java.micro.planner.entity.Category;
import kz.java.micro.planner.entity.User;
import kz.java.micro.planner.todo.feign.UserFeignClient;
import kz.java.micro.planner.todo.search.CategorySearchValues;
import kz.java.micro.planner.todo.service.CategoryService;
import kz.java.micro.planner.utils.rest.resttemplate.UserRestBuilder;
import kz.java.micro.planner.utils.rest.webclient.UserWebClientBuilder;
import lombok.AllArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/category")
@AllArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;
    private final UserRestBuilder userRestBuilder;
    private final UserWebClientBuilder userWebClientBuilder;
    private final UserFeignClient userFeignClient;

    @PostMapping("/all")
    public List<Category> getById(@RequestBody Long userId) {
        return categoryService.findAll(userId);
    }

    @PostMapping("/id")
    public ResponseEntity<Category> findById(@RequestBody Long id) {
        Category category = null;

        try {
            category = categoryService.findById(id);
        } catch (NoSuchElementException e) {
            e.printStackTrace();
            return new ResponseEntity(String.format("id: %s not found", id), HttpStatus.NOT_ACCEPTABLE);
        }

        return ResponseEntity.ok(category);
    }

    @PostMapping("/add")
    public ResponseEntity<Category> add(@RequestBody Category category) {

        if (category.getId() != null && category.getId() != 0) {
            return new ResponseEntity("id must be null", HttpStatus.NOT_ACCEPTABLE);
        }

        if (category.getTitle() == null || category.getTitle().trim().length() == 0) {
            return new ResponseEntity("missed param: title must be not null", HttpStatus.NOT_ACCEPTABLE);
        }

//        if (userRestBuilder.userExists(category.getUserId())) {
//            return ResponseEntity.ok(categoryService.add(category));
//        }

//        if (userWebClientBuilder.userExists(category.getUserId())) {
//            return ResponseEntity.ok(categoryService.add(category));
//        }

        ResponseEntity<User> result = userFeignClient.findUserById(category.getUserId());

        if (result == null) {
            return new ResponseEntity("user service is disabled", HttpStatus.NOT_FOUND);
        }

        if (result.getBody() != null) {
            return ResponseEntity.ok(categoryService.add(category));
        }

        return new ResponseEntity("user with id: " + category.getUserId() + " not found", HttpStatus.NOT_ACCEPTABLE);
    }

    @PutMapping("/update")
    public ResponseEntity update(@RequestBody Category category) {
        if (category.getId() == null || category.getId() == 0) {
            return new ResponseEntity("id must be null", HttpStatus.NOT_ACCEPTABLE);
        }

        if (category.getTitle() == null || category.getTitle().trim().length() == 0) {
            return new ResponseEntity("missed param: title must be not null", HttpStatus.NOT_ACCEPTABLE);
        }
        categoryService.update(category);
        return new ResponseEntity(HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity delete(@PathVariable Long id) {
        try {
            categoryService.delete(id);
        } catch (EmptyResultDataAccessException e) {
            e.printStackTrace();
            return new ResponseEntity<>(String.format("category with id: %s not found ", id), HttpStatus.NOT_ACCEPTABLE);
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping("/search")
    public ResponseEntity search(@RequestBody CategorySearchValues categorySearchValues) {
        if (categorySearchValues.getUserId() == null || categorySearchValues.getUserId() == 0) {
            return new ResponseEntity("email must be not null", HttpStatus.NOT_ACCEPTABLE);
        }

        List<Category> list = categoryService.findByTitleAndEmail(categorySearchValues.getTitle(), categorySearchValues.getUserId());
        return ResponseEntity.ok(list);
    }
}