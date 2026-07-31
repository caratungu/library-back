package com.library.demo.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.library.demo.enums.EstadoEjemplarEnum;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "ejemplares")
@Data
public class Ejemplar {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "libro_id", nullable = false)
    private Libro libro;

    @Column(nullable = false)
    private String codigo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoEjemplarEnum estado;
}
