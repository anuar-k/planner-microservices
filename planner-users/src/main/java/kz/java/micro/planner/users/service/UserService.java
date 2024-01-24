package kz.java.micro.planner.users.service;

import kz.java.micro.planner.entity.User;
import kz.java.micro.planner.users.repo.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@Transactional
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User save(User user) {
        return userRepository.save(user);
    }

    public User update(User user) {
        return userRepository.save(user);
    }

    public void deleteByUserEmail(String email) {
        userRepository.deleteByEmail(email);
    }

    public void deleteByUserId(Long userId) {
        userRepository.deleteById(userId);
    }

    public Optional<User> findById(Long userId) {
        return userRepository.findById(userId);
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public Page<User> findByParams(String email, String username, PageRequest pageRequest) {
        return userRepository.findByParams(email, username, pageRequest);
    }
}