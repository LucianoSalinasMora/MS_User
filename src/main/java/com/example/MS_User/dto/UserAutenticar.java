package com.example.MS_User.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Schema(description = "Datos devueltos por el servidor tras una autenticación exitosa, incluyendo el token de acceso")
public class UserAutenticar {

    @Schema(description = "Token JWT generado para autorizar las peticiones subsiguientes", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    private String token;

    @Schema(description = "Nombre de usuario (RUT) de la cuenta autenticada", example = "20123456-K")
    private String username;

    @Schema(description = "Rol del usuario en el sistema académico que define sus permisos", example = "ESTUDIANTE")
    private String role;
}