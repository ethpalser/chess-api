package com.chess.api.dao;

import com.chess.api.data.Session;
import com.chess.api.data.SessionStatus;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

@DataMongoTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TestSessionRepository {

    @Autowired
    private SessionRepository sessionRepository;

    private ObjectId testSessionId;

    Session testSession() {
        String white = "white";
        String black = "black";
        Session session = new Session();
        session.setId(this.testSessionId);
        session.setUsernameBlack(white);
        session.setUsernameWhite(black);
        return session;
    }

    @BeforeAll()
    void setup() {
        this.testSessionId = new ObjectId();
    }

    @AfterAll()
    void teardown() {
        this.sessionRepository.deleteById(this.testSessionId.toString());
    }

    @Test
    void testCreateSession() {
        Session saved = this.sessionRepository.save(testSession());
        // Check it exists
        Session fetched = this.sessionRepository.findSessionById(saved.getId());
        Assertions.assertEquals(saved, fetched);
    }

    @Test
    void testUpdateSession() {
        Session fetched = this.sessionRepository.findSessionById(this.testSessionId);
        if (fetched == null) {
            fetched = this.sessionRepository.save(testSession());
        }
        fetched.setStatus(SessionStatus.STARTED);
        sessionRepository.save(fetched);
        Assertions.assertEquals(fetched, this.sessionRepository.findSessionById(fetched.getId()));
    }

}
