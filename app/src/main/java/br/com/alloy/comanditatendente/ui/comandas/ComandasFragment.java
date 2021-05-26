package br.com.alloy.comanditatendente.ui.comandas;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import br.com.alloy.comanditatendente.R;
import br.com.alloy.comanditatendente.databinding.FragmentComandasBinding;
import br.com.alloy.comanditatendente.service.RetrofitConfig;
import br.com.alloy.comanditatendente.service.exception.APIException;
import br.com.alloy.comanditatendente.service.exception.ExceptionUtils;
import br.com.alloy.comanditatendente.service.model.Comanda;
import br.com.alloy.comanditatendente.service.model.Configuracao;
import br.com.alloy.comanditatendente.service.model.dto.MesaAlt;
import br.com.alloy.comanditatendente.ui.Messages;
import br.com.alloy.comanditatendente.ui.cupomfiscal.CupomFiscalActivity;
import br.com.alloy.comanditatendente.ui.util.FragmentUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ComandasFragment extends Fragment implements ComandaClickListener {

    private FragmentComandasBinding binding;
    private ComandasViewModel comandasViewModel;
    private BottomNavigationView bottomNavigationView;
    private ProgressDialog progressDialog;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        Log.e("TESTE", "onCreateView: ComandaFragment");
        binding = FragmentComandasBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.e("TESTE", "onViewCreated: ComandaFragment");
        comandasViewModel = new ViewModelProvider(requireActivity()).get(ComandasViewModel.class);
        bottomNavigationView = requireActivity().findViewById(R.id.nav_view);
        setHasOptionsMenu(true);
        if(comandasViewModel.getQtdMesas().getValue() == null) {
            carregarQtdeMesas(view);
        } else {
            setViewModelObserversAndListeners(view);
            carregarComandas(ComandaFilter.TODAS);
        }
    }

    private void carregarQtdeMesas(View view) {
        progressDialog = ProgressDialog.show(getContext(), null,
                getString(R.string.carregando_configuracao), true);
        RetrofitConfig.getComanditAPI().consultarConfiguracao(new Configuracao("QUANTIDADE_MESAS")).enqueue(new Callback<Configuracao>() {
            @Override
            public void onResponse(Call<Configuracao> call, Response<Configuracao> response) {
                progressDialog.dismiss();
                if(response.isSuccessful()) {
                    comandasViewModel.setQtdMesas(Integer.parseInt(response.body().getValorConfiguracao()));
                    setViewModelObserversAndListeners(view);
                    carregarComandas(ComandaFilter.TODAS);
                } else {
                    showAPIException(ExceptionUtils.parseException(response));
                }
            }

            @Override
            public void onFailure(Call<Configuracao> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setViewModelObserversAndListeners(View view) {
        //listagem de comandas
        comandasViewModel.getComandas().observe(getViewLifecycleOwner(),
                comandas -> binding.gdvComandas.setAdapter(new ComandaAdapter(getContext(), comandas, this)));
        comandasViewModel.getComanda().observe(getViewLifecycleOwner(), comanda -> {
            bottomNavigationView.getMenu().findItem(R.id.navigation_pedidos).setTitle(
                    String.format(getString(R.string.pedidos_comanda_title), comanda.getIdComanda()));
        });
        comandasViewModel.getMesa().observe(getViewLifecycleOwner(), mesa -> {
            binding.fabSelecionarMesa.setImageBitmap(textAsBitmap(mesa.toString()));
        });
        binding.fabSelecionarMesa.setOnClickListener(v -> {
            showListDialog(getString(R.string.msgSelecionarMesa), getMesasArray(comandasViewModel.getQtdMesas().getValue()), null, (dialog, which) -> {
                comandasViewModel.setMesa(which + 1);
            });
        });
        //setting loadData as the method for the swipe down refresh layout
        binding.swipeRefreshComandas.setOnRefreshListener(() -> carregarComandas(ComandaFilter.TODAS));
    }

    private CharSequence[] getMesasArray(int qtdMesas) {
        if(comandasViewModel.getMesas() == null) {
            comandasViewModel.setMesas(new CharSequence[qtdMesas]);
            for(int i = 0; i < qtdMesas;i++) {
                comandasViewModel.getMesas()[i] = String.valueOf(i + 1);
            }
        }
        return comandasViewModel.getMesas();
    }

    private Bitmap textAsBitmap(String text) {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setTextSize(30);
        paint.setColor(Color.WHITE);
        paint.setTextAlign(Paint.Align.LEFT);
        float baseline = -paint.ascent(); // ascent() is negative
        int width = (int) (paint.measureText(text) + 0.0f); // round
        int height = (int) (baseline + paint.descent() + 0.0f);
        Bitmap image = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(image);
        canvas.drawText(text, 0, baseline, paint);
        return image;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.menu_comandas, menu);
        //remove os dois últimos itens do menu
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        filterComandas();
        return true;
    }

    //-------------------CALLBACKS API------------------//

    private final Callback<List<Comanda>> callBackComandas = new Callback<List<Comanda>>() {
        @Override
        public void onResponse(Call<List<Comanda>> call, Response<List<Comanda>> response) {
            stopRefreshing();
            if(response.isSuccessful()) {
                comandasViewModel.setComandas(response.body());
            } else {
                showAPIException(ExceptionUtils.parseException(response));
            }
        }

        @Override
        public void onFailure(Call<List<Comanda>> call, Throwable t) {
            stopRefreshing();
            Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
        }
    };

    private final Callback<Comanda> callBackComandaUpdate = new Callback<Comanda>() {
        @Override
        public void onResponse(Call<Comanda> call, Response<Comanda> response) {
            if(response.isSuccessful()) {
                getComandaAdapter().updateItem(response.body());
            } else {
                showAPIException(ExceptionUtils.parseException(response));
            }
        }

        @Override
        public void onFailure(Call<Comanda> call, Throwable t) {
            Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
        }
    };

    private ComandaAdapter getComandaAdapter() {
        return (ComandaAdapter) binding.gdvComandas.getAdapter();
    }

    private void stopRefreshing() {
        if (binding.swipeRefreshComandas.isRefreshing()) {
            binding.swipeRefreshComandas.setRefreshing(false);
        }
    }

    private void showAPIException(APIException e) {
        Log.e(getString(R.string.api_exception), e.getMessage());
        Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
    }

    private void carregarComandas(ComandaFilter comandaFilter) {
        binding.swipeRefreshComandas.setRefreshing(true);
        switch (comandaFilter) {
            case TODAS:
                RetrofitConfig.getComanditAPI().consultarComandas().enqueue(callBackComandas);
                break;
            case ABERTAS:
                RetrofitConfig.getComanditAPI().consultarComandasAbertas().enqueue(callBackComandas);
                break;
            case FECHADAS:
                RetrofitConfig.getComanditAPI().consultarComandasFechadas().enqueue(callBackComandas);
                break;
            case POR_MESA:
                RetrofitConfig.getComanditAPI().consultarComandasMesa(comandasViewModel.getMesaValue()).enqueue(callBackComandas);
                break;
        }
    }

    private void filterComandas() {
        List<CharSequence> options = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.comandaFilters)));
        if(!comandasViewModel.isMesaSelected()) {
            options.remove(3);
        }
        showListDialog(getString(R.string.menu_comanda), options.toArray(new CharSequence[0]), null, (dialog, which) -> {
            String filter = getResources().getStringArray(R.array.comandaFilters)
                    [which].toUpperCase().replace(' ','_');
            carregarComandas(ComandaFilter.valueOf(filter));
        });
    }

    @Override
    public void comandaClicked(Comanda comanda) {
        if(comanda.isOpen()) {
            List<CharSequence> options = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.comandaManagement)));
            if (comanda.hasPedidos()) {
                options.add(getString(R.string.comanda_gerenciar_item_cupom_fiscal));
            } else {
                options.add(getString(R.string.comanda_gerenciar_fechar_comanda));
            }
            gerenciarComanda(comanda, options);
        } else {
            if(comandasViewModel.isMesaSelected()) { //se foi selecionada uma mesa
                comanda.setNumeroMesa(comandasViewModel.getMesaValue());
                RetrofitConfig.getComanditAPI().abrirComanda(comanda).enqueue(callBackComandaUpdate);
            } else { //mesa não foi selecionada
                Toast.makeText(getContext(), R.string.msgSelecionarMesa, Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public boolean comandaLongClicked(Comanda comanda) {
        if(comanda.isOpen()) {
            comandasViewModel.setComanda(comanda);
            bottomNavigationView.setSelectedItemId(bottomNavigationView.getMenu().findItem(R.id.navigation_pedidos).getItemId());
        } else {
            Toast.makeText(getContext(), R.string.msgComandaFechada, Toast.LENGTH_SHORT).show();
        }
        return true;
    }

    private void gerenciarComanda(Comanda comanda, List<CharSequence> options) {
        showListDialog(String.format(getString(R.string.title_dialog_comanda_gerenciar), comanda.getIdComanda()), options.toArray(new CharSequence[0]), null, (dialog, which) -> {
            switch(which) {
                case 0:
                    if(comandasViewModel.isMesaSelected()) {
                        alterarMesa(comanda);
                    } else { //mesa não foi selecionada
                        Toast.makeText(getContext(), R.string.msgSelecionarMesaDestino, Toast.LENGTH_SHORT).show();
                    }
                    break;
                case 1: //Ver SAM
                    Messages.showDialogMessage(getContext(), String.format(getString(R.string.comanda_gerenciar_sam), comanda.getSenhaAcessoMobile()));
                    break;
                case 2: //Compartilhar SAM
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("text/plain");
                    shareIntent.putExtra(Intent.EXTRA_SUBJECT, String.format(getString(R.string.comanda_share_sam_subject), comanda.getIdComanda()));
                    shareIntent.putExtra(Intent.EXTRA_TEXT, String.format(getString(R.string.comanda_gerenciar_sam), comanda.getSenhaAcessoMobile()));
                    startActivity(Intent.createChooser(shareIntent, getString(R.string.comanda_share_sam_title)));
                    break;
                case 3: //Gerar QR Code da SAM
                    showQRCodeDialog(comanda);
                    break;
                case 4:
                    if(comanda.hasPedidos()) {
                        Intent i = new Intent(getContext(), CupomFiscalActivity.class);
                        i.putExtra("comanda", comanda);
                        startActivity(i);
                    } else {
                        RetrofitConfig.getComanditAPI().fecharComanda(comanda).enqueue(callBackComandaUpdate);
                    }
                    break;
            }
        });
    }

    private void alterarMesa(Comanda comanda) {
        MesaAlt mesaAlt = new MesaAlt(comanda.getNumeroMesa(), comandasViewModel.getMesaValue());
        RetrofitConfig.getComanditAPI().alterarMesa(mesaAlt).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if(response.isSuccessful()) {
                    carregarComandas(ComandaFilter.TODAS);
                } else {
                    showAPIException(ExceptionUtils.parseException(response));
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showQRCodeDialog(Comanda comanda) {
        BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
        ImageView imageViewQrCode = new ImageView(getContext());
        try {
            Bitmap bitmap = barcodeEncoder.encodeBitmap(comanda.getQRCodeString(), BarcodeFormat.QR_CODE, 500, 500);
            imageViewQrCode.setImageBitmap(bitmap);
            final AlertDialog qrCodeDialog = FragmentUtil.createGenericDialog(getContext(), getString(R.string.title_dialog_comanda_sam_qrcode),
                    String.format(Locale.getDefault(), getString(R.string.message_dialog_comanda_sam_qrcode), comanda.getIdComanda()));
            qrCodeDialog.setView(imageViewQrCode);
            qrCodeDialog.setButton(DialogInterface.BUTTON_POSITIVE, getString(R.string.btnOK), (dialog1, which) -> {
                dialog1.dismiss();
            });
            qrCodeDialog.show();
        } catch (WriterException e) {
            Messages.showToastMessage(getContext(), getString(R.string.msgErroQRCode));
        }
    }

    private void showListDialog(String title, @Nullable CharSequence[] items, @Nullable Integer listResId, DialogInterface.OnClickListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(title);
        if(items != null) {
            builder.setItems(items, listener);
        } else if(listResId != null) {
            builder.setItems(listResId, listener);
        }
        builder.show();
    }

}