package com.bibli.bia.postgres.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "resenas")
public class ResenaPostgres {

    @Id
    @Column(columnDefinition = "TEXT")
    private String id;

    @Column(columnDefinition = "TEXT")
    private String nombre;

    @Column(columnDefinition = "TEXT")
    private String comentario;

    // Constructor sin ID (genera UUID automáticamente)
    // ✅ Este tiene 2 parámetros (nombre, comentario) - DIFERENTE al de arriba
    public ResenaPostgres(String nombre, String comentario) {
        this.id = java.util.UUID.randomUUID().toString();
        this.nombre = nombre;
        this.comentario = comentario;
    }
}