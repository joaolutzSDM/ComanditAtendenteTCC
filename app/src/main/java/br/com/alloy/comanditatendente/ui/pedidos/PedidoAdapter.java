package br.com.alloy.comanditatendente.ui.pedidos;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Locale;

import br.com.alloy.comanditatendente.R;
import br.com.alloy.comanditatendente.databinding.PedidoItemBinding;
import br.com.alloy.comanditatendente.service.model.Pedido;

public class PedidoAdapter extends RecyclerView.Adapter<PedidoAdapter.PedidoHolder> {

    private final List<Pedido> pedidos;
    private ProdutoPedidoClickListener listener;

    public PedidoAdapter(List<Pedido> pedidos, ProdutoPedidoClickListener listener) {
        this.pedidos = pedidos;
        this.listener = listener;
    }

    @Override
    public PedidoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new PedidoHolder(PedidoItemBinding.inflate(LayoutInflater.from(parent.getContext()),
                parent, false));
    }

    public void remove(Pedido pedido) {
        pedidos.remove(pedido);
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(PedidoHolder holder, int position) {
        Pedido pedido = pedidos.get(position);
        holder.binding.rlPedidoNomeProduto.setText(pedido.getProduto().getNomeProduto());
        holder.binding.rlPedidoCodigoProduto.setText(String.format(Locale.getDefault(), "%d", pedido.getProduto().getIdProduto()));
        holder.binding.rlPedidoQtdProduto.setText(String.format(Locale.getDefault(), "%d", pedido.getQuantidadePedido()));

        if(pedido.getObservacaoPedido() == null || pedido.getObservacaoPedido().trim().equals("")) {
            holder.binding.rlPedidoObservacao.setVisibility(View.GONE);
        } else {
            holder.binding.rlPedidoObservacao.setText(pedido.getObservacaoPedido());
            holder.binding.rlPedidoObservacao.setVisibility(View.VISIBLE);
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) holder.binding.rlPedidoQtdProduto.getLayoutParams();
            params.removeRule(RelativeLayout.ALIGN_BASELINE);
            params.addRule(RelativeLayout.ALIGN_BASELINE, R.id.rlPedidoObservacao);
            holder.binding.rlPedidoQtdProduto.setLayoutParams(params);
        }
    }

    @Override
    public int getItemCount() {
        return pedidos != null ? pedidos.size() : 0;
    }

    class PedidoHolder extends RecyclerView.ViewHolder {

        private final PedidoItemBinding binding;

        PedidoHolder(PedidoItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            itemView.setOnClickListener((View v) -> listener.pedidoClicked(pedidos.get(getAdapterPosition())));
        }

    }

}
