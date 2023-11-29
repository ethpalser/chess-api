package com.chess.api.model.piece;

import com.chess.api.model.Colour;
import com.chess.api.model.Coordinate;
import com.chess.api.model.movement.ExtraMovement;
import com.chess.api.model.movement.Movement;
import com.chess.api.model.movement.MovementType;
import com.chess.api.model.movement.condition.Condition;
import com.chess.api.model.movement.condition.Location;
import com.chess.api.model.movement.condition.Property;
import com.chess.api.model.movement.condition.PropertyState;
import com.chess.api.model.movement.condition.Reference;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;

@Getter
public class Piece {

    private final PieceType type;
    private final Colour colour;
    private Coordinate position;
    private List<Movement> movementList;
    private int lastMoveDistance;

    @Getter(AccessLevel.NONE)
    private boolean hasMoved;

    public Piece() {
        this.type = PieceType.PAWN;
        this.colour = Colour.WHITE;
        this.position = new Coordinate(0, 0);
        this.hasMoved = false;
        this.lastMoveDistance = 0;
    }

    public Piece(PieceType pieceType, Colour colour, Coordinate coordinate) {
        this.type = pieceType;
        this.colour = colour;
        this.position = coordinate;
        this.hasMoved = false;
        this.movementList = List.of();
        this.lastMoveDistance = 0;
    }

    public Piece(PieceType pieceType, Colour colour, Coordinate coordinate, Movement... movements) {
        this(pieceType, colour, coordinate);
        this.movementList = List.of(movements);
        this.lastMoveDistance = 0;
    }

    public boolean getHasMoved() {
        return hasMoved;
    }

    public boolean verifyMove(@NonNull Coordinate destination) {
        for (Movement move : movementList) {
            boolean valid = move.isValidCoordinate(this.colour, this.position, destination);
            if (valid) {
                return true;
            }
        }
        return false;
    }

    public void performMove(@NonNull Coordinate destination) {
        this.lastMoveDistance = Math.max(Math.abs(destination.getX() - position.getX()),
                Math.abs(destination.getY() - position.getY()));
        this.position = destination;
        this.hasMoved = true;
    }

    @Override
    public String toString() {
        return this.type.getCode() + position.toString();
    }

    // region Static methods - Specific Chess Pieces

    public static Piece pawn(@NonNull Colour colour, @NonNull Coordinate coordinate) {
        Condition baseCondition = Condition.builder()
                .reference(new Reference(Location.AT_DESTINATION))
                .propertyState(PropertyState.DOES_NOT_EXIST)
                .build();
        Movement pawnBaseMove = new Movement(MovementType.ADVANCE, false, false, Coordinate.at(0, 1),
                List.of(baseCondition));
        // Conditional Moves
        Condition fastAdvanceCondition = Condition.builder()
                .reference(new Reference(Location.AT_START))
                .property(new Property<>("hasMoved"))
                .propertyState(PropertyState.FALSE)
                .build();
        Movement fastAdvance = new Movement(MovementType.ADVANCE, false, false, Coordinate.at(0, 2),
                List.of(baseCondition, fastAdvanceCondition));

        Condition captureCondition = Condition.builder()
                .reference(new Reference(Location.AT_DESTINATION))
                .property(new Property<>("colour"))
                .propertyState(PropertyState.OPPOSITE)
                .build();
        Movement capture = new Movement(MovementType.ADVANCE, false, true, Coordinate.at(1, 1),
                List.of(captureCondition));

        Condition enPassantCond1 = Condition.builder()
                .reference(new Reference(Location.LAST_MOVED))
                .property(new Property<>("type"))
                .propertyState(PropertyState.EQUAL)
                .expected(PieceType.PAWN)
                .build();
        Condition enPassantCond2 = Condition.builder()
                .reference(new Reference(Location.LAST_MOVED))
                .propertyState(PropertyState.EQUAL)
                .compare(new Reference(Location.BELOW_DESTINATION))
                .build();
        Condition enPassantCond3 = Condition.builder()
                .reference(new Reference(Location.LAST_MOVED))
                .property(new Property<>("lastMoveDistance"))
                .propertyState(PropertyState.EQUAL)
                .expected(2)
                .build();

        ExtraMovement extraMovement = new ExtraMovement(new Reference(Location.BELOW_DESTINATION), null, null, false);
        Movement enPassant = new Movement(MovementType.ADVANCE, false, true, Coordinate.at(1, 1),
                List.of(baseCondition, enPassantCond1, enPassantCond2, enPassantCond3), extraMovement, false);
        return new Piece(PieceType.PAWN, colour, coordinate, pawnBaseMove, fastAdvance, capture, enPassant);
    }

