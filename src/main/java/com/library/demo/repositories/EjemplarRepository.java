package com.library.demo.repositories;

import com.library.demo.models.Ejemplar;

import java.util.Optional;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EjemplarRepository extends JpaRepository<Ejemplar, Long> {
    Optional<Ejemplar> findByCodigoAndLibroId(String codigo, Long libroId);
    List<Ejemplar> findByLibroIsbn(String isbn);
}