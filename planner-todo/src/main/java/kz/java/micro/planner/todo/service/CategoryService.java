package kz.java.micro.planner.todo.service;

import kz.java.micro.planner.entity.Category;
import kz.java.micro.planner.todo.repo.CategoryRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
@AllArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public List<Category> findAll(Long userId) {
        return categoryRepository.findByUserIdOrderByTitleAsc(userId);
    }

    public Category add(Category category) {
        return categoryRepository.save(category);
    }

    public void update(Category category) {
        categoryRepository.save(category);
    }

    public void delete(Long id) {
        categoryRepository.deleteById(id);
    }

    public List<Category> findByTitleAndEmail(String title, Long userId) {
        return categoryRepository.findByTitleAndEmail(title, userId);
    }

    public Category findById(Long id) {
        return categoryRepository.findById(id).get();
    }
}