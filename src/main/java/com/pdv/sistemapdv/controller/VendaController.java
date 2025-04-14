package com.pdv.sistemapdv.controller;

import com.pdv.sistemapdv.dto.FechamentoCaixaDTO;
import com.pdv.sistemapdv.dto.RelatorioVendaDTO;
import com.pdv.sistemapdv.service.VendaService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ContentDisposition;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.http.HttpHeaders;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/vendas")

public class VendaController {

    private final VendaService vendaService;

    public VendaController(VendaService vendaService) {
        this.vendaService = vendaService;
    }

    @GetMapping("/relatorio")
    public List<RelatorioVendaDTO> relatorioComFiltro(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFim) {
        return vendaService.gerarRelatorio(dataInicio, dataFim);
    }
    @GetMapping("/relatorio-produtos/pdf")
    public ResponseEntity<byte[]> exportarRelatorioProdutosPDF(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFim) {

        byte[] pdf = vendaService.gerarRelatorioProdutosPDF(dataInicio, dataFim);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDisposition(ContentDisposition.attachment().filename("relatorio-produtos.pdf").build());

        return ResponseEntity.ok().headers(headers).body(pdf);
    }

    @GetMapping("/fechamento-caixa")
    public FechamentoCaixaDTO gerarFechamentoCaixa(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFim
    ) {
        return vendaService.gerarFechamentoCaixa(dataInicio, dataFim);
    }

    @GetMapping("/fechamento-caixa/pdf")
    public ResponseEntity<byte[]> gerarRelatorioFechamentoCaixaPDF(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFim
    ) {
        byte[] pdf = vendaService.gerarRelatorioFechamentoCaixaPDF(dataInicio, dataFim);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("filename", "fechamento-caixa.pdf");

        return ResponseEntity.ok()
                .headers(headers)
                .body(pdf);
    }


}
