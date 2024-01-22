package kz.java.micro.planner.todo.service;

import kz.java.micro.planner.entity.Priority;
import kz.java.micro.planner.todo.repo.PriorityRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
@AllArgsConstructor
public class PriorityService {

    private final PriorityRepository priorityRepository;

    public List<Priority> findAll(Long userId) {
        return priorityRepository.findByUserIdOrderByIdAsc(userId);
    }

    public Priority add(Priority priority) {
        return priorityRepository.save(priority);
    }

    public void update(Priority priority) {
        priorityRepository.save(priority);
    }

    public void delete(Long id) {
        priorityRepository.deleteById(id);
    }

    public List<Priority> findByTitleAndId(String title, Long userId) {
        return priorityRepository.findByTitleAndEmail(title, userId);
    }

    public Priority findById(Long id) {
        return priorityRepository.findById(id).get();
    }
}