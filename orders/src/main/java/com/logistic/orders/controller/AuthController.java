package com.logistic.orders.controller;

import com.logistic.orders.security.JwtTokenProvider;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Tag(name = "Autenticación", description = "Operacion para realizar login y obtener un token JWT")
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    public AuthController(AuthenticationManager authenticationManager,
                          JwtTokenProvider jwtTokenProvider) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
    }
    @Operation(summary = "Autenticación de usuario", description = "Recibe credenciales de acceso y devuelve un token JWT")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Autenticación exitosa",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = AuthResponse.class))),
            @ApiResponse(responseCode = "401", description = "Credenciales inválidas",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Solicitud malformada",
                    content = @Content(mediaType = "application/json"))
    })
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request) {
        logger.info("Request received to auth username: {} password: {}",request.getUsername(), request.getPassword());
        var auth = new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword());
        authenticationManager.authenticate(auth);
        String token = jwtTokenProvider.generateToken(request.getUsername());
        logger.info("Token generated for user: {}", request.getUsername());
        return ResponseEntity.ok(new AuthResponse(token));
    }

    @Schema(description = "Request body para autenticación")
    static class AuthRequest {
        @Schema(description = "Nombre de usuario", example = "admin")
        private String username;
        @Schema(description = "Contraseña del usuario", example = "secret123")
        private String password;

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }

    @Schema(description = "Respuesta con token JWT")
    static class AuthResponse {
        @Schema(description = "Token JWT generado")
        private String token;
        public AuthResponse(String token) {
            this.token = token;
        }
        public String getToken() {
            return token;
        }
    }
}
