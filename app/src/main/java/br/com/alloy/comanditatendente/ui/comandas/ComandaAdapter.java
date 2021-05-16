package br.com.alloy.comanditatendente.ui.comandas;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import br.com.alloy.comanditatendente.R;
import br.com.alloy.comanditatendente.databinding.ComandaItemBinding;
import br.com.alloy.comanditatendente.service.model.Comanda;

public class ComandaAdapter extends ArrayAdapter<Comanda> {

    private final List<Comanda> comandas;
    private final Context context;
    private final ComandaClickListener listener;
    private final SimpleDateFormat hourFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());

    /**
     * @param comandas
     */
    public ComandaAdapter(Context context, List<Comanda> comandas, ComandaClickListener listener) {
        super(context, R.layout.comanda_item, comandas);
        this.context = context;
        this.listener = listener;
        this.comandas = comandas;
    }

    @Override
    public int getCount() {
        return comandas.size();
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        ComandaItemBinding binding;

        if(convertView == null) {
            binding = ComandaItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
            convertView = binding.getRoot();
            convertView.setTag(binding);
        } else {
            binding = (ComandaItemBinding) convertView.getTag();
        }

        Comanda comanda = getItem(position);

        binding.txvNrComanda.setText(String.format(Locale.getDefault(),"%d", comanda.getIdComanda()));

        if (comanda.isOpen()) {
            //TODO Ponto de verificação futura, melhoria para API >= 23 (atual é 21)
            //holder.txvNumeroComanda.setTextAppearance(R.style.comandaAberta);
            binding.txvNrComanda.setTextColor(context.getResources().getColor(R.color.colorPrimary));
            binding.txvNrComanda.setTypeface(null, Typeface.BOLD);
            binding.txvNrComanda.setShadowLayer(2, -1, 1, Color.DKGRAY);
            binding.txvNrMesa.setText(String.format(Locale.getDefault(),"%d", comanda.getNumeroMesa()));
            if(comanda.hasPedidos()) {
                binding.horaUltimoPedido.setText(hourFormat.format(comanda.getHoraUltimoPedido()));
                setVisibility(View.VISIBLE, binding.imgvMesaComanda, binding.txvNrMesa,
                        binding.imgvHoraUltimoPedido, binding.horaUltimoPedido);
            } else {
                setVisibility(View.VISIBLE, binding.imgvMesaComanda, binding.txvNrMesa);
            }
            //convertView.setBackgroundColor(getContext().getResources().getColor(R.color.colorPrimary));
        } else {
            //TODO Ponto de verificação futura, melhoria para API >= 23 (atual é 21)
            //binding.txvNumeroComanda.setTextAppearance(R.style.comandaFechada);
            binding.txvNrComanda.setTextColor(context.getResources().getColor(R.color.comandaFechada));
            binding.txvNrComanda.setTypeface(null, Typeface.NORMAL);
            binding.txvNrComanda.setShadowLayer(0, 0, 0, 0);
            setVisibility(View.INVISIBLE, binding.imgvMesaComanda, binding.txvNrMesa,
                    binding.imgvHoraUltimoPedido, binding.horaUltimoPedido);
            //convertView.setBackgroundColor(getContext().getResources().getColor(R.color.white));
        }

        convertView.setOnClickListener((View v) -> listener.comandaClicked(comanda));
        convertView.setOnLongClickListener((View v) -> listener.comandaLongClicked(comanda));

        return convertView;
    }

    public void updateItem(Comanda comanda) {
        comandas.set(comandas.indexOf(comanda), comanda);
        notifyDataSetChanged();
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).getIdComanda();
    }

    private void setVisibility(int visibility, View... views) {
        for (View v : views) {
            v.setVisibility(visibility);
        }
    }

}