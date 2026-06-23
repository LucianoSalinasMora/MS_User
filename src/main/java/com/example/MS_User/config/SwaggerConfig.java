package com.example.MS_User.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.Schema;
import jakarta.persistence.GeneratedValue;

import java.lang.reflect.Field;

import org.springdoc.core.customizers.PropertyCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;



@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI userOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API de Usuarios (MS_User)")
                        .description("Microservicio para la autenticación y gestión de usuarios del sistema académico")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Duoc UC")
                                .email("contacto@duocuc.cl")));
    }
    @Bean
    public PropertyCustomizer generatedValueReadOnlyCustomizer() {
        return (Schema schema, io.swagger.v3.core.converter.AnnotatedType type) -> {    
            if (type.getType() instanceof Class<?>) {
                Class<?> clazz = (Class<?>) type.getType();
                
                
                for (Field field : clazz.getDeclaredFields()) {
                    if (field.getName().equals(schema.getName()) || 
                       (schema.getExtensions() != null && field.getName().equals(schema.getExtensions().get("x-properties")))) {
                        
                        
                        if (field.isAnnotationPresent(GeneratedValue.class)) {
                            schema.setReadOnly(true);
                        }
                    }
                }
            }
            return schema;
        };
    }
}
