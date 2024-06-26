package com.chess.api.data;

import com.chess.api.data.piece.Action;
import com.chess.api.data.piece.Piece;
import com.chess.api.view.response.PlayerView;
import com.chess.api.view.response.SessionView;
import java.util.Date;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@AllArgsConstructor
@Data
@Document("session")
public class Session {

    @Id
    private ObjectId id;

    private Date createdAt;

    private Date updatedAt;

    private String usernameWhite;

    private String usernameBlack;

    // Nullable
    @Setter(AccessLevel.NONE)
    private String usernameWinner;

    // MongoDB does not support Enums, so a string will be used with custom a getter & setter
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private String status;

    // List of (custom) chess notation action strings (i.e. Piece & Location to Piece & Location)
    private List<Action> moves;

    // List of Pieces on board and where they can move. Game will provide where they can move.
    private List<Piece> pieces;

    public Session() {
        this.id = new ObjectId();
        this.createdAt = new Date();
        this.updatedAt = new Date();
        this.usernameWhite = null;
        this.usernameBlack = null;
        this.usernameWinner = null;
        this.status = SessionStatus.PENDING.getValue();
        this.moves = List.of();
        this.pieces = List.of();
    }

    public Session(String usernameWhite, String usernameBlack, List<Piece> pieces) {
        this.id = new ObjectId();
        this.createdAt = new Date();
        this.updatedAt = new Date();
        this.usernameWhite = usernameWhite;
        this.usernameBlack = usernameBlack;
        this.usernameWinner = null;
        this.status = SessionStatus.PENDING.getValue();
        this.moves = List.of();
        this.pieces = pieces;
    }

    public static SessionView toView(Session session) {
        PlayerView white = new PlayerView(session.usernameWhite);
        PlayerView black = new PlayerView(session.usernameBlack);
        PlayerView winner = new PlayerView(session.usernameWinner);
        // Todo: Get Board from session details using Chess package
        return new SessionView(white, black, session.getMoves(), null, winner);
    }

    public SessionStatus getStatus() {
        SessionStatus ss = null;
        try {
            ss = SessionStatus.valueOf(this.status);
        } catch (IllegalArgumentException ex) {
            ss = SessionStatus.COMPLETED;
        }
        return ss;
    }

    public void setStatus(SessionStatus status) {
        this.status = status.getValue();
    }

    public void setWinner(String username) {
        if (!username.equals(this.usernameWhite) && !username.equals(this.usernameBlack)) {
            throw new IllegalArgumentException("Illegal Argument: username does not match a player's username");
        }
        this.usernameWinner = username;
    }

}
