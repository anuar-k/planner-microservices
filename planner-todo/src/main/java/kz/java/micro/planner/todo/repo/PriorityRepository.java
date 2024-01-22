package kz.java.micro.planner.todo.repo;

import kz.java.micro.planner.entity.Priority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PriorityRepository extends JpaRepository<Priority, Long> {

    List<Priority> findByUserIdOrderByIdAsc(Long userId);

    @Query("SELECT c FROM Priority c WHERE " +
            "(c.title is null  or c.title='' " +
            "or lower(c.title) like lower(concat('%', :title, '%')))" +
            "and c.id = :id " +
            "order by c.title asc ")
    List<Priority> findByTitleAndEmail(String title, Long id);
}