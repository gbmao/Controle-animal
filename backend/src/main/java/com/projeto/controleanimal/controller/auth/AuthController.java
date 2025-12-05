package com.projeto.controleanimal.controller.auth;



import java.util.List;
import java.util.stream.Collectors;

import com.projeto.controleanimal.controller.auth.payload.request.SignupRequest;
import com.projeto.controleanimal.controller.auth.payload.response.JwtResponse;
import com.projeto.controleanimal.controller.auth.payload.response.MessageResponse;
import com.projeto.controleanimal.dto.loginRequest.LoginRequest;
import com.projeto.controleanimal.model.AppUser;
import com.projeto.controleanimal.repository.UserRepository;
import com.projeto.controleanimal.security.service.CustomUserDetails;
import com.projeto.controleanimal.security.jwt.JwtUtils;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository UserRepository;



    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateAppUser(@Valid @RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.login().trim(), loginRequest.password()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        CustomUserDetails AppUserDetails = (CustomUserDetails) authentication.getPrincipal();

        return ResponseEntity.ok(new JwtResponse(jwt,
                AppUserDetails.getId(),
                AppUserDetails.getUsername(),
                AppUserDetails.getEmail()));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerAppUser(@Valid @RequestBody SignupRequest signUpRequest) {
        if (UserRepository.existsByLogin(signUpRequest.getLogin().trim())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Já exite AppUser com esse nome!"));
        }

        if (UserRepository.existsByEmail(signUpRequest.getEmail().trim())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Email já está em uso!"));
        }

        //TODO retornar email null response
        // Create new AppUser's account
        AppUser AppUser = new AppUser(signUpRequest.getLogin().trim(),
                signUpRequest.getEmail().trim(),
                encoder.encode(signUpRequest.getPassword()));



        UserRepository.save(AppUser);

        return ResponseEntity.ok(new MessageResponse("Usuario registrado com sucesso!"));
    }
}