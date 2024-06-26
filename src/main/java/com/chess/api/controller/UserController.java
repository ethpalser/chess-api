package com.chess.api.controller;

import com.chess.api.data.User;
import com.chess.api.service.UserService;
import com.chess.api.view.request.UserCreateRequest;
import com.chess.api.view.request.UserUpdateRequest;
import com.chess.api.view.response.PlayerView;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<PlayerView> handleFetchAllUsers() {
        List<User> users = this.userService.fetchAllUsers();
        return users.stream().map(User::toView).toList();
    }

    @PostMapping
    public PlayerView handleCreateUser(UserCreateRequest request) {
        return User.toView(this.userService.createUser(request));
    }

    @GetMapping("/{username}")
    public PlayerView handleFetchUser(@PathVariable String username) {
        return User.toView(this.userService.getUser(username));
    }

    @PutMapping("/{username}")
    public PlayerView handleUpdateUser(@PathVariable String username, UserUpdateRequest request) {
        return User.toView(this.userService.updateUser(username, request));
    }

}
