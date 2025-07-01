package com.example.dulumi.config.JWT;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.dulumi.domain.User;

public class CreateJwt {
    public static String createAccessToken(User user) {
        return JWT.create()
                .withSubject(user.getUsername())
                .withClaim("id", user.getId())
                .withClaim("username", user.getUsername())
                .sign(Algorithm.HMAC256(JwtProperties.SECRET));

    }

    public static String createRefreshToken(User user, String AccessToken) {
        return JWT.create()
                .withSubject(user.getUsername())
                .withClaim("AccessToken", AccessToken)
                .withClaim("username", user.getUsername())
                .sign(Algorithm.HMAC256(JwtProperties.SECRET));
    }
}
