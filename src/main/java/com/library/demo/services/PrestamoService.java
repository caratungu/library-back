package com.library.demo.services;

import com.library.demo.dtos.PrestamoCreacionDTO;
import com.library.demo.enums.EstadoEjemplarEnum;
import com.library.demo.enums.EstadoPrestamoEnum;
import com.library.demo.models.Ejemplar;
import com.library.demo.models.Libro;
import com.library.demo.repositories.EjemplarRepository;
import com.library.demo.repositories.LibroRepository;
import com.library.demo.models.Prestamo;
import com.library.demo.models.Usuario;
import com.library.demo.repositories.PrestamoRepository;
import com.library.demo.repositories.UsuarioRepository;

import jakarta.transaction.Transactional;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PrestamoService {

    private final PrestamoRepository prestamoRepository;
    private final UsuarioRepository usuarioRepository;
    private final LibroRepository libroRepository;
    private final EjemplarRepository ejemplarRepository;
    private final EjemplarService ejemplarService;

    public PrestamoService(PrestamoRepository prestamoRepository, UsuarioRepository usuarioRepository, LibroRepository libroRepository, EjemplarRepository ejemplarRepository, EjemplarService ejemplarService) {
        this.prestamoRepository = prestamoRepository;
        this.usuarioRepository = usuarioRepository;
        this.libroRepository = libroRepository;
        this.ejemplarRepository = ejemplarRepository;
        this.ejemplarService = ejemplarService;
    }

    public List<Prestamo> obtenerTodosPorUsuario(Long userId) {
        return prestamoRepository.findByUsuarioId(userId);
    }

    public List<Prestamo> obtenerTodosPorLibro(Long libroId) {
        return prestamoRepository.findByLibroId(libroId);
    }

    public Optional<Prestamo> obtenerPorId(Long id) {
        return prestamoRepository.findById(id);
    }

    public Optional<Prestamo> actualizar(Long id, Prestamo prestamoDetalles) {
        return prestamoRepository.findById(id).map(prestamoExistente -> {
            if (prestamoDetalles.getEstadoPrestamo() != null) {
                prestamoExistente.setEstadoPrestamo(EstadoPrestamoEnum.valueOf(prestamoDetalles.getEstadoPrestamo().name()));
            }
            if (prestamoDetalles.getFecha_prestamo() != null) {
                prestamoExistente.setFecha_prestamo(prestamoDetalles.getFecha_prestamo());
            }
            if (prestamoDetalles.getFecha_devolucion() != null) {
                prestamoExistente.setFecha_devolucion(prestamoDetalles.getFecha_devolucion());
            }

            return prestamoRepository.save(prestamoExistente);
        });
    }

    @Transactional
    public Prestamo guardar(PrestamoCreacionDTO dto) {

        Usuario usuario = usuarioRepository.findById(dto.usuario_id())
                .orElseThrow(
                        () -> new IllegalArgumentException("Usuario con ID " + dto.usuario_id() + " no encontrado"));

        Libro libro = libroRepository.findById(dto.libro_id())
                .orElseThrow(() -> new IllegalArgumentException("Libro con ID " + dto.libro_id() + " no encontrado"));

        Ejemplar ejemplar = ejemplarRepository.findByCodigoAndLibroId(dto.libro_code(), dto.libro_id())
                .orElseThrow(() -> new IllegalArgumentException("Ejemplar con código " + dto.libro_code() + " no encontrado para el libro con ID " + dto.libro_id()));

        if (ejemplar.getEstado() != EstadoEjemplarEnum.DISPONIBLE) {
            throw new IllegalArgumentException("El ejemplar con código " + dto.libro_code() + " no está disponible para préstamo");
        }

        List<Prestamo> prestamosActivos = prestamoRepository.findByUsuarioId(usuario.getId());
        prestamosActivos = prestamosActivos.stream()
                .filter(prestamo -> prestamo.getEstadoPrestamo() == EstadoPrestamoEnum.ACTIVO)
                .filter(prestamo -> prestamo.getLibro().getId().equals(libro.getId()))
                .toList();

        if (!prestamosActivos.isEmpty()) {
            throw new IllegalArgumentException("El usuario con ID " + usuario.getId() + " ya tiene un préstamo activo para el libro con ID " + libro.getId());
        }

        Prestamo nuevoPrestamo = new Prestamo();
        nuevoPrestamo.setUsuario(usuario);
        nuevoPrestamo.setLibro(libro);
        nuevoPrestamo.setEjemplar_codigo(ejemplar.getCodigo());
        nuevoPrestamo.setFecha_prestamo(dto.fecha_prestamo());
        nuevoPrestamo.setFecha_devolucion(dto.fecha_devolucion());
        nuevoPrestamo.setEstadoPrestamo(EstadoPrestamoEnum.ACTIVO);

        ejemplarService.marcarComoPrestado(ejemplar.getId());
        return prestamoRepository.save(nuevoPrestamo);
    }

    @Scheduled(cron = "0 1 0 * * *") // Ejecutar todos los días a las 00:01 horas
    public void actualizarEstadoPrestamo() {
        List<Prestamo> prestamosActivos = prestamoRepository.findByEstadoPrestamo(EstadoPrestamoEnum.ACTIVO);

        for (Prestamo prestamo : prestamosActivos) {
            if (prestamo.getFecha_devolucion().isBefore(java.time.LocalDate.now())) {
                prestamo.setEstadoPrestamo(EstadoPrestamoEnum.EN_ATRASO);
                prestamoRepository.save(prestamo);
            }
        }
    }
}