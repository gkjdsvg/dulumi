package com.example.dulumi.info;

import java.util.Map;

public class emailUserInfo implements OAuth2UserInfo {
    private Map<String, Object> attributes;

    public emailUserInfo(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    public String getProviderId() {
        return String.valueOf(attributes.get("id"));
    }

    public String getProvider() {
        return "email";
    }

    public String getEmail() {
        return String.valueOf(attributes.get("email"));
    }

    public String getName() {
        return String.valueOf(attributes.get("name"));
    }
}
