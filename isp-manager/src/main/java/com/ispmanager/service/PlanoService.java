package com.ispmanager.service;

import com.ispmanager.model.Plano;
import com.ispmanager.repository.PlanoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PlanoService {

    @Autowired
    private PlanoRepository planoRepository;

    public List<Plano> listarAtivos() {
        return planoRepository.findByAtivoTrueOrderByValorAsc();
    }

    public List<Plano> listarTodos() {
        return planoRepository.findAll();
    }

    public Optional<Plano> buscarPorId(Long id) {
        return planoRepository.findById(id);
    }

    public Plano salvar(Plano plano) {
        return planoRepository.save(plano);
    }

    public Plano atualizar(Long id, Plano dadosNovos) {
        Plano plano = planoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Plano não encontrado: " + id));

        plano.setNome(dadosNovos.getNome());
        plano.setDescricao(dadosNovos.getDescricao());
        plano.setVelocidadeMbps(dadosNovos.getVelocidadeMbps());
        plano.setValor(dadosNovos.getValor());

        return planoRepository.save(plano);
    }

    public void inativar(Long id) {
        Plano plano = planoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Plano não encontrado: " + id));
        plano.setAtivo(false);
        planoRepository.save(plano);
    }
}
