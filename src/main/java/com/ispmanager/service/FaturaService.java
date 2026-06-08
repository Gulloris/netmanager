package com.ispmanager.service;

import com.ispmanager.model.Cliente;
import com.ispmanager.model.Fatura;
import com.ispmanager.model.Fatura.StatusFatura;
import com.ispmanager.repository.ClienteRepository;
import com.ispmanager.repository.FaturaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@Transactional
public class FaturaService {

    @Autowired
    private FaturaRepository faturaRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    public List<Fatura> listarTodas() {
        atualizarVencidas();
        return faturaRepository.findAllComCliente();
    }

    public List<Fatura> listarPendentes() {
        atualizarVencidas();
        return faturaRepository.findByStatusComCliente(StatusFatura.PENDENTE);
    }

    public List<Fatura> listarVencidas() {
        atualizarVencidas();
        return faturaRepository.findByStatusComCliente(StatusFatura.VENCIDO);
    }

    public List<Fatura> listarPagas() {
        return faturaRepository.findByStatusComCliente(StatusFatura.PAGO);
    }

    public List<Fatura> listarPorCliente(Long clienteId) {
        return faturaRepository.findByClienteId(clienteId);
    }

    public Fatura salvar(Fatura fatura) {
        // Formata referência mês/ano automaticamente se não informado
        if (fatura.getReferenciaMes() == null || fatura.getReferenciaMes().isBlank()) {
            fatura.setReferenciaMes(fatura.getDataVencimento()
                    .format(DateTimeFormatter.ofPattern("MM/yyyy")));
        }
        return faturaRepository.save(fatura);
    }

    public Fatura darBaixa(Long faturaId, LocalDate dataPagamento) {
        Fatura fatura = faturaRepository.findById(faturaId)
                .orElseThrow(() -> new RuntimeException("Fatura não encontrada: " + faturaId));

        fatura.setStatus(StatusFatura.PAGO);
        fatura.setDataPagamento(dataPagamento != null ? dataPagamento : LocalDate.now());
        return faturaRepository.save(fatura);
    }
    public Fatura estornar(Long faturaId) {
        Fatura fatura = faturaRepository.findById(faturaId)
                .orElseThrow(() -> new RuntimeException("Fatura não encontrada: " + faturaId));
         fatura.setStatus(StatusFatura.PENDENTE);
         fatura.setDataPagamento(null);
         return faturaRepository.save(fatura);
}
    public Fatura cancelar(Long faturaId) {
        Fatura fatura = faturaRepository.findById(faturaId)
                .orElseThrow(() -> new RuntimeException("Fatura não encontrada: " + faturaId));
        fatura.setStatus(StatusFatura.CANCELADO);
        return faturaRepository.save(fatura);
    }

    public void gerarFaturaParaCliente(Long clienteId) {
        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));

        if (cliente.getPlano() == null) {
            throw new RuntimeException("Cliente não possui plano associado");
        }

        LocalDate hoje = LocalDate.now();
        int dia = cliente.getDiaVencimento() != null ? cliente.getDiaVencimento() : 10;
        LocalDate vencimento = LocalDate.of(hoje.getYear(), hoje.getMonth(), Math.min(dia, hoje.lengthOfMonth()));
        if (vencimento.isBefore(hoje)) {
            vencimento = vencimento.plusMonths(1);
        }

        Fatura fatura = new Fatura();
        fatura.setCliente(cliente);
        fatura.setValor(cliente.getPlano().getValor());
        fatura.setDataVencimento(vencimento);
        fatura.setStatus(StatusFatura.PENDENTE);
        fatura.setReferenciaMes(vencimento.format(DateTimeFormatter.ofPattern("MM/yyyy")));

        faturaRepository.save(fatura);
    }

    private void atualizarVencidas() {
        List<Fatura> vencidas = faturaRepository.findVencidas(LocalDate.now());
        for (Fatura f : vencidas) {
            f.setStatus(StatusFatura.VENCIDO);
        }
        if (!vencidas.isEmpty()) {
            faturaRepository.saveAll(vencidas);
        }
    }

    public long contarPendentes() {
        return faturaRepository.countByStatus(StatusFatura.PENDENTE);
    }

    public long contarVencidas() {
        return faturaRepository.countByStatus(StatusFatura.VENCIDO);
    }

    public BigDecimal totalRecebidoMes() {
        BigDecimal total = faturaRepository.totalRecebidoMesAtual();
        return total != null ? total : BigDecimal.ZERO;
    }
}
