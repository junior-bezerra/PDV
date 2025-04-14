// src/main/java/com/pdv/sistemapdv/dto/FechamentoCaixaDTO.java
package com.pdv.sistemapdv.dto;

import java.math.BigDecimal;

public class FechamentoCaixaDTO {
    private BigDecimal totalVendas;
    private BigDecimal totalEntrada;
    private BigDecimal totalSaida;
    private BigDecimal saldoFinal;

    // Construtores, getters e setters
    public FechamentoCaixaDTO(BigDecimal totalVendas, BigDecimal totalEntrada, BigDecimal totalSaida, BigDecimal saldoFinal) {
        this.totalVendas = totalVendas;
        this.totalEntrada = totalEntrada;
        this.totalSaida = totalSaida;
        this.saldoFinal = saldoFinal;
    }

    public BigDecimal getTotalVendas() {
        return totalVendas;
    }

    public void setTotalVendas(BigDecimal totalVendas) {
        this.totalVendas = totalVendas;
    }

    public BigDecimal getTotalEntrada() {
        return totalEntrada;
    }

    public void setTotalEntrada(BigDecimal totalEntrada) {
        this.totalEntrada = totalEntrada;
    }

    public BigDecimal getTotalSaida() {
        return totalSaida;
    }

    public void setTotalSaida(BigDecimal totalSaida) {
        this.totalSaida = totalSaida;
    }

    public BigDecimal getSaldoFinal() {
        return saldoFinal;
    }

    public void setSaldoFinal(BigDecimal saldoFinal) {
        this.saldoFinal = saldoFinal;
    }
}
