//package com.example.booking.config;
//
//
//import io.swagger.v3.oas.annotations.OpenAPIDefinition;
//import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
//import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
//import io.swagger.v3.oas.annotations.info.Contact;
//import io.swagger.v3.oas.annotations.info.Info;
//import io.swagger.v3.oas.annotations.security.SecurityScheme;
//import io.swagger.v3.oas.annotations.servers.Server;
//
//@OpenAPIDefinition(
//        info = @Info(
//                title = "First project APIs",
//                description = "All APIs of this project",
//                contact = @Contact(
//                        name = "Milo Jani"
//                ),
//                version = "1.0.0"
//        ),
//        servers = {
//                @Server(
//                        description = "LOCAL ENV",
//                        url = "http://localhost:8080"
//                )
//        }
//)
//@SecurityScheme(
//        name = "Bearer authentication",
//        description = "Must use a token to verify authentication",
//        scheme = "bearer",
//        type = SecuritySchemeType.HTTP,
//        bearerFormat = "JWT",
//        in = SecuritySchemeIn.HEADER
//)
//public class OpenApiConfiguration {
//}
