package com.library.demo.services;

import com.library.demo.dtos.EjemplarCreacionDTO;
import com.library.demo.enums.EstadoEjemplarEnum;
import com.library.demo.models.Ejemplar;
import com.library.demo.models.Libro;
import com.library.demo.repositories.EjemplarRepository;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EjemplarService {

    private final EjemplarRepository ejemplarRepository;

    private final LibroService libroService;

    public EjemplarService(EjemplarRepository ejemplarRepository, LibroService libroService) {
        this.ejemplarRepository = ejemplarRepository;
        this.libroService = libroService;
    }

    public List<Ejemplar> obtenerTodos() {
        return ejemplarRepository.findAll();
    }

    public Optional<Ejemplar> obtenerPorId(Long id) {
        return ejemplarRepository.findById(id);
    }

    public Optional<Ejemplar> actualizar(Long id, Ejemplar ejemplarDetalles) {
        return ejemplarRepository.findById(id).map(ejemplarExistente -> {
            if (ejemplarDetalles.getCodigo() != null) {
                ejemplarExistente.setCodigo(ejemplarDetalles.getCodigo());
            }
            if (ejemplarDetalles.getEstado() != null) {
                ejemplarExistente.setEstado(ejemplarDetalles.getEstado());
            }

            return ejemplarRepository.save(ejemplarExistente);
        });
    }

    public String guardar(Long libroId, EjemplarCreacionDTO dto) {

        Libro libro = libroService.buscarPorId(libroId)
                .orElseThrow(() -> new IllegalArgumentException("Libro con ID " + libroId + " no encontrado"));

        Ejemplar ejemplar = new Ejemplar();
        ejemplar.setLibro(libro);
        ejemplar.setCodigo(dto.codigo());
        ejemplar.setEstado(EstadoEjemplarEnum.valueOf(dto.estado().name()));
        ejemplarRepository.save(ejemplar);
        return "Ejemplar creado con éxito";
    }

    public String marcarComoPrestado(Long ejemplarId) {
        Ejemplar ejemplar = ejemplarRepository.findById(ejemplarId)
                .orElseThrow(() -> new IllegalArgumentException("Ejemplar con ID " + ejemplarId + " no encontrado"));

        if (ejemplar.getEstado() == EstadoEjemplarEnum.PRESTADO) {
            throw new IllegalArgumentException("El ejemplar con ID " + ejemplarId + " ya está prestado");
        }

        ejemplar.setEstado(EstadoEjemplarEnum.PRESTADO);
        ejemplarRepository.save(ejemplar);
        return "Ejemplar marcado como prestado con éxito";
    }

    public void eliminar(Long id) {
        ejemplarRepository.deleteById(id);
    }

    public List<Ejemplar> obtenerDisponiblesPorLibroISBN(String isbn) {
        List<Ejemplar> ejemplares = ejemplarRepository.findByLibroIsbn(isbn);
        return ejemplares.stream()
                .filter(ejemplar -> ejemplar.getEstado() == EstadoEjemplarEnum.DISPONIBLE)
                .toList();
    }
}