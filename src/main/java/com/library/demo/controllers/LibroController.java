package com.library.demo.controllers;

import com.library.demo.dtos.LibroCreacionDTO;
import com.library.demo.dtos.LibroRespuestaDTO;
import com.library.demo.models.Libro;
import com.library.demo.services.LibroService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/api/libros")
@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {
    RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS
})
public class LibroController {

    private final LibroService libroService;

    public LibroController(LibroService libroService) {
        this.libroService = libroService;
    }

    // GET: http://localhost:8080/api/libros
    @GetMapping
    public List<LibroRespuestaDTO> obtenerTodos() {
        return libroService.obtenerTodos();
    }

    // GET por ID: http://localhost:8080/api/libros/1
    @GetMapping("/{id}")
    public ResponseEntity<LibroRespuestaDTO> obtenerPorId(@PathVariable Long id) {
        return libroService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // POST: http://localhost:8080/api/libros
    @PostMapping
    public ResponseEntity<LibroRespuestaDTO> crear(@Valid @RequestBody LibroCreacionDTO dto) {
        LibroRespuestaDTO nuevoLibro = libroService.guardar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoLibro);
    }

    // DELETE: http://localhost:8080/api/libros/1
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        libroService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    // PUT: http://localhost:8080/api/libros/1
    @PutMapping("/{id}")
    public ResponseEntity<Libro> actualizar(@PathVariable Long id, @RequestBody Libro libroDetalles) {
        return libroService.actualizar(id, libroDetalles)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}