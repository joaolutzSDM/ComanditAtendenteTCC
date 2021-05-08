package br.com.alloy.comanditatendente.ui.comandas;

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
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

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
import br.com.alloy.comanditatendente.service.model.MesaAlt;
import br.com.alloy.comanditatendente.ui.Messages;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ComandasFragment extends Fragment implements ComandaClickListener {

    private FragmentComandasBinding binding;
    private ComandasViewModel comandasViewModel;
    private BottomNavigationView bottomNavigationView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentComandasBinding.inflate(inflater, container, false);
        comandasViewModel = new ViewModelProvider(requireActivity()).get(ComandasViewModel.class);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setViewModelObserversAndListeners();
        carregarComandas(ComandaFilter.TODAS);
    }

    private void setViewModelObserversAndListeners() {
        //listagem de comandas
        comandasViewModel.getComandas().observe(getViewLifecycleOwner(),
                comandas -> binding.gdvComandas.setAdapter(new ComandaAdapter(getContext(), comandas, this)));

        //comanda selecionada
        comandasViewModel.getComanda().observe(getViewLifecycleOwner(), comanda -> {
            //vai para a tela de pedidos com a comanda atual selecionada
            bottomNavigationView.getMenu().findItem(R.id.navigation_pedidos).setTitle(String.format(
                    getString(R.string.pedidos_comanda_title), comanda.getIdComanda()));
            //bottomNavigationView.setSelectedItemId(R.id.navigation_pedidos);

            NavController navController = NavHostFragment.findNavController(ComandasFragment.this);
            navController.navigate(R.id.navigation_pedidos);
        });
        comandasViewModel.getMesa().observe(getViewLifecycleOwner(), mesa -> {
            binding.fabSelecionarMesa.setImageBitmap(textAsBitmap(mesa.toString()));
        });
        //setting loadData as the method for the swipe down refresh layout
        binding.swipeRefreshComandas.setOnRefreshListener(() -> carregarComandas(ComandaFilter.TODAS));
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
                RetrofitConfig.getComanditAPI(getContext()).consultarComandas().enqueue(callBackComandas);
                break;
            case ABERTAS:
                RetrofitConfig.getComanditAPI(getContext()).consultarComandasAbertas().enqueue(callBackComandas);
                break;
            case FECHADAS:
                RetrofitConfig.getComanditAPI(getContext()).consultarComandasFechadas().enqueue(callBackComandas);
                break;
            case POR_MESA:
                RetrofitConfig.getComanditAPI(getContext()).consultarComandasMesa(comandasViewModel.getMesa().getValue()).enqueue(callBackComandas);
                break;
        }
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
        if (item.getOrder() == 1) { //Comandas Filter
            filterComandas();
            return true;
        }
        return false;
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
            options.add(getString(R.string.comanda_gerenciar_item_fechar));
//            TODO - ANALISAR IMPLEMENTAÇÃO DA QUANTIDADE DE PEDIDOS DE UMA COMANDA (NECESSÁRIO?)
//            options.add(comanda.hasOrders() ? getString(R.string.comanda_gerenciar_item_cupom_fiscal)
//                    : getString(R.string.comanda_gerenciar_item_fechar));
            gerenciarComanda(comanda, options);
        } else {
            if(comandasViewModel.isMesaSelected()) { //se foi selecionada uma mesa
                RetrofitConfig.getComanditAPI(getContext()).abrirComanda(comanda);
            } else { //mesa não foi selecionada
                Toast.makeText(getContext(), R.string.msgSelecionarMesa, Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public boolean comandaLongClicked(Comanda comanda) {
        if(comanda.isOpen()) {
            comandasViewModel.setComanda(comanda);
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
                    if(options.get(which).equals(getString(R.string.comanda_gerenciar_item_cupom_fiscal))) { //Ver Cupom Fiscal
//                        Intent i = new Intent(this, CupomFiscalActivity.class);
//                        i.putExtra("comanda", comanda);
//                        startActivity(i);
                    } else if(options.get(which).equals(getString(R.string.comanda_gerenciar_item_fechar))) { //Fechar Comanda
                        //getComandaAsync().execute(new ParamResult(AsyncMethod.FECHAR_COMANDA, comanda));
                    }
                    break;
            }
        });
    }

    private void alterarMesa(Comanda comanda) {
        MesaAlt mesaAlt = new MesaAlt(comanda.getNumeroMesa(), comandasViewModel.getMesa().getValue());
        RetrofitConfig.getComanditAPI(getContext()).alterarMesa(mesaAlt).enqueue(new Callback<String>() {
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

            }
        });
    }

    private void showQRCodeDialog(Comanda comanda) {
        BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
        ImageView imageViewQrCode = new ImageView(getContext());
        try {
            Bitmap bitmap = barcodeEncoder.encodeBitmap(comanda.getQRCodeString(), BarcodeFormat.QR_CODE, 500, 500);
            imageViewQrCode.setImageBitmap(bitmap);
            final AlertDialog qrCodeDialog = createGenericDialog(getString(R.string.title_dialog_comanda_sam_qrcode),
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