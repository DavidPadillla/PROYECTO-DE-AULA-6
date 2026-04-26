package com.bibli.bia.postgres.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "reservas")
public class ReservaPostgres {

    @Id
    @Column(columnDefinition = "TEXT")
    private String id;  // ✅ Cambiado: de Long a String para TEXT en BD

    @Column(name = "id_usuario", columnDefinition = "TEXT")
    private String idUsuario;

    @Column(name = "nombre_completo", columnDefinition = "TEXT")
    private String nombreCompleto;

    @Column(columnDefinition = "TEXT")
    private String correo;

    @Column(columnDefinition = "TEXT")
    private String categoria;

    @Column(columnDefinition = "TEXT")
    private String libro;

    private LocalDate fecha;

    // Constructor sin ID (genera UUID automáticamente)
    public ReservaPostgres(String idUsuario, String nombreCompleto, String correo, String categoria, String libro, LocalDate fecha) {
        this.id = java.util.UUID.randomUUID().toString();
        this.idUsuario = idUsuario;
        this.nombreCompleto = nombreCompleto;
        this.correo = correo;
        this.categoria = categoria;
        this.libro = libro;
        this.fecha = fecha;
    }
}