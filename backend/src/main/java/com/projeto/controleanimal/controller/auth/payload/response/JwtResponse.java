package com.projeto.controleanimal.controller.auth.payload.response;

public class JwtResponse {
    private String token;
    private String type = "Bearer";
    private Long id;
    private String login;
    private String email;

    public JwtResponse(String accessToken, Long id, String login, String email) {
        this.token = accessToken;
        this.id = id;
        this.login = login;
        this.email = email;
    }

    public String getAccessToken() {
        return token;
    }

    public void setAccessToken(String accessToken) {
        this.token = accessToken;
    }

    public String getTokenType() {
        return type;
    }

    public void setTokenType(String tokenType) {
        this.type = tokenType;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }
}
