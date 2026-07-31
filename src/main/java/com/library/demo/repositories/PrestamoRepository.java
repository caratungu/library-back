package com.library.demo.repositories;

import com.library.demo.enums.EstadoPrestamoEnum;
import com.library.demo.models.Prestamo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PrestamoRepository extends JpaRepository<Prestamo, Long> {
    List<Prestamo> findByUsuarioId(Long usuarioId);
    List<Prestamo> findByLibroId(Long libroId);
    List<Prestamo> findByEstadoPrestamo(EstadoPrestamoEnum estadoPrestamo);
}