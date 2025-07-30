package com.learn.UserApp.service.external;

import com.learn.UserApp.model.Rating;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(value = "RatingApp")
public interface RatingService {


    @GetMapping("/rating/user/{userId}")
    List<Rating> getRatingByUserId(@PathVariable String userId);
}
