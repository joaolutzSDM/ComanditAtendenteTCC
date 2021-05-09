package br.com.alloy.comanditatendente.ui.pedidos;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Locale;

import static br.com.alloy.comanditcliente.ui.util.StringUtil.getCurrencyStringWithoutR$;
import br.com.alloy.comanditatendente.R;
import br.com.alloy.comanditatendente.databinding.ProdutoItemBinding;
import br.com.alloy.comanditatendente.service.model.Produto;

public class ProdutoAdapter extends RecyclerView.Adapter<ProdutoAdapter.ProdutoHolder> {

    private final List<Produto> produtos;
    private ProdutoPedidoClickListener listener;

    public ProdutoAdapter(List<Produto> produtos, ProdutoPedidoClickListener listener) {
        this.produtos = produtos;
        this.listener = listener;
    }

    @NonNull
    @NotNull
    @Override
    public ProdutoHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        return new ProdutoAdapter.ProdutoHolder(ProdutoItemBinding.inflate(LayoutInflater.from(parent.getContext()),
                parent, false));
    }

    public Produto getFirstProduto() {
        return produtos.get(0);
    }

    @Override
    public void onBindViewHolder(ProdutoHolder holder, int position) {
        Produto produto = produtos.get(position);
        holder.binding.rlNomeProduto.setText(produto.getNomeProduto());
        holder.binding.rlCodigoProduto.setText(String.format(Locale.getDefault(), "%d", produto.getIdProduto()));
        holder.binding.rlPrecoProduto.setText(getCurrencyStringWithoutR$(produto.getValorProduto()));
        holder.binding.rlProdutoDisponivel.setVisibility(produto.getDisponivel() ? View.GONE : View.VISIBLE);
        if(!produto.getDisponivel()) {
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) holder.binding.rlPrecoProduto.getLayoutParams();
            params.removeRule(RelativeLayout.ALIGN_BASELINE);
            params.addRule(RelativeLayout.ALIGN_BASELINE, R.id.rlProdutoDisponivel);
            holder.binding.rlPrecoProduto.setLayoutParams(params);
        }
    }

    @Override
    public int getItemCount() {
        return produtos != null ? produtos.size() : 0;
    }

    class ProdutoHolder extends RecyclerView.ViewHolder {

        private final ProdutoItemBinding binding;

        ProdutoHolder(ProdutoItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            itemView.setOnClickListener((View v) -> listener.produtoClicked(produtos.get(getAdapterPosition())));
        }

    }

}
