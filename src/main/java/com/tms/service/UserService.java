package com.tms.service;

import com.tms.model.User;
import com.tms.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    
    private final UserRepository userRepository;
    
    @Transactional(readOnly = true)
    public User findByUsername(String username) {
        log.info("Finding user by username: {}", username);
        return userRepository.findByUsername(username).orElse(null);
    }
    
    @Transactional(readOnly = true)
    public User findById(Long id) {
        log.info("Finding user by id: {}", id);
        return userRepository.findById(id).orElse(null);
    }
}
