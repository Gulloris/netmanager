package com.ispmanager.service;

import com.ispmanager.model.Cliente;
import com.ispmanager.repository.ClienteRepository;
import com.ispmanager.repository.PlanoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private PlanoRepository planoRepository;

    public List<Cliente> listarTodos() {
        return clienteRepository.findAllAtivosComPlano();
    }

    public List<Cliente> buscarPorNome(String nome) {
        if (nome == null || nome.isBlank()) return listarTodos();
        return clienteRepository.buscarPorNome(nome);
    }

    public Optional<Cliente> buscarPorId(Long id) {
        return clienteRepository.findById(id);
    }

    public Cliente salvar(Cliente cliente) {
        return clienteRepository.save(cliente);
    }

    public Cliente atualizar(Long id, Cliente dadosNovos) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado: " + id));

        cliente.setNome(dadosNovos.getNome());
        cliente.setCpf(dadosNovos.getCpf());
        cliente.setTelefone(dadosNovos.getTelefone());
        cliente.setEmail(dadosNovos.getEmail());
        cliente.setEndereco(dadosNovos.getEndereco());
        cliente.setBairro(dadosNovos.getBairro());
        cliente.setCidade(dadosNovos.getCidade());
        cliente.setDiaVencimento(dadosNovos.getDiaVencimento());

        if (dadosNovos.getPlano() != null && dadosNovos.getPlano().getId() != null) {
            planoRepository.findById(dadosNovos.getPlano().getId())
                    .ifPresent(cliente::setPlano);
        } else {
            cliente.setPlano(null);
        }

        return clienteRepository.save(cliente);
    }

    public void inativar(Long id) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado: " + id));
        cliente.setAtivo(false);
        clienteRepository.save(cliente);
    }

    public long contarAtivos() {
        return clienteRepository.findByAtivoTrue().size();
    }
}
