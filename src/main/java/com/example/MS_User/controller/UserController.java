package com.example.MS_User.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.MS_User.dto.UserAutenticar;
import com.example.MS_User.dto.UserLogin;
import com.example.MS_User.dto.UserRegistro;
import com.example.MS_User.model.User;
import com.example.MS_User.service.UserService;

@RestController
@RequestMapping("/") 
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/auth/register")
    public ResponseEntity<User> registrar(@RequestBody UserRegistro request) {
        try {
            User nuevo = userService.registrar(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevo);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/auth/login")
    public ResponseEntity<UserAutenticar> login(@RequestBody UserLogin request) {
        try {
            UserAutenticar response = userService.login(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> obtenerUsuarios() {
        List<User> usuarios = userService.obtenerUsuarios();
        if (usuarios.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(usuarios);
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<User> obtenerUsuario(@PathVariable Long id) {
        return userService.obtenerUsuario(id)
                .map(u -> ResponseEntity.ok(u))
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> eliminarUsuario(@PathVariable Long id) {
        if (userService.eliminarUsuario(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}