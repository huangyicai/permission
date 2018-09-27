package com.mmall.model;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class AuthInfo<T> {
    private String token;
    private T auth;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public T getAuth() {
        return auth;
    }

    public void setAuth(T auth) {
        this.auth = auth;
    }

    public AuthInfo() {
    }
}
