package com.example.dulumi.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AddUserRequest {
    private String user_id;
    private String username;
    private String provider;
    private String email;
    private String password;
    private String role;
    private String provider_id;
    private String create_date;
}
