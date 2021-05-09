package br.com.alloy.comanditatendente.ui.pedidos;

import br.com.alloy.comanditatendente.service.model.Pedido;
import br.com.alloy.comanditatendente.service.model.Produto;

public interface ProdutoPedidoClickListener {

    void produtoClicked(Produto produto);

    void pedidoClicked(Pedido pedido);

}
