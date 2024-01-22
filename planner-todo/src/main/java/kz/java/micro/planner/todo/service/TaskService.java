package kz.java.micro.planner.todo.service;

import kz.java.micro.planner.entity.Task;
import kz.java.micro.planner.todo.repo.TaskRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@AllArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;

    public List<Task> findAll(Long userId) {
       return taskRepository.findByUserIdOrderByTitleAsc(userId);
    }

    public Task add(Task task) {
        return taskRepository.save(task);
    }

    public Task update(Task task) {
        return taskRepository.save(task);
    }

    public void deleteById(Long id) {
        taskRepository.deleteById(id);
    }

    public Page<Task> findByParams(String title, Boolean competed, Long priorityId, Long categoryId, Long userId, Date dateFrom, Date dateTo, PageRequest paging) {
        return taskRepository.findByParams(title, competed, priorityId, categoryId, userId, dateFrom, dateTo, paging);
    }

    public Task findById(Long id) {
        return taskRepository.findById(id).get();
    }
}