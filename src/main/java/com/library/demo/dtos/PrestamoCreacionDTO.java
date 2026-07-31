package com.library.demo.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record PrestamoCreacionDTO(
    @NotNull(message = "El id de usuario es obligatorio")
    Long usuario_id,

    @NotNull(message = "El id del libro es obligatorio")
    Long libro_id,

    @NotBlank(message = "El código del libro es obligatorio")
    String libro_code,

    @NotNull(message = "La fecha de préstamo es obligatoria")
    LocalDate fecha_prestamo,
    
    @NotNull(message = "La fecha de devolución es obligatoria")
    LocalDate fecha_devolucion
) {}
