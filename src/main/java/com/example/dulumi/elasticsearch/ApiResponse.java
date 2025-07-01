package com.example.dulumi.elasticsearch;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ApiResponse<T> {
    private boolean success;
    private String message;
    private T data;

    public ApiResponse() {

    }

    public static <T> ApiResponse<T> onSuccess(T data) {
        ApiResponse<T> apiResponse = new ApiResponse<>();
        apiResponse.setSuccess(true);
        apiResponse.setData(data);
        return apiResponse;
    }
}
