package com.ispmanager.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "planos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Plano {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Nome do plano é obrigatório")
    @Column(nullable = false)
    private String nome;

    @Column(columnDefinition = "TEXT")
    private String descricao;

    @NotNull(message = "Velocidade é obrigatória")
    @Column(name = "velocidade_mbps", nullable = false)
    private Integer velocidadeMbps;

    @NotNull(message = "Valor é obrigatório")
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal valor;

    @Column(name = "ativo")
    private Boolean ativo = true;

    @Column(name = "criado_em")
    private LocalDateTime criadoEm;

    @OneToMany(mappedBy = "plano", fetch = FetchType.LAZY)
    private List<Cliente> clientes;

    @PrePersist
    protected void onCreate() {
        criadoEm = LocalDateTime.now();
    }
}
