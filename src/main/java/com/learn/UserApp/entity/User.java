package com.learn.UserApp.entity;

import com.learn.UserApp.model.Rating;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "micro_user")
public class User {

    @Id
    @Column(name = "ID")
    private String id;

    @Column(name = "Username", length = 30)
    private String username;

    @Column(name = "Email", length = 50)
    private String email;

    private String about;

    @CreationTimestamp
    private Date createdOn;

    @UpdateTimestamp
    private Date updatedOn;

    @Transient
    private List<Rating> ratings;

}