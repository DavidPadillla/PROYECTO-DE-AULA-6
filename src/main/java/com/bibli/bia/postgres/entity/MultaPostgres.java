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
@Table(name = "multas")
public class MultaPostgres {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "id_usuario")
    private String idUsuario;

    @Column(name = "nombre_usuario")
    private String nombreUsuario;

    @Column(name = "libro")
    private String libro;

    @Column(name = "fecha_reserva")
    private LocalDate fechaReserva;

    @Column(name = "fecha_devolucion")
    private LocalDate fechaDevolucion;

    @Column(name = "dias_retraso")
    private int diasRetraso;

    @Column(name = "valor_multa")
    private double valorMulta;

    @Column(name = "pagada")
    private boolean pagada = false;

    @Column(name = "fecha_pago")
    private LocalDate fechaPago;

    // Constructor sin ID
    public MultaPostgres(String idUsuario, String nombreUsuario, String libro,
                         LocalDate fechaReserva, LocalDate fechaDevolucion,
                         int diasRetraso, double valorMulta) {
        this.idUsuario = idUsuario;
        this.nombreUsuario = nombreUsuario;
        this.libro = libro;
        this.fechaReserva = fechaReserva;
        this.fechaDevolucion = fechaDevolucion;
        this.diasRetraso = diasRetraso;
        this.valorMulta = valorMulta;
        this.pagada = false;
        this.fechaPago = null;
    }
}