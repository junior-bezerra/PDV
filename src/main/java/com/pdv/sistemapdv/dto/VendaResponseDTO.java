package com.pdv.sistemapdv.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
public class VendaResponseDTO {
    private Long id;
    private LocalDateTime dataHora;
    private BigDecimal total;
    private String formaPagamento;
    private List<ItemVendaResponseDTO> itens;
}
