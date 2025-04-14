package com.pdv.sistemapdv.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class FormaPagamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String descricao;
}
