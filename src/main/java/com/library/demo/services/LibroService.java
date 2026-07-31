package com.library.demo.services;

import com.library.demo.dtos.EjemplarCreacionDTO;
import com.library.demo.dtos.LibroCreacionDTO;
import com.library.demo.dtos.LibroRespuestaDTO;
import com.library.demo.enums.EstadoEjemplarEnum;
import com.library.demo.models.Ejemplar;
import com.library.demo.models.Libro;
import com.library.demo.repositories.LibroRepository;

import jakarta.transaction.Transactional;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class LibroService {

    private final LibroRepository libroRepository;

    public LibroService(LibroRepository libroRepository) {
        this.libroRepository = libroRepository;
    }

    public List<LibroRespuestaDTO> obtenerTodos() {
        return libroRepository.findAll().stream()
                .map(libro -> new LibroRespuestaDTO(
                        libro.getId(),
                        libro.getTitulo(),
                        libro.getIsbn(),
                        libro.getEdicion(),
                        libro.getFecha_publicacion(),
                        libro.getAutor(),
                        libro.getEjemplares().stream()
                                .map(e -> new com.library.demo.dtos.EjemplarRespuestaDTO(e.getCodigo(), e.getEstado()))
                                .toList()
                ))
                .toList();
    }

    public Optional<Libro> buscarPorId(Long id) {
        return libroRepository.findById(id);
    }

    public Optional<LibroRespuestaDTO> obtenerPorId(Long id) {
        return libroRepository.findById(id)
                .map(libro -> new LibroRespuestaDTO(
                        libro.getId(),
                        libro.getTitulo(),
                        libro.getIsbn(),
                        libro.getEdicion(),
                        libro.getFecha_publicacion(),
                        libro.getAutor(),
                        libro.getEjemplares().stream()
                                .map(e -> new com.library.demo.dtos.EjemplarRespuestaDTO(e.getCodigo(), e.getEstado()))
                                .toList()
                ));
    }

    public Optional<Libro> actualizar(Long id, Libro libroDetalles) {
        return libroRepository.findById(id).map(libroExistente -> {
            libroExistente.setTitulo(libroDetalles.getTitulo());
            libroExistente.setIsbn(libroDetalles.getIsbn());
            libroExistente.setEdicion(libroDetalles.getEdicion());
            libroExistente.setFecha_publicacion(libroDetalles.getFecha_publicacion());
            libroExistente.setAutor(libroDetalles.getAutor());

            return libroRepository.save(libroExistente);
        });
    }

    @Transactional
    public LibroRespuestaDTO guardar(LibroCreacionDTO dto) {

        Libro libro = new Libro();
        libro.setTitulo(dto.titulo());
        libro.setIsbn(dto.isbn());
        libro.setEdicion(dto.edicion());
        libro.setFecha_publicacion(dto.fecha_publicacion());
        libro.setAutor(dto.autor());

        List<Ejemplar> ejemplares = new ArrayList<>();

        if (dto.ejemplares() != null) {
            for (EjemplarCreacionDTO ejemplarDto : dto.ejemplares()) {
                Ejemplar ejemplar = new Ejemplar();
                ejemplar.setCodigo(ejemplarDto.codigo());
                ejemplar.setEstado(EstadoEjemplarEnum.valueOf(ejemplarDto.estado().name()));
                ejemplar.setLibro(libro);

                ejemplares.add(ejemplar);
            }
        }

        libro.setEjemplares(ejemplares);

        Libro libroGuardado = libroRepository.save(libro);
        return new LibroRespuestaDTO(
                libroGuardado.getId(),
                libroGuardado.getTitulo(),
                libroGuardado.getIsbn(),
                libroGuardado.getEdicion(),
                libroGuardado.getFecha_publicacion(),
                libroGuardado.getAutor(),
                libroGuardado.getEjemplares().stream()
                        .map(e -> new com.library.demo.dtos.EjemplarRespuestaDTO(e.getCodigo(), e.getEstado()))
                        .toList()
        );
    }
    
    public void eliminar(Long id) {
        libroRepository.deleteById(id);
    }
}