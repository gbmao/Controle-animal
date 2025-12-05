package com.projeto.controleanimal.controller.auth.payload.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class SignupRequest {

    @NotBlank(message = "Login nao pode estar vazio")
    @Size(min = 3, max = 20, message = "Login tem que ter entre 3 a 20 caracteres")
    private String login;

    @NotBlank(message = "Email vazio")
    @Email(regexp = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$",
            message = "Email inválido")
    @Size(max = 50, message = "Email só pode ter até 50 caracteres")
    private String email;

    @NotBlank(message = "A senha nao pode estar vazia")
    @Size(min = 6, max = 49,message = "Senha tem que ter entre 6 a 49 caracteres")
    private String password;

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
