package com.innscribe.journalApp.controller;

import com.innscribe.journalApp.entity.User;
import com.innscribe.journalApp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    // Get all users
    @GetMapping("/all")
    public ResponseEntity<List<User>> getAllUsers(){
        List<User> allUsers = userService.getAllUsers();
        if(allUsers != null && !allUsers.isEmpty()){
            return new ResponseEntity<>(allUsers, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    // Create a user
    @PostMapping
    public User createUser(@RequestBody User user){
        userService.saveUser(user);
        return user;
    }

    // Update a user
    @PutMapping("/{username}")
    public ResponseEntity<?> updateUser(@RequestBody User user, @PathVariable String username){
        User searchedUser = userService.findByUsername(username);
        if(searchedUser != null){
            searchedUser.setUsername(user.getUsername());
            searchedUser.setPassword(user.getPassword());
            userService.saveUser(searchedUser);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
