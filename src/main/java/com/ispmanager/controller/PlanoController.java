package com.ispmanager.controller;

import com.ispmanager.model.Plano;
import com.ispmanager.service.PlanoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/planos")
public class PlanoController {

    @Autowired private PlanoService planoService;

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("planos", planoService.listarTodos());
        return "planos/lista";
    }

    @GetMapping("/novo")
    public String formularioNovo(Model model) {
        model.addAttribute("plano", new Plano());
        return "planos/form";
    }

    @PostMapping("/novo")
    public String salvar(@Valid @ModelAttribute Plano plano, BindingResult result,
                         Model model, RedirectAttributes ra) {
        if (result.hasErrors()) return "planos/form";
        planoService.salvar(plano);
        ra.addFlashAttribute("sucesso", "Plano cadastrado com sucesso!");
        return "redirect:/planos";
    }

    @GetMapping("/{id}/editar")
    public String formularioEditar(@PathVariable Long id, Model model) {
        Plano plano = planoService.buscarPorId(id)
                .orElseThrow(() -> new RuntimeException("Plano não encontrado"));
        model.addAttribute("plano", plano);
        return "planos/form";
    }

    @PostMapping("/{id}/editar")
    public String atualizar(@PathVariable Long id, @Valid @ModelAttribute Plano plano,
                            BindingResult result, Model model, RedirectAttributes ra) {
        if (result.hasErrors()) return "planos/form";
        planoService.atualizar(id, plano);
        ra.addFlashAttribute("sucesso", "Plano atualizado com sucesso!");
        return "redirect:/planos";
    }

    @PostMapping("/{id}/inativar")
    public String inativar(@PathVariable Long id, RedirectAttributes ra) {
        planoService.inativar(id);
        ra.addFlashAttribute("sucesso", "Plano inativado com sucesso!");
        return "redirect:/planos";
    }
}
