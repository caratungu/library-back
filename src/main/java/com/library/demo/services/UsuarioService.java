package com.library.demo.services;

import com.library.demo.dtos.UsuarioCreacionDTO;
import com.library.demo.models.Usuario;
import com.library.demo.repositories.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public List<Usuario> obtenerTodos() {
        return usuarioRepository.findAll();
    }

    public Optional<Usuario> obtenerPorId(Long id) {
        return usuarioRepository.findById(id);
    }

    public Optional<Usuario> actualizar(Long id, Usuario usuarioDetalles) {
        return usuarioRepository.findById(id).map(usuarioExistente -> {
            usuarioExistente.setNombre(usuarioDetalles.getNombre());
            usuarioExistente.setApellido(usuarioDetalles.getApellido());
            usuarioExistente.setEmail(usuarioDetalles.getEmail());
            usuarioExistente.setFecha_nacimiento(usuarioDetalles.getFecha_nacimiento());

            return usuarioRepository.save(usuarioExistente);
        });
    }

    public Usuario guardar(UsuarioCreacionDTO dto) {
        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setNombre(dto.nombre());
        nuevoUsuario.setApellido(dto.apellido());
        nuevoUsuario.setEmail(dto.email());
        nuevoUsuario.setFecha_nacimiento(dto.fecha_nacimiento());
        return usuarioRepository.save(nuevoUsuario);
    }

    public void eliminar(Long id) {
        usuarioRepository.deleteById(id);
    }
}