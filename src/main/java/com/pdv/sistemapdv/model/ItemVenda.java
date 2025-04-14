package com.pdv.sistemapdv.model;

import jakarta.persistence.*;
import lombok.*;


import java.math.BigDecimal;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemVenda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Produto produto;

    private int quantidade;

    private BigDecimal precoUnitario;

    private BigDecimal subtotal;

    @ManyToOne
    private Venda venda;
}
