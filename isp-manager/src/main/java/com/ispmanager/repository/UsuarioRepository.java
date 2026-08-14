package com.ispmanager.repository;

import com.ispmanager.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByUsername(String username);

    List<Usuario> findAllByOrderByNomeCompletoAsc();

    List<Usuario> findByRoleOrderByNomeCompletoAsc(String role);

    List<Usuario> findByAtivoOrderByNomeCompletoAsc(Boolean ativo);
}