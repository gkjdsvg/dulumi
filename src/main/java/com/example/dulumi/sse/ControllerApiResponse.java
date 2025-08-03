package com.example.dulumi.sse;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ControllerApiResponse<T> {
    private boolean success;
    private String message;
    private T data;

    public ControllerApiResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
        this.data = null;
    }
}
