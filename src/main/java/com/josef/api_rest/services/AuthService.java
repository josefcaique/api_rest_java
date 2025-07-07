package com.josef.api_rest.services;

import com.josef.api_rest.data.dto.v1.PersonDTO;
import com.josef.api_rest.data.dto.v1.security.AccountCredentialsDTO;
import com.josef.api_rest.data.dto.v1.security.TokenDTO;
import com.josef.api_rest.exception.RequiredObjectIsNullException;
import com.josef.api_rest.model.Person;
import com.josef.api_rest.model.User;
import com.josef.api_rest.repository.UserRepository;
import com.josef.api_rest.security.jwt.JwtTokenProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

import static com.josef.api_rest.mapper.ObjectMapper.parseObject;

@Service
public class AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    UserRepository repository;

    Logger logger = LoggerFactory.getLogger(AuthService.class);

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

    public ResponseEntity<TokenDTO> refreshToken(String username, String refreshToken) {
        var user = repository.findUserByName(username);
        if (user == null) throw new UsernameNotFoundException("Username not exist!");
        TokenDTO token = tokenProvider.refreshToken(refreshToken);
        return ResponseEntity.ok(token);
    }

    private String generateHashedPassword(String password) {
        PasswordEncoder pbkdf2Encoder = new Pbkdf2PasswordEncoder("",
                8,
                185000,
                Pbkdf2PasswordEncoder.SecretKeyFactoryAlgorithm.PBKDF2WithHmacSHA256);
        Map<String, PasswordEncoder> encoders = new HashMap<>();
        encoders.put("pbkdf2", pbkdf2Encoder);
        DelegatingPasswordEncoder passwordEncoder = new DelegatingPasswordEncoder("pbkdf2", encoders);

        passwordEncoder.setDefaultPasswordEncoderForMatches(pbkdf2Encoder);
        return passwordEncoder.encode(password);

    }

    public AccountCredentialsDTO create(AccountCredentialsDTO user) {

        if (user == null) throw new RequiredObjectIsNullException();

        logger.info("Creating a new user");
        var entity = new User();
        entity.setUsername(user.getUsername());
        entity.setFullName(user.getFullname());
        entity.setPassword(generateHashedPassword(user.getPassword()));
        entity.setAccountNonExpired(true);
        entity.setAccountNonLocked(true);
        entity.setCredentialsNonExpired(true);
        entity.setEnabled(true);

        return parseObject(repository.save(entity), AccountCredentialsDTO.class);
    }
}
