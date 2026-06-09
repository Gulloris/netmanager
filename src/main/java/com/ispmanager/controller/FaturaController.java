package com.ispmanager.controller;

import com.ispmanager.model.Cliente;
import com.ispmanager.model.Fatura;
import com.ispmanager.service.ClienteService;
import com.ispmanager.service.FaturaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;

@Controller
@RequestMapping("/faturas")
public class FaturaController {

    @Autowired private FaturaService faturaService;
    @Autowired private ClienteService clienteService;

    @GetMapping
public String listar(@RequestParam(defaultValue = "todas") String filtro,
                     @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicio,
                     @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFim,
                     Model model) {

    boolean temFiltroData = dataInicio != null && dataFim != null;

    if (temFiltroData) {
        switch (filtro) {
            case "pendentes" -> model.addAttribute("faturas", faturaService.listarPorStatusEPeriodo(Fatura.StatusFatura.PENDENTE, dataInicio, dataFim));
            case "vencidas"  -> model.addAttribute("faturas", faturaService.listarPorStatusEPeriodo(Fatura.StatusFatura.VENCIDO, dataInicio, dataFim));
            case "pagas"     -> model.addAttribute("faturas", faturaService.listarPorStatusEPeriodo(Fatura.StatusFatura.PAGO, dataInicio, dataFim));
            default          -> model.addAttribute("faturas", faturaService.listarPorPeriodo(dataInicio, dataFim));
        }
    } else {
        switch (filtro) {
            case "pendentes" -> model.addAttribute("faturas", faturaService.listarPendentes());
            case "vencidas"  -> model.addAttribute("faturas", faturaService.listarVencidas());
            case "pagas"     -> model.addAttribute("faturas", faturaService.listarPagas());
            default          -> model.addAttribute("faturas", faturaService.listarTodas());
        }
    }

    model.addAttribute("currentPage", "faturas");
    model.addAttribute("filtro", filtro);
    model.addAttribute("dataInicio", dataInicio);
    model.addAttribute("dataFim", dataFim);
    model.addAttribute("qtdPendentes", faturaService.contarPendentes());
    model.addAttribute("qtdVencidas", faturaService.contarVencidas());
    return "faturas/lista";
}

    @GetMapping("/nova")
    public String formularioNovo(Model model) {
        model.addAttribute("fatura", new Fatura());
        model.addAttribute("clientes", clienteService.listarTodos());
        return "faturas/form";
    }

 @PostMapping("/nova")
public String salvar(@RequestParam Long clienteId,
                     @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataVencimento,
                     @RequestParam java.math.BigDecimal valor,
                     @RequestParam(required = false) String referenciaMes,
                     @RequestParam(required = false) String observacao,
                     @RequestParam(required = false) Boolean gerarAnual,
                     RedirectAttributes ra) {
    Cliente cliente = clienteService.buscarPorId(clienteId)
            .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));

    faturaService.salvarComOpcaoAnual(cliente, dataVencimento, valor, referenciaMes, observacao, gerarAnual);

    ra.addFlashAttribute("sucesso", Boolean.TRUE.equals(gerarAnual)
            ? "12 faturas geradas com sucesso!"
            : "Fatura criada com sucesso!");
    return "redirect:/faturas";
}

    @PostMapping("/{id}/baixa")
    public String darBaixa(@PathVariable Long id,
                           @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataPagamento,
                           RedirectAttributes ra) {
        faturaService.darBaixa(id, dataPagamento);
        ra.addFlashAttribute("sucesso", "Pagamento registrado com sucesso!");
        return "redirect:/faturas";
    }
    @PostMapping("/{id}/estornar")
public String estornar(@PathVariable Long id, RedirectAttributes ra) {
    faturaService.estornar(id);
    ra.addFlashAttribute("sucesso", "Pagamento estornado com sucesso!");
    return "redirect:/faturas";
}

    @PostMapping("/{id}/cancelar")
    public String cancelar(@PathVariable Long id, RedirectAttributes ra) {
        faturaService.cancelar(id);
        ra.addFlashAttribute("sucesso", "Fatura cancelada.");
        return "redirect:/faturas";
    }

    @PostMapping("/gerar/{clienteId}")
    public String gerarFatura(@PathVariable Long clienteId, RedirectAttributes ra) {
        faturaService.gerarFaturaParaCliente(clienteId);
        ra.addFlashAttribute("sucesso", "Fatura gerada com sucesso!");
        return "redirect:/faturas";
    }
}
