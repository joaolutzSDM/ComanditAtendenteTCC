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
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import br.com.alloy.comanditatendente.R;
import br.com.alloy.comanditatendente.databinding.ActivityCupomFiscalBinding;
import br.com.alloy.comanditatendente.service.RetrofitConfig;
import br.com.alloy.comanditatendente.service.exception.APIException;
import br.com.alloy.comanditatendente.service.exception.ExceptionUtils;
import br.com.alloy.comanditatendente.service.model.Comanda;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CupomFiscalActivity extends AppCompatActivity {

    private ActivityCupomFiscalBinding binding;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCupomFiscalBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Comanda comanda = (Comanda) getIntent().getExtras().getSerializable("comanda");
        setListeners(comanda);
        carregarCupomFiscal(comanda);
    }

    private void setListeners(Comanda comanda) {
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
            createWebPrintJob(comanda);
        });
    }

    //create a function to create the print job
    private void createWebPrintJob(Comanda comanda) {
        PrintManager printManager = (PrintManager) this.getSystemService(Context.PRINT_SERVICE);
        PrintDocumentAdapter printAdapter;
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) { // API 21
            printAdapter = binding.wvCupomFiscal.createPrintDocumentAdapter(String.format(getString(R.string.cf_pdf_file_name), comanda.getIdComanda()));
        } else {
            printAdapter = binding.wvCupomFiscal.createPrintDocumentAdapter();
        }
        printManager.print("cupomNaoFiscalPrintPDF", printAdapter, new PrintAttributes.Builder().build());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Ação do botão Home/Up (<-)
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressLint("RestrictedApi")
    private void carregarCupomFiscal(Comanda comanda) {
        progressDialog = ProgressDialog.show(this, getString(R.string.app_name),
                getString(R.string.loading), true);
        RetrofitConfig.getComanditAPI(this).consultarPedidosCupomFiscal(comanda).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                progressDialog.dismiss();
                if(response.isSuccessful()) {
                    binding.wvCupomFiscal.loadDataWithBaseURL(null, response.body(), "text/html", "UTF-8", null);
                } else {
                    showAPIException(ExceptionUtils.parseException(response));
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                progressDialog.dismiss();
                showFinishDialog(null);
            }
        });
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
