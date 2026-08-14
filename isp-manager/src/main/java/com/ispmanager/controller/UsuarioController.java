package com.ispmanager.controller;

import com.ispmanager.model.Usuario;
import com.ispmanager.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    /*
     * LISTAGEM
     */
    @GetMapping
    public String listar(
            @RequestParam(required = false) String sucesso,
            @RequestParam(required = false) String erro,
            Model model) {

        model.addAttribute(
                "usuarios",
                usuarioService.listar()
        );

        model.addAttribute(
                "currentPage",
                "usuarios"
        );

        model.addAttribute(
                "sucesso",
                sucesso
        );

        model.addAttribute(
                "erro",
                erro
        );

        return "usuarios/lista";
    }

    /*
     * NOVO USUÁRIO
     */
    @GetMapping("/novo")
    public String novo(Model model) {

        Usuario usuario = new Usuario();

        usuario.setAtivo(true);
        usuario.setRole("USER");

        model.addAttribute(
                "usuario",
                usuario
        );

        model.addAttribute(
                "currentPage",
                "usuarios"
        );

        return "usuarios/form";
    }

    /*
     * CRIAR USUÁRIO
     */
    @PostMapping("/novo")
    public String criar(
            @ModelAttribute Usuario usuario,
            @RequestParam String senha,
            @RequestParam String confirmarSenha,
            Model model) {

        if (usuarioService.usernameExiste(
                usuario.getUsername(),
                null)) {

            return erroFormulario(
                    usuario,
                    "Este nome de usuário já está cadastrado.",
                    model
            );
        }

        if (senha == null || senha.length() < 6) {

            return erroFormulario(
                    usuario,
                    "A senha deve possuir pelo menos 6 caracteres.",
                    model
            );
        }

        if (!senha.equals(confirmarSenha)) {

            return erroFormulario(
                    usuario,
                    "As senhas não conferem.",
                    model
            );
        }

        try {

            usuarioService.salvar(
                    usuario,
                    senha
            );

            return "redirect:/usuarios?sucesso=criado";

        } catch (IllegalArgumentException e) {

            return erroFormulario(
                    usuario,
                    e.getMessage(),
                    model
            );
        }
    }

    /*
     * EDITAR
     */
    @GetMapping("/{id}/editar")
    public String editar(
            @PathVariable Long id,
            Model model) {

        model.addAttribute(
                "usuario",
                usuarioService.buscar(id)
        );

        model.addAttribute(
                "currentPage",
                "usuarios"
        );

        return "usuarios/form";
    }

    /*
     * ATUALIZAR
     */
    @PostMapping("/{id}/editar")
    public String atualizar(
            @PathVariable Long id,
            @ModelAttribute Usuario usuario,
            @RequestParam(required = false) String senha,
            @RequestParam(required = false) String confirmarSenha,
            Model model) {

        if (usuarioService.usernameExiste(
                usuario.getUsername(),
                id)) {

            usuario.setId(id);

            return erroFormulario(
                    usuario,
                    "Este nome de usuário já está cadastrado.",
                    model
            );
        }

        /*
         * Se informou nova senha,
         * valida a confirmação.
         */
        if (senha != null && !senha.isBlank()) {

            if (senha.length() < 6) {

                usuario.setId(id);

                return erroFormulario(
                        usuario,
                        "A nova senha deve possuir pelo menos 6 caracteres.",
                        model
                );
            }

            if (!senha.equals(confirmarSenha)) {

                usuario.setId(id);

                return erroFormulario(
                        usuario,
                        "As senhas não conferem.",
                        model
                );
            }
        }

        usuario.setId(id);

        try {

            usuarioService.salvar(
                    usuario,
                    senha
            );

            return "redirect:/usuarios?sucesso=atualizado";

        } catch (IllegalArgumentException e) {

            usuario.setId(id);

            return erroFormulario(
                    usuario,
                    e.getMessage(),
                    model
            );
        }
    }

    /*
     * ATIVAR / DESATIVAR
     */
    @PostMapping("/{id}/status")
    public String alternarStatus(
            @PathVariable Long id,
            Authentication authentication) {

        Usuario usuario =
                usuarioService.buscar(id);

        /*
         * Impede o administrador de
         * desativar a própria conta.
         */
        if (usuario.getUsername()
                .equals(authentication.getName())) {

            return "redirect:/usuarios?erro=proprio";
        }

        usuarioService.alternarStatus(id);

        return "redirect:/usuarios?sucesso=status";
    }

    /*
     * Método auxiliar para retornar
     * ao formulário com mensagem de erro.
     */
    private String erroFormulario(
            Usuario usuario,
            String mensagem,
            Model model) {

        model.addAttribute(
                "erro",
                mensagem
        );

        model.addAttribute(
                "usuario",
                usuario
        );

        model.addAttribute(
                "currentPage",
                "usuarios"
        );

        return "usuarios/form";
    }
}