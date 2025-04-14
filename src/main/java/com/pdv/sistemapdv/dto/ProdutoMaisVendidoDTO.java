package com.pdv.sistemapdv.dto;

import java.math.BigDecimal;

public class ProdutoMaisVendidoDTO {

    private String nomeProduto;
    private int quantidadeVendida;
    private BigDecimal totalVendido;

    public ProdutoMaisVendidoDTO(String nomeProduto, int quantidadeVendida, BigDecimal totalVendido) {
        this.nomeProduto = nomeProduto;
        this.quantidadeVendida = quantidadeVendida;
        this.totalVendido = totalVendido;
    }

    public String getNomeProduto() {
        return nomeProduto;
    }

    public void setNomeProduto(String nomeProduto) {
        this.nomeProduto = nomeProduto;
    }

    public int getQuantidadeVendida() {
        return quantidadeVendida;
    }

    public void setQuantidadeVendida(int quantidadeVendida) {
        this.quantidadeVendida = quantidadeVendida;
    }

    public BigDecimal getTotalVendido() {
        return totalVendido;
    }

    public void setTotalVendido(BigDecimal totalVendido) {
        this.totalVendido = totalVendido;
    }
}
