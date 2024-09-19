package com.robert.schedules.controllers;

import com.robert.schedules.entities.AuthDTO;
import com.robert.schedules.entities.BadRequestResponse;
import com.robert.schedules.entities.SuccessResponse;
import com.robert.schedules.entities.User;
import com.robert.schedules.repositories.UsersRepository;
import com.robert.schedules.security.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping
    public ResponseEntity authUser(@RequestBody AuthDTO data) throws SQLException {

        var usernamePassword = new UsernamePasswordAuthenticationToken(data.email(), data.password());

        System.out.println(data.email());
        System.out.println(data.password());

        var auth = authenticationManager.authenticate(usernamePassword);

        System.out.println(auth.getPrincipal());

        var token = tokenService.generateToken((User) auth.getPrincipal());

        SuccessResponse<String> response = new SuccessResponse<>(token, true);

        return ResponseEntity.ok().body(response);
    }
}
