package kz.java.micro.planner.todo.repo;

import kz.java.micro.planner.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    @Query("SELECT c FROM Category c WHERE c.id = :id")
    List<Category> findByUserIdOrderByTitleAsc(@Param("id") Long id);

    @Query("SELECT c FROM Category c WHERE " +
            "(c.title is null  or c.title='' " +
            "or lower(c.title) like lower(concat('%', :title, '%')))" +
            "and c.id = :id " +
            "order by c.title asc ")
    List<Category> findByTitleAndEmail(@Param("title") String title, @Param("id") Long id);
}