    public static Piece rook(@NonNull Colour colour, @NonNull Coordinate coordinate) {
        Movement rookBaseMoveV = new Movement(MovementType.ADVANCE, true, false, Coordinate.at(0, 1),
                Coordinate.at(0, Coordinate.MAX_Y));
        Movement rookBaseMoveH = new Movement(MovementType.ADVANCE, false, true, Coordinate.at(1, 0),
                Coordinate.at(Coordinate.MAX_X, 0));
        return new Piece(PieceType.ROOK, colour, coordinate, rookBaseMoveV, rookBaseMoveH);
    }

    public static Piece knight(@NonNull Colour colour, @NonNull Coordinate coordinate) {
        Movement knightBaseMove1 = new Movement(MovementType.JUMP, true, true, Coordinate.at(1, 2));
        Movement knightBaseMove2 = new Movement(MovementType.JUMP, true, true, Coordinate.at(2, 1));
        return new Piece(PieceType.KNIGHT, colour, coordinate, knightBaseMove1, knightBaseMove2);
    }

    public static Piece bishop(@NonNull Colour colour, @NonNull Coordinate coordinate) {
        Movement bishopBaseMove = new Movement(MovementType.ADVANCE, true, true, Coordinate.at(1, 1),
                Coordinate.at(Coordinate.MAX_X, Coordinate.MAX_Y));
        return new Piece(PieceType.BISHOP, colour, coordinate, bishopBaseMove);
    }

    public static Piece queen(@NonNull Colour colour, @NonNull Coordinate coordinate) {
        Movement queenBaseMoveV = new Movement(MovementType.ADVANCE, true, false, Coordinate.at(0, 1),
                Coordinate.at(0, Coordinate.MAX_Y));
        Movement queenBaseMoveH = new Movement(MovementType.ADVANCE, false, true, Coordinate.at(1, 0),
                Coordinate.at(Coordinate.MAX_X, 0));
        Movement queenBaseMoveD = new Movement(MovementType.ADVANCE, true, true, Coordinate.at(1, 1),
                Coordinate.at(Coordinate.MAX_X, Coordinate.MAX_Y));
        return new Piece(PieceType.QUEEN, colour, coordinate, queenBaseMoveV, queenBaseMoveH, queenBaseMoveD);
    }

    public static Piece king(@NonNull Colour colour, @NonNull Coordinate coordinate) {
        Movement kingBaseMoveV = new Movement(MovementType.ADVANCE, true, false, new Coordinate(0, 1));
        Movement kingBaseMoveH = new Movement(MovementType.ADVANCE, false, true, new Coordinate(1, 0));
        Movement kingBaseMoveD = new Movement(MovementType.ADVANCE, true, true, new Coordinate(1, 1));

        Condition castleCond1 = Condition.builder()
                .reference(new Reference(Location.AT_START))
                .property(new Property<>("hasMoved"))
                .propertyState(PropertyState.FALSE)
                .build();
        Condition castleKingSideCond2 = Condition.builder()
                .reference(new Reference(Location.AT_COORDINATE, Coordinate.at(7, 0)))
                .property(new Property<>("hasMoved"))
                .propertyState(PropertyState.FALSE)
                .build();
        Condition castleKingSideCond3 = Condition.builder()
                .reference(new Reference(Location.PATH_TO_COORDINATE, Coordinate.at(7, 0)))
                .propertyState(PropertyState.DOES_NOT_EXIST)
                .build();
        ExtraMovement kingSideRookMovement = new ExtraMovement(Coordinate.at(7, 0), Coordinate.at(5, 0));
        Movement castleKingSide = new Movement(MovementType.ADVANCE, false, false, Coordinate.at(2, 0),
                List.of(castleCond1, castleKingSideCond2, castleKingSideCond3), kingSideRookMovement, true);

        Condition castleQueenSideCond2 = Condition.builder()
                .reference(new Reference(Location.AT_COORDINATE, Coordinate.at(0, 0)))
                .property(new Property<>("hasMoved"))
                .propertyState(PropertyState.FALSE)
                .build();
        Condition castleQueenSideCond3 = Condition.builder()
                .reference(new Reference(Location.PATH_TO_COORDINATE, Coordinate.at(0, 0)))
                .propertyState(PropertyState.DOES_NOT_EXIST)
                .build();
        ExtraMovement queenSideRookMovement = new ExtraMovement(Coordinate.at(0, 0), Coordinate.at(3, 0));
        Movement castleQueenSide = new Movement(MovementType.ADVANCE, true, false, Coordinate.at(2, 0),
                List.of(castleCond1, castleQueenSideCond2, castleQueenSideCond3), queenSideRookMovement, true);
        return new Piece(PieceType.KING, colour, coordinate, kingBaseMoveV, kingBaseMoveH, kingBaseMoveD,
                castleKingSide, castleQueenSide);
    }

    // endregion
}
