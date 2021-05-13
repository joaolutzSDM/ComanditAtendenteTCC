package br.com.alloy.comanditatendente.ui.pedidos;

import android.view.View;

import br.com.alloy.comanditatendente.databinding.DialogPedidoBinding;
import br.com.alloy.comanditatendente.databinding.SeletorQuantidadeBinding;

public class PedidoDialogHolder {

    private Integer quantidade = 1; //inicializa a quantidade com 1

    public PedidoDialogHolder(SeletorQuantidadeBinding binding) {
        View.OnClickListener ajusteQuantidadeClick = v -> {
            quantidade += (v == binding.btnMaisQuantidade) ? 1 : -1;
            binding.txvQuantidade.setText(quantidade.toString());
            binding.btnMenosQuantidade.setEnabled(quantidade > 1);
            binding.btnMaisQuantidade.setEnabled(quantidade < 30);
        };
        binding.btnMenosQuantidade.setOnClickListener(ajusteQuantidadeClick);
        binding.btnMaisQuantidade.setOnClickListener(ajusteQuantidadeClick);
    }

    public Integer getQuantidade() {
        return quantidade;
    }

}
