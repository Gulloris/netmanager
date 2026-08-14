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
        return usuarioRepository.findAllByOrderByNomeCompletoAsc();
    }

    public Usuario buscar(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException("Usuário não encontrado."));
    }

    public boolean usernameExiste(String username, Long id) {

        if (username == null || username.isBlank()) {
            return false;
        }

        return usuarioRepository.findByUsername(username.trim())
                .filter(usuario ->
                        id == null || !usuario.getId().equals(id))
                .isPresent();
    }

    public Usuario salvar(Usuario usuario, String senha) {

        usuario.setUsername(usuario.getUsername().trim());

        if (usuario.getNomeCompleto() != null) {
            usuario.setNomeCompleto(usuario.getNomeCompleto().trim());
        }

        validarRole(usuario.getRole());

        /*
         * NOVO USUÁRIO
         */
        if (usuario.getId() == null) {

            if (senha == null || senha.isBlank()) {
                throw new IllegalArgumentException(
                        "A senha é obrigatória para novos usuários.");
            }

            if (senha.length() < 6) {
                throw new IllegalArgumentException(
                        "A senha deve possuir pelo menos 6 caracteres.");
            }

            usuario.setPassword(
                    passwordEncoder.encode(senha)
            );

            if (usuario.getAtivo() == null) {
                usuario.setAtivo(true);
            }

            if (usuario.getRole() == null || usuario.getRole().isBlank()) {
                usuario.setRole("USER");
            }

            return usuarioRepository.save(usuario);
        }

        /*
         * EDIÇÃO
         */
        Usuario existente = buscar(usuario.getId());

        existente.setUsername(usuario.getUsername());
        existente.setNomeCompleto(usuario.getNomeCompleto());
        existente.setRole(usuario.getRole());
        existente.setAtivo(
                usuario.getAtivo() != null
                        ? usuario.getAtivo()
                        : true
        );

        /*
         * Só altera a senha se o administrador
         * realmente informou uma nova senha.
         */
        if (senha != null && !senha.isBlank()) {

            if (senha.length() < 6) {
                throw new IllegalArgumentException(
                        "A nova senha deve possuir pelo menos 6 caracteres.");
            }

            existente.setPassword(
                    passwordEncoder.encode(senha)
            );
        }

        return usuarioRepository.save(existente);
    }

    public void alternarStatus(Long id) {

        Usuario usuario = buscar(id);

        usuario.setAtivo(
                !Boolean.TRUE.equals(usuario.getAtivo())
        );

        usuarioRepository.save(usuario);
    }

    private void validarRole(String role) {

        if (role == null || role.isBlank()) {
            return;
        }

        if (!role.equals("ADMIN")
                && !role.equals("SUPORTE")
                && !role.equals("FINANCEIRO")
                && !role.equals("USER")) {

            throw new IllegalArgumentException(
                    "Perfil de usuário inválido."
            );
        }
    }
}