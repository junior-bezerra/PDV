package com.pdv.sistemapdv.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class RelatorioVendaDTO {

    private LocalDate data;
    private BigDecimal valorTotal;
    private int quantidadeVendas;

    public RelatorioVendaDTO(LocalDate data, BigDecimal valorTotal, int quantidadeVendas) {
        this.data = data;
        this.valorTotal = valorTotal;
        this.quantidadeVendas = quantidadeVendas;
    }

    public LocalDate getData() {
        return data;
    }

    public BigDecimal getValorTotal() {
        return valorTotal;
    }

    public int getQuantidadeVendas() {
        return quantidadeVendas;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public void setValorTotal(BigDecimal valorTotal) {
        this.valorTotal = valorTotal;
    }

    public void setQuantidadeVendas(int quantidadeVendas) {
        this.quantidadeVendas = quantidadeVendas;
    }
}
