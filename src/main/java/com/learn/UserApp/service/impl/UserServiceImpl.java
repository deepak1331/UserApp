package com.learn.UserApp.service.impl;

import com.learn.UserApp.entity.User;
import com.learn.UserApp.exception.ResourceNotFoundException;
import com.learn.UserApp.model.Hotel;
import com.learn.UserApp.model.Rating;
import com.learn.UserApp.model.dto.HotelDto;
import com.learn.UserApp.repo.UserRespository;
import com.learn.UserApp.service.UserService;
import com.learn.UserApp.service.external.HotelService;
import com.learn.UserApp.service.external.RatingService;
import lombok.extern.java.Log;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;

import static com.learn.UserApp.constant.AppConstants.*;

@Log4j2
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRespository repository;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private HotelService hotelService;

    @Autowired
    private RatingService ratingService;

    @Autowired
    private ModelMapper mapper;

    @Override
    public User saveUser(User user) {
        log.info("Saving User: {}", user);
        String uuid = UUID.randomUUID().toString();
        user.setId(uuid);
        return repository.save(user);
    }

    @Override
    public List<User> getAllUsersWithRating() {
        log.info("Fetching All Users");
        List<User> userList = repository.findAll();
        if (!userList.isEmpty()) {
            Map<String,User> userMap = userList.stream().collect(Collectors.toMap(User::getId, user -> user));

            //Using RestTemplate
            Rating[] ratings = restTemplate.getForObject(RATING_SERVICE, Rating[].class);
            log.info("Rating Service Response Size: {}", ratings != null ? ratings.length : 0);

            Hotel[] hotels = restTemplate.getForObject(HOTEL_SERVICE, Hotel[].class);
            log.info("Hotel Service Response Size : {}", hotels != null ? hotels.length : 0);
            Map<String, Hotel> hotelMap = Arrays.stream(hotels)
                    .collect(Collectors.toMap(Hotel::getId, hotel -> hotel));

            List<Rating> ratingList = Arrays.stream(ratings).map(rating -> {
                if (hotelMap.containsKey(rating.getHotelId())) {
                    rating.setHotel(mapper.map(hotelMap.get(rating.getHotelId()), HotelDto.class));
                }
                return rating;
            }).toList();

            //Map all rating into a map, with userID as key, and List of ratings as value.
            Map<String, List<Rating>> ratingMap = ratingList.stream()
                    .collect(Collectors.groupingBy(Rating::getUserId));
            // log.debug("Ratings after adding Hotel Info : {}", ratingList);

            userList =  userList.stream().map(user ->{
                if(ratingMap.containsKey(user.getId())){
                    user.setRatings(ratingMap.get(user.getId()));
                }
                return user;
            }).toList();
        }
        return userList;
    }

    @Override
    public List<User> getAllUsers() {
        return repository.findAll();
    }

    @Override
    public User getUserById(String userId) {
        log.info("Fetching User with ID: {}", userId);
        User user = repository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(NOT_FOUND + userId));
        log.debug("User: {}", user);
        if (user != null) {
            log.info("Calling Rating Service: {}", RATING_BY_USER + user.getId());

            //Using Feign Client
            List<Rating> ratingList = ratingService.getRatingByUserId(user.getId());
            if (!ratingList.isEmpty()) {
                ratingList = ratingList.stream().map(
                        rating -> {
                            Hotel hotel = hotelService.getHotelById(rating.getHotelId());
                            rating.setHotel(mapper.map(hotel, HotelDto.class));
                            return rating;
                        }).toList();
            }
            user.setRatings(ratingList);
        }
        return user;
    }
}