package br.com.alloy.comanditatendente.ui.pedidos;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.DividerItemDecoration;

import java.util.List;

import br.com.alloy.comanditatendente.R;
import br.com.alloy.comanditatendente.databinding.FragmentComandasBinding;
import br.com.alloy.comanditatendente.databinding.FragmentPedidosBinding;
import br.com.alloy.comanditatendente.service.RetrofitConfig;
import br.com.alloy.comanditatendente.service.model.Comanda;
import br.com.alloy.comanditatendente.service.model.Pedido;
import br.com.alloy.comanditatendente.service.model.Produto;
import br.com.alloy.comanditatendente.service.model.ProdutoCategoria;
import br.com.alloy.comanditatendente.ui.Messages;
import br.com.alloy.comanditatendente.ui.comandas.ComandaAdapter;
import br.com.alloy.comanditatendente.ui.comandas.ComandaFilter;
import br.com.alloy.comanditatendente.ui.comandas.ComandasFragment;
import br.com.alloy.comanditatendente.ui.comandas.ComandasViewModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PedidosFragment extends Fragment implements ProdutoPedidoClickListener {

    private FragmentPedidosBinding binding;
    private PedidosViewModel pedidosViewModel;
    private ComandasViewModel comandasViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentPedidosBinding.inflate(inflater, container, false);
        pedidosViewModel = new ViewModelProvider(this).get(PedidosViewModel.class);
        comandasViewModel = new ViewModelProvider(requireActivity()).get(ComandasViewModel.class);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setViewModelObserversAndListeners();
        binding.rcvPedidosProdutos.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        carregarProdutos(new ProdutoCategoria());
    }

    private void setViewModelObserversAndListeners() {
        //listagem produtos
        pedidosViewModel.getProdutos().observe(getViewLifecycleOwner(), produtos -> {
            binding.rcvPedidosProdutos.setAdapter(new ProdutoAdapter(produtos, this));
        });
        //listagem pedidos
        pedidosViewModel.getPedidos().observe(getViewLifecycleOwner(), pedidos -> {
            binding.rcvPedidosProdutos.setAdapter(new PedidoAdapter(pedidos, this));
        });
        //setting loadData as the method for the swipe down refresh layout
        binding.swipeRefreshPedidos.setOnRefreshListener(() -> {
            if (binding.rcvPedidosProdutos.getAdapter() instanceof ProdutoAdapter) { //se o adapter de produtos estiver ativo
                ProdutoAdapter adapter = (ProdutoAdapter) binding.rcvPedidosProdutos.getAdapter();
                if(adapter.getItemCount() > 0) {
                    carregarProdutos(adapter.getFirstProduto().getProdutoCategoria());
                } else {
                    Messages.showToastMessage(getContext(), getString(R.string.msgSemProdutosCategoria));
                }
            } else {
                carregarPedidos();
            }
        });
    }

    private Callback<List<Pedido>> callBackPedidoUpdate = new Callback<List<Pedido>>() {
        @Override
        public void onResponse(Call<List<Pedido>> call, Response<List<Pedido>> response) {
            if(response.isSuccessful()) {

            } else {

            }
        }

        @Override
        public void onFailure(Call<List<Pedido>> call, Throwable t) {

        }
    };

    private void carregarProdutos(ProdutoCategoria produtoCategoria) {

    }

    private void carregarPedidos() {
        if (comandasViewModel.isComandaSelected()) {
            RetrofitConfig.getComanditAPI(getContext()).consultarPedidosComandaResumo(comandasViewModel.getComanda().getValue()).enqueue(new Callback<List<Pedido>>() {
                @Override
                public void onResponse(Call<List<Pedido>> call, Response<List<Pedido>> response) {
                    if(response.isSuccessful()) {

                    } else {

                    }
                }

                @Override
                public void onFailure(Call<List<Pedido>> call, Throwable t) {

                }
            });
        } else {
            if(binding.swipeRefreshPedidos.isRefreshing()) {
                binding.swipeRefreshPedidos.setRefreshing(false);
            }
            Messages.showToastMessage(getContext(), getString(R.string.msgSelecionarComanda));
        }
    }

    @Override
    public void produtoClicked(Produto produto) {

    }

    @Override
    public void pedidoClicked(Pedido pedido) {

    }

}