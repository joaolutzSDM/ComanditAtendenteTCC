package br.com.alloy.comanditatendente.ui.comandas;

import br.com.alloy.comanditatendente.service.model.Comanda;

public interface ComandaClickListener {

    void comandaClicked(Comanda comanda);

    boolean comandaLongClicked(Comanda comanda);

}
