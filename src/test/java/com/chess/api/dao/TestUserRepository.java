package com.chess.api.dao;

import com.chess.api.data.User;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

@DataMongoTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TestUserRepository {

    @Autowired
    private UserRepository userRepository;

    private ObjectId testUserId;

    User testUser() {
        User user = new User("Ethan", "SuperSecurePassword1!");
        user.setId(this.testUserId);
        return user;
    }

    @BeforeAll()
    void beforeAll() {
        this.testUserId = new ObjectId();
        // Only for DEV environment; todo: update to only be for DEV
        this.userRepository.deleteAll();
    }

    @Test
    void testCreateSession() {
        User saved = this.userRepository.save(testUser());
        // Check it exists
        User fetched = this.userRepository.findUserById(saved.getId());
        Assertions.assertEquals(saved, fetched);
    }

    @Test
    void testUpdateSession() {
        User fetched = this.userRepository.findUserById(this.testUserId);
        if (fetched == null) {
            fetched = this.userRepository.save(testUser());
        }
        fetched.setToken("todo");
        userRepository.save(fetched);
        Assertions.assertEquals(fetched, this.userRepository.findUserById(fetched.getId()));
    }

}
