package com.josef.api_rest.services;

import com.josef.api_rest.data.dto.v1.security.AccountCredentialsDTO;
import com.josef.api_rest.data.dto.v1.security.TokenDTO;
import com.josef.api_rest.repository.UserRepository;
import com.josef.api_rest.security.jwt.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    UserRepository repository;

    public ResponseEntity<TokenDTO> signIn(AccountCredentialsDTO credentials) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        credentials.getUsername(),
                        credentials.getPassword()
                )
        );

        var user = repository.findUserByName(credentials.getUsername());
        if (user == null) throw new UsernameNotFoundException("Username not exist!");

        var tokenResponse = tokenProvider.createAccessToken(credentials.getUsername(), user.getRoles());
        return ResponseEntity.ok(tokenResponse);
    }
}
