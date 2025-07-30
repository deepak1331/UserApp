package com.learn.UserApp.model;

import com.learn.UserApp.model.dto.HotelDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Rating {

    private String ratingId;
    private String userId;
    private String hotelId;
    private String title;
    private int rating;
    private String feedback;
    private Date createdOn;
    private Date updatedOn;
    //private Hotel hotel;
    private HotelDto hotel;
}
