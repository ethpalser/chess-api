package com.chess.api.dao;

import com.chess.api.data.Session;
import java.util.List;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface SessionRepository extends MongoRepository<Session, String> {

    @Query("{_id:'?0'}")
    Session findSessionById(ObjectId id);

    @Aggregation(pipeline = {
            "{ $match: { $or: [{ usernameWhite:{'?0'} }, { usernameBlack:{'?1'} }], status:{ $ne:'Deleted'} } } }",
            "{ $sort: { createdAt:-1 } }",
            "{ $skip: '?2' }",
            "{ $limit: '?3' }"
    })
    List<Session> findAllByPlayerName(String usernameWhite, String usernameBlack, int page, int pageSize);

}
