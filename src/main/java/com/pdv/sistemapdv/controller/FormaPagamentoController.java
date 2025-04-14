package com.pdv.sistemapdv.controller;

import com.pdv.sistemapdv.model.FormaPagamento;
import com.pdv.sistemapdv.repository.FormaPagamentoRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/formas-pagamento")
public class FormaPagamentoController {

    private final FormaPagamentoRepository repository;

    public FormaPagamentoController(FormaPagamentoRepository repository) {
        this.repository = repository;
    }

    @PostMapping
    public FormaPagamento salvar(@RequestBody FormaPagamento formaPagamento) {
        return repository.save(formaPagamento);
    }

    @GetMapping
    public List<FormaPagamento> listar() {
        return repository.findAll();
    }
}
