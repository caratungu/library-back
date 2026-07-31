package com.library.demo.dtos;

import com.library.demo.enums.EstadoEjemplarEnum;

public record EjemplarRespuestaDTO(
    String codigo,
    EstadoEjemplarEnum estado
) {}
