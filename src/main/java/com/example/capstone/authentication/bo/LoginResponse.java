package com.example.capstone.authentication.bo;

public class LoginResponse {
    private String token;

    private long expiresIn;

    private String role;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public long getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(long expiresIn) {
        this.expiresIn = expiresIn;
    }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
}
