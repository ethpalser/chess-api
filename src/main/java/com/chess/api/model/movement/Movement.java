package com.chess.api.model.movement;

import com.chess.api.model.Colour;
import com.chess.api.model.Coordinate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import lombok.NonNull;

@Getter
public class Movement {

    private final Map<Integer, Coordinate> coordinateBlueprint;
    private final MovementType pathType;
    private final boolean mirrorXAxis;
    private final boolean mirrorYAxis;

    public Movement() {
        this.coordinateBlueprint = new HashMap<>();
        this.pathType = MovementType.JUMP;
        this.mirrorXAxis = false;
        this.mirrorYAxis = false;
    }

    public Movement(MovementType pathType, boolean mirrorXAxis, boolean mirrorYAxis, List<Coordinate> blueprintCoordinates) {
        this.pathType = pathType;
        this.mirrorXAxis = mirrorXAxis;
        this.mirrorYAxis = mirrorYAxis;

        Map<Integer, Coordinate> coordinateHashMap = new HashMap<>();
        for (Coordinate c : blueprintCoordinates) {
            coordinateHashMap.put(c.hashCode(), c);
        }
        this.coordinateBlueprint = coordinateHashMap;
    }

    public Movement(MovementType pathType, boolean mirrorXAxis, boolean mirrorYAxis, Coordinate... blueprintCoordinates) {
        this.pathType = pathType;
        this.mirrorXAxis = mirrorXAxis;
        this.mirrorYAxis = mirrorYAxis;

        Map<Integer, Coordinate> coordinateHashMap = new HashMap<>();
        for (Coordinate c : blueprintCoordinates) {
            coordinateHashMap.put(c.hashCode(), c);
        }
        this.coordinateBlueprint = coordinateHashMap;
    }

    public Map<Integer, Coordinate> getCoordinates() {
        return coordinateBlueprint;
    }

    public Map<Integer, Coordinate> getCoordinates(@NonNull Coordinate offset, @NonNull Colour colour) {
        if (Colour.WHITE.equals(colour)) {
            return this.getWhiteCoordinates(offset.getX(), offset.getY());
        } else {
            return this.getBlackCoordinates(offset.getX(), offset.getY());
        }
    }

    private Map<Integer, Coordinate> getWhiteCoordinates(final int offsetX, final int offsetY) {
        Map<Integer, Coordinate> map = new HashMap<>();
        for (Coordinate coordinate : coordinateBlueprint.values()) {
            int baseOffsetX = coordinate.getX() + offsetX;
            int baseOffsetY = coordinate.getY() + offsetY;
            int mirrorOffsetX = offsetX - coordinate.getX();
            int mirrorOffsetY = offsetY - coordinate.getY();

            // Q1 Top Right
            if (baseOffsetX <= Coordinate.MAX_X && baseOffsetY <= Coordinate.MAX_Y) {
                Coordinate offsetCo = new Coordinate(baseOffsetX, baseOffsetY);
                map.put(offsetCo.hashCode(), offsetCo);
            }
            // Q2 Bottom Right
            if (this.mirrorXAxis && baseOffsetX <= Coordinate.MAX_X && mirrorOffsetY >= 0) {
                Coordinate mirrorX = new Coordinate(baseOffsetX, mirrorOffsetY);
                map.put(mirrorX.hashCode(), mirrorX);
            }
            // Q3 Bottom Left
            if (this.mirrorXAxis && this.mirrorYAxis && mirrorOffsetX >= 0 && mirrorOffsetY >= 0) {
                Coordinate mirrorXY = new Coordinate(mirrorOffsetX, mirrorOffsetY);
                map.put(mirrorXY.hashCode(), mirrorXY);
            }
            // Q4 Top Left
            if (this.mirrorYAxis && mirrorOffsetX >= 0 && baseOffsetY <= Coordinate.MAX_Y) {
                Coordinate mirrorY = new Coordinate(mirrorOffsetX, baseOffsetY);
                map.put(mirrorY.hashCode(), mirrorY);
            }
        }
        return map;
    }

    private Map<Integer, Coordinate> getBlackCoordinates(int offsetX, int offsetY) {
        Map<Integer, Coordinate> map = new HashMap<>();
        for (Coordinate coordinate : coordinateBlueprint.values()) {
            int baseOffsetX = coordinate.getX() + offsetX;
            int baseOffsetY = coordinate.getY() + offsetY;
            int mirrorOffsetX = offsetX - coordinate.getX();
            int mirrorOffsetY = offsetY - coordinate.getY();

            // Q1 Top Right
            if (this.mirrorXAxis && baseOffsetX <= Coordinate.MAX_X && baseOffsetY <= Coordinate.MAX_Y) {
                Coordinate offsetCo = new Coordinate(baseOffsetX, baseOffsetY);
                map.put(offsetCo.hashCode(), offsetCo);
            }
            // Q2 Bottom Right
            if (baseOffsetX <= Coordinate.MAX_X && mirrorOffsetY >= 0) {
                Coordinate mirrorX = new Coordinate(baseOffsetX, mirrorOffsetY);
                map.put(mirrorX.hashCode(), mirrorX);
            }
            // Q3 Bottom Left
            if (this.mirrorYAxis && mirrorOffsetX >= 0 && mirrorOffsetY >= 0) {
                Coordinate mirrorXY = new Coordinate(mirrorOffsetX, mirrorOffsetY);
                map.put(mirrorXY.hashCode(), mirrorXY);
            }
            // Q4 Top Left
            if (this.mirrorXAxis && this.mirrorYAxis && mirrorOffsetX >= 0 && mirrorOffsetY >= 0) {
                Coordinate mirrorXY = new Coordinate(baseOffsetX, mirrorOffsetY);
                map.put(mirrorXY.hashCode(), mirrorXY);
            }
        }
        return map;
    }

    public boolean validCoordinate(@NonNull Colour colour, @NonNull Coordinate source, @NonNull Coordinate destination) {
        Map<Integer, Coordinate> coordinates = this.getCoordinates(source, colour);
        return coordinates.get(destination.hashCode()) != null;
    }

    public boolean[][] drawCoordinates(@NonNull Colour colour) {
        return this.drawCoordinates(colour, new Coordinate(0, 0));
    }

    public boolean[][] drawCoordinates(@NonNull Colour colour, @NonNull Coordinate offset) {
        Map<Integer, Coordinate> coordinates = this.getCoordinates(offset, colour);
        boolean[][] boardMove = new boolean[Coordinate.MAX_X + 1][Coordinate.MAX_Y + 1];
        for (Coordinate c : coordinates.values()) {
            boardMove[c.getX()][c.getY()] = true;
        }
        return boardMove;
    }

    public String toString(@NonNull Colour colour, @NonNull Coordinate offset) {
        boolean[][] boardMove = this.drawCoordinates(colour, offset);
        StringBuilder sb = new StringBuilder();
        for (int y = boardMove[0].length - 1; y >= 0; y--) {
            for (int x = 0; x < boardMove.length; x++) {
                if (x == offset.getX() && y == offset.getY()) {
                    sb.append("| P ");
                } else if (boardMove[x][y]) {
                    sb.append("| o ");
                } else {
                    sb.append("| x ");
                }
                if (x == boardMove.length - 1) {
                    sb.append("|");
                }
            }
            sb.append("\n");
        }
        return sb.toString();
    }

}