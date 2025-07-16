package com.example.dulumi.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ResponseDTO<T> {
    private int status;
    private String message;
    private T data;

    public static <T> ResponseDTO<T> res(int status, String message, T data) {
        return new ResponseDTO<>(status, message, data);
    }
}
