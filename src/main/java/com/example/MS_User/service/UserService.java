package com.example.MS_User.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.MS_User.dto.UserAutenticar;
import com.example.MS_User.dto.UserLogin;
import com.example.MS_User.dto.UserRegistro;
import com.example.MS_User.model.User;
import com.example.MS_User.repository.UserRepository;
import com.example.MS_User.security.JwtUtil;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    //username y email únicos password siempre cifrado
    public User registrar(UserRegistro request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new IllegalArgumentException("El nombre de usuario ya está en uso.");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("El correo ya está registrado.");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEmail(request.getEmail());
        user.setEstudianteId(request.getEstudianteId());
        user.setRole(User.Role.ESTUDIANTE);
        user.setEnabled(true);

        return userRepository.save(user);
    }

    public UserAutenticar login(UserLogin request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado."));

        String token = jwtUtil.generateToken(user.getUsername(), user.getRole().name());

        return new UserAutenticar(token, user.getUsername(), user.getRole().name());
    }

    public List<User> obtenerUsuarios() {
        return userRepository.findAll();
    }

    public Optional<User> obtenerUsuario(Long id) {
        return userRepository.findById(id);
    }

    public boolean eliminarUsuario(Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return true;
        }
        return false;
    }
}