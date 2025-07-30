package com.learn.UserApp.service.impl;

import com.learn.UserApp.entity.User;
import com.learn.UserApp.exception.ResourceNotFoundException;
import com.learn.UserApp.model.Hotel;
import com.learn.UserApp.model.Rating;
import com.learn.UserApp.repo.UserRespository;
import com.learn.UserApp.service.UserService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.learn.UserApp.constant.AppConstants.*;

@Log4j2
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRespository repository;

    @Autowired
    private RestTemplate restTemplate;

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
        User user = repository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(NOT_FOUND + userId));
        log.info("User: {}", user);
        if (user != null) {
            log.info("Calling Rating Service: {}", RATING_BY_USER + user.getId());
            Rating[] ratings = restTemplate.getForObject(RATING_BY_USER + user.getId(), Rating[].class);
            log.info("Rating Service Response Size: {}", ratings != null ? ratings.length : 0);
            List<Rating> ratingList = Arrays.stream(ratings).toList();

            if (!ratingList.isEmpty()) {
                ratingList.stream().map(
                        rating -> {
                            Hotel hotel = restTemplate.getForObject(HOTEL_SERVICE + rating.getHotelId(), Hotel.class);
                            rating.setHotel(hotel);
                            return rating;
                        }).collect(Collectors.toList());
            }
            user.setRatings(ratingList);
        }
        return user;
    }
}