package com.bibli.bia.postgres.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "libros_fisicos")
public class LibroFisicoPostgres {

    @Id
    @Column(columnDefinition = "TEXT")
    private String id;  // Cambiar de Long a String para coincidir con TEXT en BD

    @Column(columnDefinition = "TEXT")
    private String titulo;

    @Column(columnDefinition = "TEXT")
    private String autor;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    @Column(columnDefinition = "TEXT")
    private String categoria;

    private int stock = 0;
    private int reservado = 0;

}