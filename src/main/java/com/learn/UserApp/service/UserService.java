package com.learn.UserApp.service;

import com.learn.UserApp.entity.User;

import java.util.List;

public interface UserService {

    //Create User
    User saveUser(User user);

    //Get All Users
    List<User> getAllUsers();

    //Get User by ID
    User getUserById(String userId);
}
