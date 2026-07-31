package com.library.demo.exceptions;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 1. Manejo de validaciones normales (@NotBlank, @NotNull, etc.)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(
            MethodArgumentNotValidException ex) {

        Map<String, String> errores = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String campo = ((FieldError) error).getField();
            String mensaje = error.getDefaultMessage();
            errores.put(campo, mensaje);
        });

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errores);
    }

    // 2. Manejo de errores de conversión/deserialización del JSON (Valores de Enum inválidos, etc.)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, String>> handleHttpMessageNotReadable(
            HttpMessageNotReadableException ex) {

        Map<String, String> respuesta = new HashMap<>();

        // Verificamos si la causa raíz es una mala conversión de tipo (como en un Enum)
        if (ex.getCause() instanceof InvalidFormatException invalidFormatException) {

            // Si el campo problemático es un Enum
            if (invalidFormatException.getTargetType() != null && invalidFormatException.getTargetType().isEnum()) {

                String campo = invalidFormatException.getPath().get(0).getFieldName();

                // Obtenemos los valores permitidos del Enum dinámicamente
                Object[] valoresPermitidos = invalidFormatException.getTargetType().getEnumConstants();

                String mensaje = "El valor '" + invalidFormatException.getValue() +
                        "' no es válido. Los valores permitidos son: " +
                        Arrays.toString(valoresPermitidos);

                respuesta.put(campo, mensaje);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(respuesta);
            }
        }

        // Para cualquier otro error de lectura del JSON (ejemplo: JSON mal formado)
        respuesta.put("error", "El cuerpo de la petición (JSON) no tiene un formato válido");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(respuesta);
    }

    // 3. Manejo de IllegalArgumentException
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleIllegalArgumentException(IllegalArgumentException ex) {
        Map<String, String> respuesta = new HashMap<>();
        respuesta.put("error", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(respuesta);
    }
}