package com.ispmanager.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "usuarios")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Nome de usuário é obrigatório")
    @Column(nullable = false, unique = true)
    private String username;

    @NotBlank(message = "Senha é obrigatória")
    @Column(nullable = false)
    private String password;

    @Column(name = "nome_completo")
    private String nomeCompleto;

    /*
     * Perfis disponíveis:
     *
     * ADMIN      - acesso total
     * SUPORTE     - clientes e planos
     * FINANCEIRO  - faturas
     * USER        - acesso básico
     */
    @Column(nullable = false)
    private String role = "USER";

    @Column(nullable = false)
    private Boolean ativo = true;

    @Column(name = "criado_em", nullable = false)
    private LocalDateTime criadoEm;

    @PrePersist
    protected void onCreate() {
        if (criadoEm == null) {
            criadoEm = LocalDateTime.now();
        }

        if (role == null || role.isBlank()) {
            role = "USER";
        }

        if (ativo == null) {
            ativo = true;
        }
    }
}