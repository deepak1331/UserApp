package com.learn.UserApp.controller;

import com.learn.UserApp.entity.User;
import com.learn.UserApp.service.UserService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@Log4j2
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService service;

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<User> createUser(@RequestBody User user) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.saveUser(user));
    }

    @RequestMapping(path = "/{userId}", method = RequestMethod.GET)
    @CircuitBreaker(name = "ratingHotelBreaker", fallbackMethod = "ratingHotelFallback")
    public ResponseEntity<User> getUser(@PathVariable String userId) {
        return ResponseEntity.ok(service.getUserById(userId));
    }

    public ResponseEntity<User> ratingHotelFallback(String userId, Exception exception) {
        log.info("User - > Rating Fallback is called because service is down: {}",
                exception.getMessage());
        User user = User.builder().username("Dummy User")
                .id("0000-0000-0000-0000")
                .about("This is a dummy response, since rating service is down")
                .email("dummy@gmail.com").build();
        return ResponseEntity.ok(user);

    }


    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<User>> getUsers() {
        return ResponseEntity.ok(service.getAllUsers());
    }
}
