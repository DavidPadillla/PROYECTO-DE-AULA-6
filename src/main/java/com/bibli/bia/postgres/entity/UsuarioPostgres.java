package com.bibli.bia.postgres.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "usuarios")
public class UsuarioPostgres {

    @Id
    @Column(columnDefinition = "TEXT")
    private String id;  // ✅ Cambiado: de Long a String para TEXT en BD

    @Column(name = "username", columnDefinition = "TEXT", unique = true, nullable = false)
    private String username;

    @Column(name = "password", columnDefinition = "TEXT", nullable = false)
    private String password;

    @Column(columnDefinition = "TEXT")
    private String roles;  // Guardar Set como JSON: ["ROLE_USER","ROLE_ADMIN"]

    // Constructor sin ID (genera UUID automáticamente)
    public UsuarioPostgres(String username, String password, String roles) {
        this.id = java.util.UUID.randomUUID().toString();
        this.username = username;
        this.password = password;
        this.roles = roles;
    }
}