package com.pdv.sistemapdv.exception;

public class FormaPagamentoInvalidaException extends RuntimeException {

    public FormaPagamentoInvalidaException(Long formaPagamentoId) {
        super("Forma de pagamento inválida. ID: " + formaPagamentoId);
    }
}
