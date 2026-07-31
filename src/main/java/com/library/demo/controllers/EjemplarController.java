package com.library.demo.controllers;

import com.library.demo.dtos.EjemplarCreacionDTO;
import com.library.demo.models.Ejemplar;
import com.library.demo.services.EjemplarService;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/ejemplares")
@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {
    RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS
})
public class EjemplarController {

    private final EjemplarService ejemplarService;

    public EjemplarController(EjemplarService ejemplarService) {
        this.ejemplarService = ejemplarService;
    }

    // GET: http://localhost:8080/api/ejemplares/325-258/disponibles
    @GetMapping("/{isbn}/disponibles")
    public ResponseEntity<List<Ejemplar>> obtenerDisponiblesPorLibroISBN(@PathVariable String isbn) {
        List<Ejemplar> ejemplaresDisponibles = ejemplarService.obtenerDisponiblesPorLibroISBN(isbn);
        return ResponseEntity.ok(ejemplaresDisponibles);
    }

    // POST: http://localhost:8080/api/ejemplares/libro/{libroId}
    @PostMapping("/libro/{libroId}")
    public ResponseEntity<String> crear(@PathVariable Long libroId, @Valid @RequestBody EjemplarCreacionDTO dto) {
        String nuevoEjemplar = ejemplarService.guardar(libroId, dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoEjemplar);
    }

    // DELETE: http://localhost:8080/api/ejemplares/1
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        ejemplarService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    // PUT: http://localhost:8080/api/ejemplares/1
    @PutMapping("/{id}")
    public ResponseEntity<Ejemplar> actualizar(@PathVariable Long id, @RequestBody Ejemplar ejemplarDetalles) {
        return ejemplarService.actualizar(id, ejemplarDetalles)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}