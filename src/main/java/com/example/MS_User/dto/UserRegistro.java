package com.example.MS_User.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Datos obligatorios y opcionales para registrar una nueva cuenta de usuario")
public class UserRegistro {

    @Schema(
        description = "Nombre de usuario único (RUT del alumno o docente)", 
        example = "20123456-K", 
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String username;

    @Schema(
        description = "Contraseña en texto plano (será encriptada mediante BCrypt en el servidor)", 
        example = "ClaveAlumno2026", 
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String password;

    @Schema(
        description = "Correo electrónico institucional de contacto", 
        example = "alumno@duocuc.cl", 
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String email;

    @Schema(
        description = "Identificador alfanumérico del estudiante en el microservicio de Malla/Inscripciones (Opcional si es Admin)", 
        example = "EST-9941"
    )
    private String estudianteId;
}