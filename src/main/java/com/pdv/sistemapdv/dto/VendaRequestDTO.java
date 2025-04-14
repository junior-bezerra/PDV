package com.pdv.sistemapdv.dto;

import lombok.Data;

import java.util.List;

@Data
public class VendaRequestDTO {
    private Long formaPagamentoId;
    private List<ItemVendaDTO> itens;
}
