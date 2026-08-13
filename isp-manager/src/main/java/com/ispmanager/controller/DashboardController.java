package com.ispmanager.controller;

import com.ispmanager.service.ClienteService;
import com.ispmanager.service.FaturaService;
import com.ispmanager.service.PlanoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    @Autowired private ClienteService clienteService;
    @Autowired private FaturaService faturaService;
    @Autowired private PlanoService planoService;

    @GetMapping("/")
    public String dashboard(Model model) {
        model.addAttribute("currentPage", "dashboard");
        model.addAttribute("totalClientes", clienteService.contarAtivos());
        model.addAttribute("totalPlanos", planoService.listarAtivos().size());
        model.addAttribute("faturasPendentes", faturaService.contarPendentes());
        model.addAttribute("faturasVencidas", faturaService.contarVencidas());
        model.addAttribute("totalRecebidoMes", faturaService.totalRecebidoMes());
        model.addAttribute("faturasPendentesLista", faturaService.listarPendentes().stream().limit(5).toList());
        return "dashboard";
    }
}
