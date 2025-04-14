
package com.pdv.sistemapdv.exception;

public class ProdutoNaoEncontradoException extends RuntimeException {
    public ProdutoNaoEncontradoException(Long produtoId) {
        super("Produto não encontrado. ID: " + produtoId);
    }
}

