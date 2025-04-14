package com.pdv.sistemapdv.repository;

import com.pdv.sistemapdv.model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {
}
