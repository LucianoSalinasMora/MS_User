package com.example.MS_User.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Credenciales requeridas para iniciar sesión en la plataforma")
public class UserLogin {

    @Schema(
        description = "Nombre de usuario registrado (RUT sin puntos y con guión)", 
        example = "20123456-K", 
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String username;

    @Schema(
        description = "Contraseña de acceso a la cuenta", 
        example = "MiClaveSegura123", 
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String password;
}