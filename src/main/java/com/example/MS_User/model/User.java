package com.example.MS_User.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "users")
@Schema(description = "Representa las credenciales y el perfil de acceso de un usuario en el sistema")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Identificador único autogenerado del usuario", example = "1")
    private Long id;

    @Column(unique = true, nullable = false)
    @Schema(description = "Nombre de usuario único, típicamente el RUT del alumno o docente", example = "20123456-K", requiredMode = Schema.RequiredMode.REQUIRED)
    private String username;

    @Column(nullable = false)
    @Schema(description = "Contraseña encriptada del usuario", example = "$2a$10$7R...", requiredMode = Schema.RequiredMode.REQUIRED)
    private String password; 

    @Column(unique = true, nullable = false)
    @Schema(description = "Dirección de correo electrónico institucional", example = "usuario@duocuc.cl", requiredMode = Schema.RequiredMode.REQUIRED)
    private String email;

    @Schema(description = "ID relacional del estudiante asociado (opcional si es ADMIN)", example = "EST-9941")
    private String estudianteId; 

    @Enumerated(EnumType.STRING)
    @Schema(description = "Rol asignado para el control de accesos en los microservicios", example = "ESTUDIANTE")
    private Role role;

    @Schema(description = "Estado de habilitación de la cuenta de usuario", example = "true")
    private Boolean enabled = true;

    @Schema(description = "Roles permitidos dentro de la plataforma académica")
    public enum Role {
        ADMIN, ESTUDIANTE, DOCENTE
    }
}