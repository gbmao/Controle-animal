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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;


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

            throw new ResponseStatusException(HttpStatus.CONFLICT, "Já existe AppUser com esse nome!");
        }

        if (UserRepository.existsByEmail(signUpRequest.getEmail().trim())) {

            throw new ResponseStatusException(HttpStatus.CONFLICT, "Já existe AppUser com esse email: " + signUpRequest.getEmail().trim());
        }

        // Create new AppUser's account
        AppUser AppUser = new AppUser(signUpRequest.getLogin().trim(),
                signUpRequest.getEmail().trim(),
                encoder.encode(signUpRequest.getPassword()));



        UserRepository.save(AppUser);

        return ResponseEntity.ok(new MessageResponse("Usuario registrado com sucesso!"));
    }

    @GetMapping("/isAwake")
    public ResponseEntity<?> isAwake() {
        return ResponseEntity.ok(new MessageResponse("Is on"));
    }
}