package com.learn.UserApp.service.external;


import com.learn.UserApp.model.Hotel;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "HotelApp")
public interface HotelService {

    @GetMapping("/hotel/{hotelId}")
    Hotel getHotelById(@PathVariable String hotelId);
}