package com.projeto.controleanimal.security.service;

import com.projeto.controleanimal.model.AppUser;
import com.projeto.controleanimal.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    UserRepository userRepository;


    @Override
    @Transactional
    public CustomUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser user = userRepository.findByLogin(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario nao encontrado com esse login: " + username));
        return CustomUserDetails.build(user);
    }
}
