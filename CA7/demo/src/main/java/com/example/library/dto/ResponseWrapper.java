package com.example.library.dto;

import java.time.Instant;
import java.util.Map;

public class ResponseWrapper {
    private boolean success;
    private String message;
    private Instant timestamp;
    private Object data;

    public ResponseWrapper(boolean success, String message, Object data) {
        this.success = success;
        this.message = message;
        this.timestamp = Instant.now();
        this.data = data;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public Object getData() {
        return data;
    }

    public static ResponseWrapper success(String message, Object data) {
        return new ResponseWrapper(true, message, data);
    }

    public static ResponseWrapper error(String message) {
        return new ResponseWrapper(false, message, null);
    }
}
