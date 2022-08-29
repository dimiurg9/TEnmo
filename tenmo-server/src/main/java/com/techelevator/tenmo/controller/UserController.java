package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.model.User;
import com.techelevator.tenmo.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@PreAuthorize("isAuthenticated()")
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/user/{id}", method = RequestMethod.GET)
    public User getUserByUserId(@PathVariable long id){
        return userService.getUserByUserId(id);
    }

    @RequestMapping(value = "/users/{id}", method = RequestMethod.GET)
    public List<User> listAllUsers(@PathVariable int id){return userService.getAllUsers(id);}


}
