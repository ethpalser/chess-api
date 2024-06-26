package com.chess.api.dao;

import com.chess.api.data.User;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface UserRepository extends MongoRepository<User, String> {

    @Query("{_id:'?0'}")
    User findUserById(ObjectId id);

    @Query("{username:'?0'}")
    User findUserByUsername(String username);

    @Query("{token:'?0'}")
    User findUserByToken(String token);


}
