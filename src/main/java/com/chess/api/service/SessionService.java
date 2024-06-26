package com.chess.api.service;

import com.chess.api.dao.SessionRepository;
import com.chess.api.data.Session;
import com.chess.api.data.SessionStatus;
import com.chess.api.view.request.SessionCreateRequest;
import com.chess.api.view.request.SessionUpdateRequest;
import java.awt.print.Pageable;
import java.util.Date;
import java.util.List;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SessionService {

    private final SessionRepository sessionRepository;

    @Autowired
    private SessionService(SessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    public List<Session> getAllSessions(String username, int page, int pageSize) {

        return this.sessionRepository.findAllByPlayerName(username, username, page, pageSize);
    }

    public Session createSession(SessionCreateRequest request) {
        // Todo: Use Chess package to initialize game board
        Session session = new Session(request.getWhiteUsername(), request.getBlackUsername(), List.of());
        return this.sessionRepository.save(session);
    }

    public Session updateSession(SessionUpdateRequest request) {
        // Todo: Use Chess package to validate and move piece on board
        return null;
    }

    public Session getSession(String id) {
        return this.sessionRepository.findSessionById(new ObjectId(id));
    }

    public void deleteSession(String id) {
        Session session = getSession(id);
        if (session == null) {
            return;
        }
        session.setStatus(SessionStatus.DELETED);
        session.setUpdatedAt(new Date());
        this.sessionRepository.save(session);
    }

}
