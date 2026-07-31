package com.library.demo.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import com.library.demo.enums.EstadoEjemplarEnum;

public record EjemplarCreacionDTO(
    @NotBlank(message = "El código es obligatorio")
    String codigo,

    @NotNull(message = "El estado es obligatorio")
    EstadoEjemplarEnum estado
) {}
