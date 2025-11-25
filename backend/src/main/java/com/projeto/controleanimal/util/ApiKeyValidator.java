package com.projeto.controleanimal.util;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public final class ApiKeyValidator {

    private static final String SECRET = System.getenv("API_SECRET");

    private ApiKeyValidator() {
    }

    public static void check(String key) {
        if(SECRET == null || !SECRET.equals(key)) {

            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Não autorizado");
        }
    }

}
