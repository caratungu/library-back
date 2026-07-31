package com.library.demo.controllers;

import com.library.demo.dtos.PrestamoCreacionDTO;
import com.library.demo.models.Prestamo;
import com.library.demo.services.PrestamoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/api/prestamos")
@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {
    RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS
})
public class PrestamoController {

    private final PrestamoService prestamoService;

    public PrestamoController(PrestamoService prestamoService) {
        this.prestamoService = prestamoService;
    }

    // GET: http://localhost:8080/api/prestamos/por-libro/1
    @GetMapping("/por-libro/{libro_id}")
    public List<Prestamo> obtenerTodosPorLibro(@PathVariable Long libro_id) {
        return prestamoService.obtenerTodosPorLibro(libro_id);
    }

    // GET: http://localhost:8080/api/prestamos/por-usuario/1
    @GetMapping("/por-usuario/{usuario_id}")
    public List<Prestamo> obtenerTodosPorUsuario(@PathVariable Long usuario_id) {
        return prestamoService.obtenerTodosPorUsuario(usuario_id);
    }

    // GET por ID: http://localhost:8080/api/prestamos/1
    @GetMapping("/{id}")
    public ResponseEntity<Prestamo> obtenerPorId(@PathVariable Long id) {
        return prestamoService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // POST: http://localhost:8080/api/prestamos
    @PostMapping
    public ResponseEntity<Prestamo> crear(@Valid @RequestBody PrestamoCreacionDTO dto) {
        Prestamo nuevoPrestamo = prestamoService.guardar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoPrestamo);
    }

    // PUT: http://localhost:8080/api/prestamos/1
    @PutMapping("/{id}")
    public ResponseEntity<Prestamo> actualizar(@PathVariable Long id, @RequestBody Prestamo prestamoDetalles) {
        return prestamoService.actualizar(id, prestamoDetalles)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}