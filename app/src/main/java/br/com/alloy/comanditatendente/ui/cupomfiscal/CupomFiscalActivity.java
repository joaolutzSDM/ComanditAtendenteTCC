package br.com.alloy.comanditatendente.ui.cupomfiscal;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import br.com.alloy.comanditatendente.R;
import br.com.alloy.comanditatendente.databinding.ActivityCupomFiscalBinding;
import br.com.alloy.comanditatendente.service.RetrofitConfig;
import br.com.alloy.comanditatendente.service.exception.APIException;
import br.com.alloy.comanditatendente.service.exception.ExceptionUtils;
import br.com.alloy.comanditatendente.service.model.Comanda;
import br.com.alloy.comanditatendente.service.model.MovimentoDiarioFormaPagamento;
import br.com.alloy.comanditatendente.service.model.dto.ComandaPagamento;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CupomFiscalActivity extends AppCompatActivity {

    private ActivityCupomFiscalBinding binding;
    private ProgressDialog progressDialog;
    private Comanda comanda;
    private static List<MovimentoDiarioFormaPagamento> formasPagamento;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCupomFiscalBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        comanda = (Comanda) getIntent().getExtras().getSerializable("comanda");
        setListeners();
        carregarCupomFiscal();
    }

    private void setListeners() {
        binding.wvCupomFiscal.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }

            @SuppressLint("RestrictedApi")
            @Override
            public void onPageFinished(WebView view, String url) {
                binding.fabShareCupomFiscal.setVisibility(View.VISIBLE);
            }
        });

        binding.fabShareCupomFiscal.setOnClickListener(view -> {
            createWebPrintJob();
        });
    }

    //create a function to create the print job
    private void createWebPrintJob() {
        PrintManager printManager = (PrintManager) this.getSystemService(Context.PRINT_SERVICE);
        PrintDocumentAdapter printAdapter = binding.wvCupomFiscal.createPrintDocumentAdapter(String.format(getString(R.string.cf_pdf_file_name), comanda.getIdComanda()));
        printManager.print("cupomNaoFiscalPrintPDF", printAdapter, new PrintAttributes.Builder().build());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_fechamento_comanda, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Ação do botão Home/Up (<-)
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        } else {
            fecharComanda();
        }
        return true;
    }

    private void fecharComanda() {
        if(formasPagamento == null) {
            RetrofitConfig.getComanditAPI().consultarFormasDePagamento().enqueue(new Callback<List<MovimentoDiarioFormaPagamento>>() {
                @Override
                public void onResponse(Call<List<MovimentoDiarioFormaPagamento>> call, Response<List<MovimentoDiarioFormaPagamento>> response) {
                    if(response.isSuccessful()) {
                        formasPagamento = response.body();
                        mostrarOpcoesPagamento();
                    } else {
                        showAPIException(ExceptionUtils.parseException(response));
                    }
                }

                @Override
                public void onFailure(Call<List<MovimentoDiarioFormaPagamento>> call, Throwable t) {
                    showFinishDialog(null);
                }
            });
        } else {
            mostrarOpcoesPagamento();
        }
    }

    private void mostrarOpcoesPagamento() {
        showListDialog(getString(R.string.fechamento_escolha_forma_pagamento), formasPagamento, (dialog, which) -> {
            cadastrarPagamentoComanda(formasPagamento.get(which));
        });
    }

    private void cadastrarPagamentoComanda(MovimentoDiarioFormaPagamento movimentoDiarioFormaPagamento) {
        ComandaPagamento comandaPagamento = new ComandaPagamento(comanda, movimentoDiarioFormaPagamento);
        RetrofitConfig.getComanditAPI().cadastrarPagamentoComanda(comandaPagamento).enqueue(new Callback<Comanda>() {
            @Override
            public void onResponse(Call<Comanda> call, Response<Comanda> response) {
                if(response.isSuccessful()) {
                    Toast.makeText(CupomFiscalActivity.this, R.string.pagamento_registrado, Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    showAPIException(ExceptionUtils.parseException(response));
                }
            }

            @Override
            public void onFailure(Call<Comanda> call, Throwable t) {
                showFinishDialog(null);
            }
        });
    }

    @SuppressLint("RestrictedApi")
    private void carregarCupomFiscal() {
        progressDialog = ProgressDialog.show(this, getString(R.string.app_name),
                getString(R.string.loading), true);
        RetrofitConfig.getComanditAPI().consultarPedidosCupomFiscal(comanda).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()) {
                    try {
                        binding.wvCupomFiscal.loadDataWithBaseURL(null, response.body().string(), "text/html", "UTF-8", null);
                    } catch (IOException e) {
                        showFinishDialog(getString(R.string.erro_formatacao_cupom_fiscal));
                    }
                } else {
                    showAPIException(ExceptionUtils.parseException(response));
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                progressDialog.dismiss();
                showFinishDialog(null);
            }
        });
    }

    private void showListDialog(String title, List<MovimentoDiarioFormaPagamento> items, DialogInterface.OnClickListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setAdapter(new ArrayAdapter<>(this, R.layout.produto_categoria_item, items), listener);
        builder.show();
    }

    private void showAPIException(APIException e) {
        Log.e(getString(R.string.api_exception), e.getMessage());
        Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
    }

    private void showFinishDialog(@Nullable String message) {
        final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle(getString(R.string.app_name));
        alertDialog.setMessage(message == null ? getString(R.string.msgError) : message);
        alertDialog.setIcon(R.mipmap.ic_launcher);
        alertDialog.setCancelable(false);
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, getString(R.string.btnOK), (dialog, which) -> finish());
        alertDialog.show();
    }

}
