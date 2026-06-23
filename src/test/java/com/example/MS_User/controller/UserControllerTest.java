package com.example.MS_User.controller;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.example.MS_User.dto.UserAutenticar;
import com.example.MS_User.dto.UserLogin;
import com.example.MS_User.dto.UserRegistro;
import com.example.MS_User.model.User;
import com.example.MS_User.service.UserService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private User user;
    private UserRegistro userRegistro;
    private UserLogin userLogin;
    private UserAutenticar userAutenticar;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setUsername("juan.perez");
        user.setEmail("juan@duocuc.cl");
        user.setRole(User.Role.ESTUDIANTE);

        userRegistro = new UserRegistro();
        userLogin = new UserLogin();
        userAutenticar = new UserAutenticar("token123", "juan.perez", "ESTUDIANTE");
    }

    @Test
    void testRegistrarCreado_201() {
        when(userService.registrar(any(UserRegistro.class))).thenReturn(user);

        ResponseEntity<User> respuesta = userController.registrar(userRegistro);

        assertEquals(HttpStatus.CREATED, respuesta.getStatusCode());
        assertNotNull(respuesta.getBody());
        assertEquals("juan.perez", respuesta.getBody().getUsername());
    }

    @Test
    void testRegistrarConflicto_409() {
        when(userService.registrar(any(UserRegistro.class))).thenThrow(new IllegalArgumentException("Duplicado"));

        ResponseEntity<User> respuesta = userController.registrar(userRegistro);

        assertEquals(HttpStatus.CONFLICT, respuesta.getStatusCode());
        assertNull(respuesta.getBody());
    }

    @Test
    void testRegistrarErrorServidor_500() {
        when(userService.registrar(any(UserRegistro.class))).thenThrow(new RuntimeException("Error fatal"));

        ResponseEntity<User> respuesta = userController.registrar(userRegistro);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, respuesta.getStatusCode());
    }

    @Test
    void testLoginExitoso_200() {
        when(userService.login(any(UserLogin.class))).thenReturn(userAutenticar);

        ResponseEntity<UserAutenticar> respuesta = userController.login(userLogin);

        assertEquals(HttpStatus.OK, respuesta.getStatusCode());
        assertEquals("token123", respuesta.getBody().getToken());
    }

    @Test
    void testLoginFallido_401() {
        when(userService.login(any(UserLogin.class))).thenThrow(new RuntimeException("Bad credentials"));

        ResponseEntity<UserAutenticar> respuesta = userController.login(userLogin);

        assertEquals(HttpStatus.UNAUTHORIZED, respuesta.getStatusCode());
    }

    @Test
    void testObtenerUsuariosConDatos_200() {
        List<User> usuarios = Arrays.asList(user);
        when(userService.obtenerUsuarios()).thenReturn(usuarios);

        ResponseEntity<List<User>> respuesta = userController.obtenerUsuarios();

        assertEquals(HttpStatus.OK, respuesta.getStatusCode());
        assertEquals(1, respuesta.getBody().size());
    }

    @Test
    void testObtenerUsuariosVacio_204() {
        when(userService.obtenerUsuarios()).thenReturn(new ArrayList<>());

        ResponseEntity<List<User>> respuesta = userController.obtenerUsuarios();

        assertEquals(HttpStatus.NO_CONTENT, respuesta.getStatusCode());
    }

    @Test
    void testObtenerUsuarioPorIdEncontrado_200() {
        when(userService.obtenerUsuario(1L)).thenReturn(Optional.of(user));

        ResponseEntity<User> respuesta = userController.obtenerUsuario(1L);

        assertEquals(HttpStatus.OK, respuesta.getStatusCode());
        assertEquals("juan.perez", respuesta.getBody().getUsername());
    }

    @Test
    void testObtenerUsuarioPorIdNoEncontrado_404() {
        when(userService.obtenerUsuario(1L)).thenReturn(Optional.empty());

        ResponseEntity<User> respuesta = userController.obtenerUsuario(1L);

        assertEquals(HttpStatus.NOT_FOUND, respuesta.getStatusCode());
    }

    @Test
    void testEliminarUsuarioExitoso_204() {
        when(userService.eliminarUsuario(1L)).thenReturn(true);

        ResponseEntity<Void> respuesta = userController.eliminarUsuario(1L);

        assertEquals(HttpStatus.NO_CONTENT, respuesta.getStatusCode());
    }

    @Test
    void testEliminarUsuarioFallido_404() {
        when(userService.eliminarUsuario(1L)).thenReturn(false);

        ResponseEntity<Void> respuesta = userController.eliminarUsuario(1L);

        assertEquals(HttpStatus.NOT_FOUND, respuesta.getStatusCode());
    }
}