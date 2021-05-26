package br.com.alloy.comanditatendente.service.model.dto;

import br.com.alloy.comanditatendente.service.model.Comanda;
import br.com.alloy.comanditatendente.service.model.MovimentoDiarioFormaPagamento;

public class ComandaPagamento {

    private Comanda comanda;
    private MovimentoDiarioFormaPagamento formaPagamento;

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

}
