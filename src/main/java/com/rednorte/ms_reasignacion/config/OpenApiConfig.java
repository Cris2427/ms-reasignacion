package com.rednorte.ms_reasignacion.config;


import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("ms-reasignacion API")
                        .version("1.0.0")
                        .description("Microservicio de Reasignacion Automatica de Citas Medicas " +
                                "del Sistema RedNorte. Gestiona el proceso automatico de reasignacion " +
                                "cuando un paciente cancela su cita médica.")
                        .contact(new Contact()
                                .name("Equipo RedNorte")
                                .email("rednorte@duocuc.cl"))
                        .license(new License()
                                .name("MIT License")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8084")
                                .description("Servidor de desarrollo")
                ));
    }
}
