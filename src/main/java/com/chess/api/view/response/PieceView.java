package com.chess.api.view.response;

import com.chess.api.data.piece.Point;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class PieceView {

    private final String code;
    private final String colour;
    private final Set<Point> allowedMoves;

}
