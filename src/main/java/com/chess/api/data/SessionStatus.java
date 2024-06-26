package com.chess.api.data;

public enum SessionStatus {
    PENDING("Pending"),
    STARTED("Started"),
    COMPLETED("Completed"),
    DELETED("Deleted");

    private final String value;

    SessionStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
