package com.chess.api.dao;

import com.chess.api.data.Session;
import java.util.List;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface SessionRepository extends MongoRepository<Session, Long> {

    @Query("{_id:'?0'}")
    Session findSessionById(ObjectId id);

    @Query("{$or: [{usernameWhite:{'?0'}}, {usernameBlack:{'?1'}}], status:{$ne:'Deleted'} }")
    List<Session> findAllByPlayerName(String usernameWhite, String usernameBlack);

}
