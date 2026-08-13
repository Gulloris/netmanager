package com.ispmanager.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

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

    @Column(nullable = false)
    private String role = "ADMIN";

    @Column(nullable = false)
    private Boolean ativo = true;

    @Column(name = "criado_em")
    private LocalDateTime criadoEm = LocalDateTime.now();

    @PrePersist
    protected void onCreate() {
        criadoEm = LocalDateTime.now();
    }
}
