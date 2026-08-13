package com.ispmanager.repository;

import com.ispmanager.model.Plano;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlanoRepository extends JpaRepository<Plano, Long> {
    List<Plano> findByAtivoTrue();
    List<Plano> findByAtivoTrueOrderByValorAsc();
}
