package com.chess.api.view.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class SessionCreateRequest {

    private final String whiteUsername;
    private final String blackUsername;

}
