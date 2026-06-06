package com.ispmanager.repository;

import com.ispmanager.model.Fatura;
import com.ispmanager.model.Fatura.StatusFatura;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface FaturaRepository extends JpaRepository<Fatura, Long> {

    @Query("SELECT f FROM Fatura f JOIN FETCH f.cliente c LEFT JOIN FETCH c.plano ORDER BY f.dataVencimento DESC")
    List<Fatura> findAllComCliente();

    @Query("SELECT f FROM Fatura f JOIN FETCH f.cliente c LEFT JOIN FETCH c.plano WHERE f.status = :status ORDER BY f.dataVencimento ASC")
    List<Fatura> findByStatusComCliente(@Param("status") StatusFatura status);

    @Query("SELECT f FROM Fatura f JOIN FETCH f.cliente WHERE f.cliente.id = :clienteId ORDER BY f.dataVencimento DESC")
    List<Fatura> findByClienteId(@Param("clienteId") Long clienteId);

    @Query("SELECT f FROM Fatura f JOIN FETCH f.cliente c WHERE f.dataVencimento <= :hoje AND f.status = 'PENDENTE'")
    List<Fatura> findVencidas(@Param("hoje") LocalDate hoje);

    long countByStatus(StatusFatura status);

    @Query("SELECT COALESCE(SUM(f.valor), 0) FROM Fatura f WHERE f.status = 'PAGO' AND MONTH(f.dataPagamento) = MONTH(CURRENT_DATE) AND YEAR(f.dataPagamento) = YEAR(CURRENT_DATE)")
    java.math.BigDecimal totalRecebidoMesAtual();
}
