package com.chess.api.core.exception;

import java.util.Map;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.web.context.request.WebRequest;

public class AppErrorAttributes extends DefaultErrorAttributes {

    private final String currentApiVersion;

    public AppErrorAttributes(String currentApiVersion) {
        this.currentApiVersion = currentApiVersion;
    }

    public Map<String, Object> getErrorAttributes(final WebRequest webRequest, ErrorAttributeOptions options) {
        final Map<String, Object> defaultErrorAttributes = super.getErrorAttributes(webRequest, options);
        final ErrorView errorView = ErrorView.fromDefaultAttributeMap(currentApiVersion, defaultErrorAttributes);
        return errorView.toAttributeMap();
    }

}
