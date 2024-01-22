package kz.java.micro.planner.todo.repo;

import kz.java.micro.planner.entity.Stat;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StatRepository extends CrudRepository<Stat, Long> {

    Optional<Stat> findByUserId(Long userId);
}