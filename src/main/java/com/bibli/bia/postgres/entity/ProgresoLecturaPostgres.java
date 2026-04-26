package com.bibli.bia.postgres.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "progreso_lectura")
public class ProgresoLecturaPostgres {

    @Id
    @Column(columnDefinition = "TEXT")
    private String id;  // ✅ Cambiado: de Long a String para TEXT en BD

    @Column(name = "username", columnDefinition = "TEXT", unique = true)
    private String username;

    @Column(columnDefinition = "TEXT")
    private String librosCompletados;  // ← GUARDA JSON: ["libro1","libro2"]

    @Column(columnDefinition = "TEXT")
    private String capitulosPorLibro;  // ← GUARDA JSON: {"libro1":[1,2,3]}

    @Column(name = "total_libros_leidos")
    private int totalLibrosLeidos = 0;

    @Column(name = "ultima_actualizacion")
    private LocalDateTime ultimaActualizacion;

    @Column(name = "puntos")
    private int puntos = 0;

    // Constructor con ID String
    public ProgresoLecturaPostgres(String id, String username) {
        this.id = id;
        this.username = username;
        this.librosCompletados = "[]";
        this.capitulosPorLibro = "{}";
        this.totalLibrosLeidos = 0;
        this.ultimaActualizacion = LocalDateTime.now();
        this.puntos = 0;
    }

    // Constructor solo con username (genera ID automático o puedes usar UUID)
    public ProgresoLecturaPostgres(String username) {
        this.id = java.util.UUID.randomUUID().toString();  // Genera un ID único
        this.username = username;
        this.librosCompletados = "[]";
        this.capitulosPorLibro = "{}";
        this.totalLibrosLeidos = 0;
        this.ultimaActualizacion = LocalDateTime.now();
        this.puntos = 0;
    }
}