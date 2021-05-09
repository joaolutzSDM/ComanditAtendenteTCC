package br.com.alloy.comanditatendente.ui.pedidos;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import br.com.alloy.comanditatendente.service.model.Pedido;
import br.com.alloy.comanditatendente.service.model.Produto;

public class PedidosViewModel extends ViewModel {

    private final MutableLiveData<List<Pedido>> pedidos;
    private final MutableLiveData<List<Produto>> produtos;

    public PedidosViewModel() {
        pedidos = new MutableLiveData<>();
        produtos = new MutableLiveData<>();
    }

    public LiveData<List<Pedido>> getPedidos() {
        return pedidos;
    }

    public void setPedidos(List<Pedido> pedidos) {
        this.pedidos.setValue(pedidos);
    }

    public LiveData<List<Produto>> getProdutos() {
        return produtos;
    }

    public void setProdutos(List<Produto> produtos) {
        this.produtos.setValue(produtos);
    }

}