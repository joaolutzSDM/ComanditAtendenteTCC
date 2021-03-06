package br.com.alloy.comanditatendente.service.model.dto;

import java.math.BigDecimal;

import br.com.alloy.comanditatendente.service.model.Comanda;
import br.com.alloy.comanditatendente.service.model.MovimentoDiarioFormaPagamento;

public class ComandaPagamento {

    private Comanda comanda;
    private MovimentoDiarioFormaPagamento formaPagamento;
    private BigDecimal valor;

    public ComandaPagamento() {}

    public ComandaPagamento(Comanda comanda, MovimentoDiarioFormaPagamento formaPagamento) {
        this.comanda = comanda;
        this.formaPagamento = formaPagamento;
    }

    public Comanda getComanda() {
        return comanda;
    }

    public void setComanda(Comanda comanda) {
        this.comanda = comanda;
    }

    public MovimentoDiarioFormaPagamento getFormaPagamento() {
        return formaPagamento;
    }

    public void setFormaPagamento(MovimentoDiarioFormaPagamento formaPagamento) {
        this.formaPagamento = formaPagamento;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

}
