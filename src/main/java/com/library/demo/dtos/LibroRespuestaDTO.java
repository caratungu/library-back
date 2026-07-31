package com.library.demo.dtos;

import java.time.LocalDate;
import java.util.List;

public record LibroRespuestaDTO(
    Long id,
    String titulo,
    String isbn,
    String edicion,
    LocalDate fecha_publicacion,
    String autor,
    List<EjemplarRespuestaDTO> ejemplares
) {}
