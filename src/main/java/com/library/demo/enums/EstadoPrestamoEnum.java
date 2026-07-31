package com.library.demo.enums;

public enum EstadoPrestamoEnum {
    SOLICITADO,    // El usuario pidió el préstamo, pero aún no se le entrega el ejemplar.
    ACTIVO,        // El ejemplar está prestado y el plazo de devolución está vigente.
    DEVUELTO,      // El ejemplar fue regresado correctamente a tiempo.
    EN_ATRASO,     // El plazo límite venció y el usuario aún no ha devuelto el ejemplar.
    CANCELADO,     // La solicitud de préstamo fue cancelada antes de entregarse.
    PERDIDO        // El ejemplar se reportó como extraviado o dañado durante el préstamo.
}