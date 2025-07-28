package com.learn.UserApp.service.impl;

import com.learn.UserApp.entity.User;
import com.learn.UserApp.exception.ResourceNotFoundException;
import com.learn.UserApp.repo.UserRespository;
import com.learn.UserApp.service.UserService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Log4j2
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRespository repository;

    @Override
    public User saveUser(User user) {
        log.info("Saving User: {}", user);
        String uuid = UUID.randomUUID().toString();
        user.setId(uuid);
        return repository.save(user);
    }

    @Override
    public List<User> getAllUsers() {
        log.info("Fetching All Users");
        return repository.findAll();
    }

    @Override
    public User getUserById(String userId) {
        log.info("Fetching User with ID: {}", userId);
        return repository.findById(userId).orElseThrow(() ->
                        new ResourceNotFoundException("Records not found for ID: " + userId));
    }
}
