package kz.java.micro.planner.todo.controller;

import kz.java.micro.planner.entity.Category;
import kz.java.micro.planner.entity.Priority;
import kz.java.micro.planner.entity.Task;
import kz.java.micro.planner.todo.service.CategoryService;
import kz.java.micro.planner.todo.service.PriorityService;
import kz.java.micro.planner.todo.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Calendar;
import java.util.Date;

@RestController
@RequiredArgsConstructor
@RequestMapping("/data")
public class TestDataController {

    private final TaskService taskService;
    private final PriorityService priorityService;
    private final CategoryService categoryService;


    @PostMapping("/init")
    public ResponseEntity<Boolean> init(@RequestBody String userId) {
        Priority priority1 = Priority.builder()
                .title("Важный")
                .color("#fff")
                .userId(userId)
                .build();

        Priority priority2 = Priority.builder()
                .title("Неважный")
                .color("#ffe")
                .userId(userId)
                .build();

        priorityService.add(priority1);
        priorityService.add(priority2);

        Category category1 = Category.builder()
                .title("Работа")
                .userId(userId)
                .build();

        Category category2 = Category.builder()
                .title("Семья")
                .userId(userId)
                .build();

        categoryService.add(category1);
        categoryService.add(category2);

        //завтра
        Date tomorrow = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(tomorrow);
        c.add(Calendar.DATE, 1);
        tomorrow = c.getTime();

        //неделя
        Date oneWeek = new Date();
        Calendar c2 = Calendar.getInstance();
        c2.setTime(oneWeek);
        c.add(Calendar.DATE, 7);
        oneWeek = c.getTime();

        Task task1 = Task.builder()
                .title("Покушать")
                .category(category1)
                .priority(priority1)
                .completed(true)
                .taskDate(tomorrow)
                .userId(userId)
                .build();

        Task task2 = Task.builder()
                .title("Поспать")
                .category(category2)
                .priority(priority2)
                .completed(false)
                .taskDate(oneWeek)
                .userId(userId)
                .build();

        taskService.add(task1);
        taskService.add(task2);

        return ResponseEntity.ok(true);
    }
}