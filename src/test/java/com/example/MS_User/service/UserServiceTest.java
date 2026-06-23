package com.example.MS_User.service;


import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.MS_User.dto.UserAutenticar;
import com.example.MS_User.dto.UserLogin;
import com.example.MS_User.dto.UserRegistro;
import com.example.MS_User.model.User;
import com.example.MS_User.repository.UserRepository;
import com.example.MS_User.security.JwtUtil;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private UserService userService;

    private User user;
    private UserRegistro userRegistro;
    private UserLogin userLogin;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setUsername("juan.perez");
        user.setPassword("encodedPassword");
        user.setEmail("juan@duocuc.cl");
        user.setEstudianteId("100L");
        user.setRole(User.Role.ESTUDIANTE);
        user.setEnabled(true);

        userRegistro = new UserRegistro();
        userRegistro.setUsername("juan.perez");
        userRegistro.setPassword("rawPassword");
        userRegistro.setEmail("juan@duocuc.cl");
        userRegistro.setEstudianteId("100L");

        userLogin = new UserLogin();
        userLogin.setUsername("juan.perez");
        userLogin.setPassword("rawPassword");
    }

    @Test
    void testRegistrarExitoso() {
        when(userRepository.existsByUsername("juan.perez")).thenReturn(false);
        when(userRepository.existsByEmail("juan@duocuc.cl")).thenReturn(false);
        when(passwordEncoder.encode("rawPassword")).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);

        User resultado = userService.registrar(userRegistro);

        assertNotNull(resultado);
        assertEquals("juan.perez", resultado.getUsername());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testRegistrarUsernameDuplicado() {
        when(userRepository.existsByUsername("juan.perez")).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> {
            userService.registrar(userRegistro);
        });

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testRegistrarEmailDuplicado() {
        when(userRepository.existsByUsername("juan.perez")).thenReturn(false);
        when(userRepository.existsByEmail("juan@duocuc.cl")).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> {
            userService.registrar(userRegistro);
        });

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testLoginExitoso() {
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(null);
        when(userRepository.findByUsername("juan.perez")).thenReturn(Optional.of(user));
        when(jwtUtil.generateToken("juan.perez", "ESTUDIANTE")).thenReturn("mocked-jwt-token");

        UserAutenticar resultado = userService.login(userLogin);

        assertNotNull(resultado);
        assertEquals("mocked-jwt-token", resultado.getToken());
        assertEquals("juan.perez", resultado.getUsername());
        assertEquals("ESTUDIANTE", resultado.getRole());
    }

    @Test
    void testObtenerUsuarios() {
        List<User> lista = Arrays.asList(user);
        when(userRepository.findAll()).thenReturn(lista);

        List<User> resultado = userService.obtenerUsuarios();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void testObtenerUsuarioEncontrado() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        Optional<User> resultado = userService.obtenerUsuario(1L);

        assertTrue(resultado.isPresent());
        assertEquals("juan.perez", resultado.get().getUsername());
    }

    @Test
    void testObtenerUsuarioNoEncontrado() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<User> resultado = userService.obtenerUsuario(1L);

        assertFalse(resultado.isPresent());
    }

    @Test
    void testEliminarUsuarioExitoso() {
        when(userRepository.existsById(1L)).thenReturn(true);
        doNothing().when(userRepository).deleteById(1L);

        boolean eliminado = userService.eliminarUsuario(1L);

        assertTrue(eliminado);
        verify(userRepository, times(1)).deleteById(1L);
    }

    @Test
    void testEliminarUsuarioNoExiste() {
    
        when(userRepository.existsById(1L)).thenReturn(false);

    
        boolean eliminado = userService.eliminarUsuario(1L);

    
        assertFalse(eliminado); 
        verify(userRepository, never()).deleteById(anyLong());
    }
}