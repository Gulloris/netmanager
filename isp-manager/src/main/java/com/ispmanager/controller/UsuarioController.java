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

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("usuarios", usuarioService.listar());
        model.addAttribute("currentPage", "usuarios");
        return "usuarios/lista";
    }

    @GetMapping("/novo")
    public String novo(Model model) {
        Usuario usuario = new Usuario();
        usuario.setAtivo(true);
        usuario.setRole("USER");
        model.addAttribute("usuario", usuario);
        model.addAttribute("currentPage", "usuarios");
        return "usuarios/form";
    }

    @PostMapping("/novo")
    public String criar(@ModelAttribute Usuario usuario,
                        @RequestParam String senha,
                        @RequestParam String confirmarSenha,
                        Model model) {

        if (usuarioService.usernameExiste(usuario.getUsername(), null)) {
            model.addAttribute("erro", "Este nome de usuário já está cadastrado.");
            model.addAttribute("usuario", usuario);
            model.addAttribute("currentPage", "usuarios");
            return "usuarios/form";
        }

        if (senha == null || senha.length() < 6) {
            model.addAttribute("erro", "A senha deve possuir pelo menos 6 caracteres.");
            model.addAttribute("usuario", usuario);
            model.addAttribute("currentPage", "usuarios");
            return "usuarios/form";
        }

        if (!senha.equals(confirmarSenha)) {
            model.addAttribute("erro", "As senhas não conferem.");
            model.addAttribute("usuario", usuario);
            model.addAttribute("currentPage", "usuarios");
            return "usuarios/form";
        }

        try {
            usuarioService.salvar(usuario, senha);
            return "redirect:/usuarios?sucesso=criado";
        } catch (IllegalArgumentException e) {
            model.addAttribute("erro", e.getMessage());
            model.addAttribute("usuario", usuario);
            model.addAttribute("currentPage", "usuarios");
            return "usuarios/form";
        }
    }

    @GetMapping("/{id}/editar")
    public String editar(@PathVariable Long id, Model model) {
        model.addAttribute("usuario", usuarioService.buscar(id));
        model.addAttribute("currentPage", "usuarios");
        return "usuarios/form";
    }

    @PostMapping("/{id}/editar")
    public String atualizar(@PathVariable Long id,
                            @ModelAttribute Usuario usuario,
                            @RequestParam String senha,
                            @RequestParam String confirmarSenha,
                            Model model) {

        if (usuarioService.usernameExiste(usuario.getUsername(), id)) {
            model.addAttribute("erro", "Este nome de usuário já está cadastrado.");
            usuario.setId(id);
            model.addAttribute("usuario", usuario);
            model.addAttribute("currentPage", "usuarios");
            return "usuarios/form";
        }

        if (senha != null && !senha.isBlank()) {
            if (senha.length() < 6) {
                model.addAttribute("erro", "A nova senha deve possuir pelo menos 6 caracteres.");
                usuario.setId(id);
                model.addAttribute("usuario", usuario);
                model.addAttribute("currentPage", "usuarios");
                return "usuarios/form";
            }
            if (!senha.equals(confirmarSenha)) {
                model.addAttribute("erro", "As senhas não conferem.");
                usuario.setId(id);
                model.addAttribute("usuario", usuario);
                model.addAttribute("currentPage", "usuarios");
                return "usuarios/form";
            }
        }

        usuario.setId(id);
        try {
            usuarioService.salvar(usuario, senha);
            return "redirect:/usuarios?sucesso=atualizado";
        } catch (IllegalArgumentException e) {
            model.addAttribute("erro", e.getMessage());
            model.addAttribute("usuario", usuario);
            model.addAttribute("currentPage", "usuarios");
            return "usuarios/form";
        }
    }

    @PostMapping("/{id}/status")
    public String alternarStatus(@PathVariable Long id, Authentication authentication) {
        Usuario usuario = usuarioService.buscar(id);

        // Evita que o administrador desative a própria conta.
        if (usuario.getUsername().equals(authentication.getName())) {
            return "redirect:/usuarios?erro=proprio";
        }

        usuarioService.alternarStatus(id);
        return "redirect:/usuarios?sucesso=status";
    }
}
