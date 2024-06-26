package com.chess.api.controller;

import com.chess.api.data.Session;
import com.chess.api.service.SessionService;
import com.chess.api.view.request.SessionCreateRequest;
import com.chess.api.view.request.SessionUpdateRequest;
import com.chess.api.view.response.SessionView;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sessions")
public class SessionController {

    private final SessionService sessionService;

    @Autowired
    public SessionController(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @GetMapping
    public List<SessionView> handleGetSessions(
            @RequestParam int page,
            @RequestParam int size,
            @RequestParam String username
    ) {
        List<Session> list = this.sessionService.getAllSessions(username, page, size);
        return list.stream().map(Session::toView).toList();
    }

    @PostMapping
    public SessionView handleCreateSession(SessionCreateRequest request) {
        return Session.toView(this.sessionService.createSession(request));
    }

    /**
     * Update the session by performing an action on the board. A session can only be updated
     * while it is currently in-progress, by authorized players, and by the player allowed to act,
     * and once an action causes checkmate or stalemate this session can no longer be updated.
     *
     * @return Information of a session: Players, Board, and history of actions.
     */
    @PutMapping
    public SessionView handleUpdateSession(SessionUpdateRequest request) {
        return Session.toView(this.sessionService.updateSession(request));
    }

    @GetMapping("/{id}")
    public SessionView handleFetchSession(@PathVariable String id) {
        return Session.toView(this.sessionService.getSession(id));
    }

    @DeleteMapping("/{id}")
    public void handleSoftDeleteSession(@PathVariable String id) {
        this.sessionService.deleteSession(id);
    }

}
