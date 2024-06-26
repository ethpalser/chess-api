package com.chess.api.core.exception;

import java.time.LocalDateTime;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ErrorView {

    private String apiVersion;
    private String error;
    private String code;
    private String message;
    private String path;
    private String timestamp;

    public static ErrorView fromDefaultAttributeMap(String apiVersion, Map<String, Object> defaultErrorAttributes) {
        return new ErrorView(
                apiVersion,
                defaultErrorAttributes.get("status").toString(),
                "SERVER-ERR",
                (String) defaultErrorAttributes.getOrDefault("message", "no message available"),
                (String) defaultErrorAttributes.getOrDefault("error", "no error available"),
                (String) defaultErrorAttributes.getOrDefault("timestamp", LocalDateTime.now().toString())
        );
    }

    public Map<String, Object> toAttributeMap() {
        return Map.of(
                "apiVersion", apiVersion,
                "error", error,
                "code", code,
                "message", message,
                "path", path,
                "timestamp", timestamp);
    }

}
