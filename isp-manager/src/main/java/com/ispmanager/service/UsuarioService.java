package com.ispmanager.service;

import com.ispmanager.model.Usuario;
import com.ispmanager.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<Usuario> listar() {
        return usuarioRepository.findAll();
    }

    public Usuario buscar(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado."));
    }

    public boolean usernameExiste(String username, Long id) {
        return usuarioRepository.findByUsername(username)
                .filter(usuario -> id == null || !usuario.getId().equals(id))
                .isPresent();
    }

    public Usuario salvar(Usuario usuario, String senha) {
        if (usuario.getId() == null) {
            if (senha == null || senha.isBlank()) {
                throw new IllegalArgumentException("A senha é obrigatória para novos usuários.");
            }
            usuario.setPassword(passwordEncoder.encode(senha));
            if (usuario.getAtivo() == null) {
                usuario.setAtivo(true);
            }
            if (usuario.getRole() == null || usuario.getRole().isBlank()) {
                usuario.setRole("USER");
            }
        } else {
            Usuario existente = buscar(usuario.getId());

            existente.setUsername(usuario.getUsername());
            existente.setNomeCompleto(usuario.getNomeCompleto());
            existente.setRole(usuario.getRole());
            existente.setAtivo(usuario.getAtivo() != null ? usuario.getAtivo() : true);

            if (senha != null && !senha.isBlank()) {
                existente.setPassword(passwordEncoder.encode(senha));
            }

            return usuarioRepository.save(existente);
        }

        return usuarioRepository.save(usuario);
    }

    public void alternarStatus(Long id) {
        Usuario usuario = buscar(id);
        usuario.setAtivo(!Boolean.TRUE.equals(usuario.getAtivo()));
        usuarioRepository.save(usuario);
    }
}
