package com.zinchenko.common.error;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;

public class BasicErrorResponse {

    @JsonProperty("message")
    private String message;

    @JsonProperty("time")
    private Instant time;

    public BasicErrorResponse(String message) {
        this.message = message;
        this.time = Instant.now();
    }

    public String getMessage() {
        return message;
    }

    public BasicErrorResponse setMessage(String message) {
        this.message = message;
        return this;
    }

    public Instant getTime() {
        return time;
    }

    public BasicErrorResponse setTime(Instant time) {
        this.time = time;
        return this;
    }
}
