package com.ispmanager.controller;

import com.ispmanager.model.Cliente;
import com.ispmanager.model.Plano;
import com.ispmanager.service.ClienteService;
import com.ispmanager.service.PlanoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/clientes")
public class ClienteController {

    @Autowired private ClienteService clienteService;
    @Autowired private PlanoService planoService;

    @GetMapping
    public String listar(@RequestParam(required = false) String busca, Model model) {
        List<Cliente> clientes = (busca != null && !busca.isBlank())
                ? clienteService.buscarPorNome(busca)
                : clienteService.listarTodos();
        model.addAttribute("clientes", clientes);
        model.addAttribute("busca", busca);
        model.addAttribute("currentPage", "clientes");
        return "clientes/lista";
    }

    @GetMapping("/novo")
    public String formularioNovo(Model model) {
        model.addAttribute("cliente", new Cliente());
        model.addAttribute("planos", planoService.listarAtivos());
        return "clientes/form";
    }

    @PostMapping("/novo")
    public String salvar(@Valid @ModelAttribute Cliente cliente, BindingResult result,
                         @RequestParam(required = false) Long planoId,
                         Model model, RedirectAttributes ra) {
        if (result.hasErrors()) {
            model.addAttribute("planos", planoService.listarAtivos());
            return "clientes/form";
        }
        if (planoId != null) {
            planoService.buscarPorId(planoId).ifPresent(cliente::setPlano);
        }
        clienteService.salvar(cliente);
        ra.addFlashAttribute("sucesso", "Cliente cadastrado com sucesso!");
        return "redirect:/clientes";
    }

    @GetMapping("/{id}/editar")
    public String formularioEditar(@PathVariable Long id, Model model) {
        Cliente cliente = clienteService.buscarPorId(id)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));
        model.addAttribute("cliente", cliente);
        model.addAttribute("planos", planoService.listarAtivos());
        return "clientes/form";
    }

    @PostMapping("/{id}/editar")
    public String atualizar(@PathVariable Long id, @Valid @ModelAttribute Cliente cliente,
                            BindingResult result, @RequestParam(required = false) Long planoId,
                            Model model, RedirectAttributes ra) {
        if (result.hasErrors()) {
            model.addAttribute("planos", planoService.listarAtivos());
            return "clientes/form";
        }
        if (planoId != null) {
            Plano plano = new Plano();
            plano.setId(planoId);
            cliente.setPlano(plano);
        }
        clienteService.atualizar(id, cliente);
        ra.addFlashAttribute("sucesso", "Cliente atualizado com sucesso!");
        return "redirect:/clientes";
    }

    @PostMapping("/{id}/inativar")
    public String inativar(@PathVariable Long id, RedirectAttributes ra) {
        clienteService.inativar(id);
        ra.addFlashAttribute("sucesso", "Cliente inativado com sucesso!");
        return "redirect:/clientes";
    }

    @GetMapping("/{id}/faturas")
    public String faturas(@PathVariable Long id, Model model) {
        Cliente cliente = clienteService.buscarPorId(id)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));
        model.addAttribute("cliente", cliente);
        return "clientes/faturas";
    }
}
