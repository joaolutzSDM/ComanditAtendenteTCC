package br.com.alloy.comanditatendente.ui.pedidos;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import br.com.alloy.comanditatendente.R;
import br.com.alloy.comanditatendente.databinding.DialogPedidoBinding;
import br.com.alloy.comanditatendente.databinding.FragmentPedidosBinding;
import br.com.alloy.comanditatendente.databinding.SeletorQuantidadeBinding;
import br.com.alloy.comanditatendente.service.RetrofitConfig;
import br.com.alloy.comanditatendente.service.exception.APIException;
import br.com.alloy.comanditatendente.service.exception.ExceptionUtils;
import br.com.alloy.comanditatendente.service.model.Comanda;
import br.com.alloy.comanditatendente.service.model.Pedido;
import br.com.alloy.comanditatendente.service.model.Produto;
import br.com.alloy.comanditatendente.service.model.ProdutoCategoria;
import br.com.alloy.comanditatendente.ui.Messages;
import br.com.alloy.comanditatendente.ui.comandas.ComandasViewModel;
import br.com.alloy.comanditatendente.ui.util.StringUtil;
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
        setHasOptionsMenu(true);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setViewModelObserversAndListeners();
        binding.rcvPedidosProdutos.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        carregarCategorias();
    }

    private void setViewModelObserversAndListeners() {
        //listagem cetagorias de produtos
        pedidosViewModel.getCategorias().observe(getViewLifecycleOwner(), categorias -> {
            carregarProdutos(categorias.get(0));
        });
        //listagem produtos
        pedidosViewModel.getProdutos().observe(getViewLifecycleOwner(), produtos -> {
            binding.rcvPedidosProdutos.setAdapter(new ProdutoAdapter(produtos, this));
        });
        //listagem pedidos
        pedidosViewModel.getPedidos().observe(getViewLifecycleOwner(), pedidos -> {
            binding.rcvPedidosProdutos.setAdapter(new PedidoAdapter(pedidos, this));
        });
        binding.fabSelecionarCategoriaProduto.setOnClickListener(v -> {
            showListDialog(getString(R.string.msgSelecionarCategoriaProduto), null, null, (dialog, which) -> {
                carregarProdutos(pedidosViewModel.getCategoriasValue().get(which));
            });
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

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.menu_pedidos, menu);
        //remove os dois últimos itens do menu
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        binding.swipeRefreshPedidos.setRefreshing(false);
        carregarPedidos();
        return true;
    }

    private Callback<Pedido> callBackPedidoUpdate = new Callback<Pedido>() {
        @Override
        public void onResponse(Call<Pedido> call, Response<Pedido> response) {
            if(response.isSuccessful()) {
                String message = getString(R.string.msgPedidoMensagemRetornoSucesso,
                        Objects.requireNonNull(call.request().header("msg")));
                Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
            } else {
                showAPIException(ExceptionUtils.parseException(response));
            }
        }

        @Override
        public void onFailure(Call<Pedido> call, Throwable t) {
            Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
        }
    };

    private void showListDialog(String title, @Nullable CharSequence[] items, @Nullable Integer listResId, DialogInterface.OnClickListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(title);
        if(items != null) {
            builder.setItems(items, listener);
        } else if(listResId != null) {
            builder.setItems(listResId, listener);
        } else {
            builder.setAdapter(new ArrayAdapter<>(getContext(),
                    R.layout.produto_categoria_item, pedidosViewModel.getCategoriasValue()), listener);
        }
        builder.show();
    }

    private void carregarCategorias() {
        if(pedidosViewModel.getCategorias().getValue() == null) {
            RetrofitConfig.getComanditAPI().consultarCategoriasProduto().enqueue(new Callback<List<ProdutoCategoria>>() {
                @Override
                public void onResponse(Call<List<ProdutoCategoria>> call, Response<List<ProdutoCategoria>> response) {
                    if(response.isSuccessful()) {
                        pedidosViewModel.setCategorias(response.body());
                    } else {
                        showAPIException(ExceptionUtils.parseException(response));
                    }
                }

                @Override
                public void onFailure(Call<List<ProdutoCategoria>> call, Throwable t) {
                    Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void showAPIException(APIException e) {
        Log.e(getString(R.string.api_exception), e.getMessage());
        Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
    }

    private void carregarProdutos(ProdutoCategoria produtoCategoria) {
        RetrofitConfig.getComanditAPI().consultarProdutosPorCategoria(produtoCategoria).enqueue(new Callback<List<Produto>>() {
            @Override
            public void onResponse(Call<List<Produto>> call, Response<List<Produto>> response) {
                if(binding.swipeRefreshPedidos.isRefreshing()) {
                    binding.swipeRefreshPedidos.setRefreshing(false);
                }
                if(response.isSuccessful()) {
                    pedidosViewModel.setProdutos(response.body());
                } else {
                    showAPIException(ExceptionUtils.parseException(response));
                }
            }

            @Override
            public void onFailure(Call<List<Produto>> call, Throwable t) {
                if(binding.swipeRefreshPedidos.isRefreshing()) {
                    binding.swipeRefreshPedidos.setRefreshing(false);
                }
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void carregarPedidos() {
        if (comandasViewModel.isComandaSelected()) {
            RetrofitConfig.getComanditAPI().consultarPedidosComandaResumo(comandasViewModel.getComandaValue()).enqueue(new Callback<List<Pedido>>() {
                @Override
                public void onResponse(Call<List<Pedido>> call, Response<List<Pedido>> response) {
                    if(binding.swipeRefreshPedidos.isRefreshing()) {
                        binding.swipeRefreshPedidos.setRefreshing(false);
                    }
                    if(response.isSuccessful()) {
                        pedidosViewModel.setPedidos(response.body());
                    } else {
                        showAPIException(ExceptionUtils.parseException(response));
                    }
                }

                @Override
                public void onFailure(Call<List<Pedido>> call, Throwable t) {
                    if(binding.swipeRefreshPedidos.isRefreshing()) {
                        binding.swipeRefreshPedidos.setRefreshing(false);
                    }
                    Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Messages.showToastMessage(getContext(), getString(R.string.msgSelecionarComanda));
        }
    }

    @Override
    public void produtoClicked(Produto produto) {
        if(!produto.getDisponivel()) { //verifica se o produto está disponível
            Toast.makeText(getContext(), getString(R.string.msgProdutoIndisponivel), Toast.LENGTH_SHORT).show();
            return;
        }
        if(!comandasViewModel.isComandaSelected()) { //verifica se existe uma comanda selecionada
            Toast.makeText(getContext(), getString(R.string.msgSelecionarComanda), Toast.LENGTH_SHORT).show();
            return;
        }
        showConfirmPedidoDialog(produto);
    }

    @Override
    public void pedidoClicked(Pedido pedido) {
        showManagePedidoDialog(pedido);
    }

    private void showConfirmPedidoDialog(final Produto produto) {
        DialogPedidoBinding dialogPedidoBinding = DialogPedidoBinding.inflate(LayoutInflater.from(getContext()));
        PedidoDialogHolder holder = new PedidoDialogHolder(dialogPedidoBinding.includeQuantidade);

        //seta os dados do produto selecionado
        dialogPedidoBinding.txvDialogNomeProduto.setText(String.format(Locale.getDefault(),
                getString(R.string.produto_desc_pedido_dialog), produto.getIdProduto(), produto.getNomeProduto()));

        final AlertDialog dialog = createGenericDialog(getString(R.string.title_dialog_pedido_cadastrar), null);
        dialog.setView(dialogPedidoBinding.getRoot(), 10,20,10,5);
        dialog.setButton(DialogInterface.BUTTON_POSITIVE, getString(R.string.btnOK), (dialog1, which) -> {
            Pedido pedido = new Pedido(comandasViewModel.getComandaValue(), produto, holder.getQuantidade());
            String obs = dialogPedidoBinding.edtDialogObs.getText().toString();
            if(!StringUtil.isEmptyString(obs)) {
                pedido.setObservacaoPedido(obs);
            }
            RetrofitConfig.getComanditAPI().cadastrarPedido(pedido).enqueue(callBackPedidoUpdate);
        });
        dialog.setButton(DialogInterface.BUTTON_NEGATIVE, getString(R.string.btnCancelar), (dialog2, which) -> dialog2.dismiss());
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        dialog.show();
    }

    private void showManagePedidoDialog(final Pedido pedido) {
        AlertDialog dialog = createGenericDialog(getString(R.string.title_dialog_pedido_gerenciar),
                pedido.toStringResumo());
        dialog.setButton(DialogInterface.BUTTON_POSITIVE, getString(R.string.btnCancelar), (dialog1, which) -> showCancelPedidoDialog(pedido));
        dialog.setButton(DialogInterface.BUTTON_NEUTRAL, getString(R.string.btnTransferir), (dialog2, which) -> {
            if(pedido.getComanda().equals(comandasViewModel.getComandaValue())) {
                if(pedido.comHistorico()) {
                    showTransferPedidoDialog(pedido);
                } else {
                    Messages.showDialogMessage(getContext(), getString(R.string.msgPedidoSemHistorico));
                }
            } else {
                Messages.showDialogMessage(getContext(), getString(R.string.msgPedidoComandaSelecionadaDifere));
            }
        });
        dialog.setButton(DialogInterface.BUTTON_NEGATIVE, getString(R.string.btnVoltar), (dialog3, which) -> dialog3.dismiss());
        dialog.show();
    }

    private void showCancelPedidoDialog(final Pedido pedido) {
        AlertDialog dialog = createGenericDialog(getString(R.string.title_dialog_pedido_cancelar),
                getString(R.string.msgConfirmCancelPedido));
        dialog.setButton(DialogInterface.BUTTON_POSITIVE, getString(R.string.btnSim),
                (dialog1, which) -> RetrofitConfig.getComanditAPI().cancelarPedido(pedido).enqueue(callBackPedidoUpdate));
        dialog.setButton(DialogInterface.BUTTON_NEGATIVE, getString(R.string.btnNao), (dialog2, which) -> dialog2.dismiss());
        dialog.show();
    }

    private void showTransferPedidoDialog(final Pedido pedido) {
        AlertDialog dialog = createGenericDialog(getString(R.string.title_dialog_pedido_transferir),
                getString(R.string.msgComandaDestinoTransferencia));
        final Spinner spnComandasAbertas = new Spinner(getContext());
        List<Comanda> comandasMesa = getComandasMesa(comandasViewModel.getComandaValue().getNumeroMesa());
        if (comandasMesa != null) {
            spnComandasAbertas.setAdapter(new ArrayAdapter<>(getContext(), R.layout.produto_categoria_item, comandasMesa));
            dialog.setView(spnComandasAbertas, 10,20,10,5);
            dialog.setButton(DialogInterface.BUTTON_POSITIVE, getString(R.string.btnOK), (dialog1, which) -> {
                Comanda comandaDestino = (Comanda) spnComandasAbertas.getSelectedItem();
                if (!comandaDestino.equals(pedido.getComanda())) {
                    pedido.setItem(pedido.getComanda().getIdComanda()); //recupera a comanda origem do pedido
                    pedido.setComanda(comandaDestino); //seta a comanda destino do pedido
                    RetrofitConfig.getComanditAPI().transferirPedido(pedido).enqueue(callBackPedidoUpdate);
                } else {
                    Messages.showDialogMessage(getContext(), getString(R.string.msgPedidoComandasIguaisSelecionadas));
                }
            });
            dialog.setButton(DialogInterface.BUTTON_NEGATIVE, getString(R.string.btnCancelar), (dialog2, which) -> dialog2.dismiss());
            dialog.show();
        } else {
            showFinishDialog(null);
        }
    }

    private List<Comanda> getComandasMesa(Integer mesa) {
        try {
            return RetrofitConfig.getComanditAPI().consultarComandasMesa(mesa).execute().body();
        } catch (IOException e) {
            return null;
        }
    }

    private AlertDialog createGenericDialog(String title, String message){
        AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
        //alertDialog.setIcon(R.mipmap.ic_launcher);
        alertDialog.setTitle(title);
        if(message != null) {
            alertDialog.setMessage(message);
        }
        alertDialog.setCancelable(false);
        alertDialog.setCanceledOnTouchOutside(false);
        return alertDialog;
    }

    //Método para mostrar uma mensagem genérica ao usuário quando um erro ocorrer. (o app é fechado ao clicar no OK)
    private void showFinishDialog(@Nullable String message) {
        final AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
        alertDialog.setTitle(getString(R.string.app_name));
        alertDialog.setMessage(message == null ? getString(R.string.msgError) : message);
        alertDialog.setIcon(R.mipmap.ic_launcher);
        alertDialog.setCancelable(false);
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, getString(R.string.btnOK), (dialog, which) -> requireActivity().finish());
        alertDialog.show();
    }

}