package com.learn.UserApp.model.dto;

import jakarta.persistence.OneToOne;
import lombok.Data;
import org.springframework.web.bind.annotation.Mapping;

@Data
public class RatingDto {

    private String title;
    private int rating;
    private String feedback;
    private HotelDto hotel;
}
