package com.robert.schedules.controllers;

import com.robert.schedules.entities.BadRequestResponse;
import com.robert.schedules.entities.SuccessResponse;
import com.robert.schedules.entities.User;
import com.robert.schedules.repositories.UsersRepository;
import com.robert.schedules.security.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;

@RestController
@RequestMapping("/users")
public class RegisterUserController {
    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private TokenService tokenService;

    @PostMapping
    public ResponseEntity usersRegister(@RequestBody User user) throws SQLException {

        User checkExists = usersRepository.findByEmail(user.getEmail());

        if (checkExists != null) {
            BadRequestResponse response = new BadRequestResponse(false, "Já existe um usuário com este e-mail cadastrado");
            return ResponseEntity.badRequest().body(response);
        }

        String encryptedPassword = new BCryptPasswordEncoder().encode(user.getPassword());

        user.setPassword(encryptedPassword);

        usersRepository.save(user);

        var token = tokenService.generateToken(user);

        SuccessResponse<String> response = new SuccessResponse<>(token, true);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
