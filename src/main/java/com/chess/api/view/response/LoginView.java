package com.chess.api.view.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class LoginView {

    private final String jwt;
    private final String refreshToken;
}
