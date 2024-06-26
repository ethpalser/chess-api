package com.chess.api.data;

public enum Privileges {

    SESSION_READ("SESSION_READ"),
    SESSION_WRITE("SESSION_WRITE"),
    USER_READ("USER_READ"),
    USER_WRITE("USER_WRITE");

    private final String name;

    Privileges(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

}
