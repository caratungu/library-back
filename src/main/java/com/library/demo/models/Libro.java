package com.library.demo.models;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "libros")
@Data
public class Libro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String titulo;

    @Column(unique = true)
    private String isbn;

    @Column(nullable = false)
    private String edicion;

    @Column(nullable = false)
    private LocalDate fecha_publicacion;

    @Column(nullable = false)
    private String autor;

    @OneToMany(mappedBy = "libro", cascade = CascadeType.ALL)
    private List<Ejemplar> ejemplares;
}