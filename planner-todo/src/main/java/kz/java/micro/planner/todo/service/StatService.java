package kz.java.micro.planner.todo.service;

import kz.java.micro.planner.entity.Stat;
import kz.java.micro.planner.todo.repo.StatRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class StatService {

    private final StatRepository statRepository;

    public Stat findStat(Long userId) {
        return statRepository.findByUserId(userId).get();
    }
}