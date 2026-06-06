package com.ispmanager.repository;

import com.ispmanager.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    List<Cliente> findByAtivoTrue();

    List<Cliente> findByNomeContainingIgnoreCaseAndAtivoTrue(String nome);

    @Query("SELECT c FROM Cliente c LEFT JOIN FETCH c.plano WHERE c.ativo = true ORDER BY c.nome")
    List<Cliente> findAllAtivosComPlano();

    @Query("SELECT c FROM Cliente c LEFT JOIN FETCH c.plano WHERE c.nome LIKE %:nome% AND c.ativo = true")
    List<Cliente> buscarPorNome(@Param("nome") String nome);

    boolean existsByCpf(String cpf);
}
