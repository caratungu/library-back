package com.library.demo.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import java.time.LocalDate;
import java.util.List;

public record LibroCreacionDTO(
    @NotBlank(message = "El título es obligatorio")
    String titulo,

    @NotBlank(message = "El número ISBN es obligatorio")
    String isbn,

    @NotBlank(message = "La edición es obligatoria")
    String edicion,
    
    @NotNull(message = "La fecha de publicación es obligatoria")
    @Past(message = "La fecha de publicación debe ser en el pasado")
    LocalDate fecha_publicacion,

    @NotBlank(message = "El autor es obligatorio")
    String autor,

    @NotNull(message = "La cantidad de ejemplares es obligatoria")
    List<EjemplarCreacionDTO> ejemplares
) {}
