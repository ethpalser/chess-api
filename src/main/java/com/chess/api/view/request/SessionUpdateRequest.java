package com.chess.api.view.request;

import com.chess.api.data.piece.Action;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class SessionUpdateRequest {

    private final Action action;

}
