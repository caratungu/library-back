package com.library.demo.models;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

import com.library.demo.enums.EstadoPrestamoEnum;

@Entity
@Table(name = "prestamos")
@Data
public class Prestamo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "libro_id", nullable = false)
    private Libro libro;

    @Column(nullable = false)
    private String ejemplar_codigo;

    @Column(nullable = false)
    private LocalDate fecha_prestamo = LocalDate.now();

    @Column(nullable = false)
    private LocalDate fecha_devolucion;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_prestamo", nullable = false)
    private EstadoPrestamoEnum estadoPrestamo;
}