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
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.learn.UserApp.constant.AppConstants.NOT_FOUND;
import static com.learn.UserApp.constant.AppConstants.RATING_BY_USER;

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
    public List<User> getAllUsers() {
        log.info("Fetching All Users");
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

            //Using RestTemplate
            //Rating[] ratings = restTemplate.getForObject(RATING_BY_USER + user.getId(), Rating[].class);
            //log.info("Rating Service Response Size: {}", ratings != null ? ratings.length : 0);
            //List<Rating> ratingList = Arrays.stream(ratings).toList();


            //Using Feign Client
            List<Rating> ratingList = ratingService.getRatingByUserId(user.getId());
            //List<RatingDto> ratingDtoList = new ArrayList<>();
                    //ratingList.stream().map(rating -> mapper.map(rating, RatingDto.class)).collect(Collectors.toList());
            if (!ratingList.isEmpty()) {
                ratingList.stream().map(
                        rating -> {
                            //Hotel hotel = restTemplate.getForObject(HOTEL_SERVICE + rating.getHotelId(), Hotel.class);
                            Hotel hotel = hotelService.getHotelById(rating.getHotelId());
                            //rating.setHotel(hotel);
                            rating.setHotel(mapper.map(hotel, HotelDto.class));

                            //Using Mapper to convert Entity into DTO class
                            /*TypeMap<Rating, RatingDto> propertyMapper = this.mapper.createTypeMap(Rating.class, RatingDto.class);
                            propertyMapper.addMappings(mapper -> mapper.skip(RatingDto::setHotel));
                            RatingDto ratingDto  = propertyMapper.map(rating);
                            ratingDto.setHotel(mapper.map(hotel, HotelDto.class));
                            ratingDtoList.add(ratingDto);*/

                            return rating;
                        }).collect(Collectors.toList());
            }
            //user.setRatings(ratingDtoList);
            user.setRatings(ratingList);
        }
        return user;
    }
}