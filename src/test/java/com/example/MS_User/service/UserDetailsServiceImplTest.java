package com.example.MS_User.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.example.MS_User.model.User;
import com.example.MS_User.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class UserDetailsServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserDetailsServiceImpl userDetailsService;

    private User usuarioActivo;

    @BeforeEach
    void setUp() {
        usuarioActivo = new User();
        usuarioActivo.setId(1L);
        usuarioActivo.setUsername("20123456-K");
        usuarioActivo.setPassword("$2a$10$encodedPasswordHashMock");
        usuarioActivo.setEmail("alumno@duocuc.cl");
        usuarioActivo.setEstudianteId("EST-9941");
        usuarioActivo.setRole(User.Role.ESTUDIANTE); 
        usuarioActivo.setEnabled(true);
    }

    @Test
    void testLoadUserByUsername_UsuarioExisteYEstaActivo_RetornaUserDetails() {
        when(userRepository.findByUsername("20123456-K")).thenReturn(Optional.of(usuarioActivo));

        UserDetails resultado = userDetailsService.loadUserByUsername("20123456-K");

        assertNotNull(resultado);
        assertEquals("20123456-K", resultado.getUsername());
        assertEquals("$2a$10$encodedPasswordHashMock", resultado.getPassword());
        assertTrue(resultado.isEnabled());
        assertTrue(resultado.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ESTUDIANTE")),
                "Debe poseer la autoridad ROLE_ESTUDIANTE");

        verify(userRepository, times(1)).findByUsername("20123456-K");
    }

    @Test
    void testLoadUserByUsername_UsuarioNoExiste_LanzaUsernameNotFoundException() {
        when(userRepository.findByUsername("11111111-1")).thenReturn(Optional.empty());

        UsernameNotFoundException excepcion = assertThrows(UsernameNotFoundException.class, () -> {
            userDetailsService.loadUserByUsername("11111111-1");
        });

        assertEquals("Usuario no encontrado: 11111111-1", excepcion.getMessage());
        verify(userRepository, times(1)).findByUsername("11111111-1");
    }
}