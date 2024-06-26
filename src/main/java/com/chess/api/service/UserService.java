package com.chess.api.service;

import com.chess.api.dao.UserRepository;
import com.chess.api.data.User;
import com.chess.api.view.request.UserCreateRequest;
import com.chess.api.view.request.UserUpdateRequest;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    private UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDetails user = this.userRepository.findUserByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("username not found");
        }
        return user;
    }

    public List<User> fetchAllUsers() {
        return this.userRepository.findAll();
    }

    public User createUser(UserCreateRequest request) {
        User user = new User(request.getUsername(), request.getPassword());
        return this.userRepository.save(user);
    }

    public User getUser(String username) {
        return (User) this.loadUserByUsername(username);
    }

    public User updateUser(String originalUsername, UserUpdateRequest request) {
        User user = getUser(originalUsername);
        user.setUsername(request.getNewUsername());
        return this.userRepository.save(user);
    }
}
