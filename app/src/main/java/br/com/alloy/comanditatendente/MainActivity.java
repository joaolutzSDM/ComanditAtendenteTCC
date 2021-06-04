package br.com.alloy.comanditatendente;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import br.com.alloy.comanditatendente.databinding.ActivityMainBinding;
import br.com.alloy.comanditatendente.service.RetrofitConfig;
import br.com.alloy.comanditatendente.service.exception.APIException;
import br.com.alloy.comanditatendente.service.exception.ExceptionUtils;
import br.com.alloy.comanditatendente.service.model.ComandaMensagem;
import br.com.alloy.comanditatendente.service.model.enums.TipoMensagem;
import br.com.alloy.comanditatendente.ui.comandas.ComandasViewModel;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private ComandasViewModel comandasViewModel;
    private NotificationManager mNotificationManager;
    private Timer timer;
    private final List<TipoMensagem> tiposMensagem = Collections.singletonList(TipoMensagem.CHAMAR_ATENDENTE);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        //instancia o ViewModel da comanda na classe MainActivity para estar assessível em nas telas filhas do app
        RetrofitConfig.initiateRetrofitAPI(this);
        comandasViewModel = new ViewModelProvider(this).get(ComandasViewModel.class);
        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment);
        NavController navController = Objects.requireNonNull(navHostFragment).getNavController();
        //Removido método que sincronizava o bottomNavigation com o ActionBar do aplicativo
        //NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel();
        }
        if (timer == null) {
            setNotificationTimer();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        CharSequence name = getString(R.string.app_name);
        String description = getString(R.string.notification_channel_description);
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel channel = new NotificationChannel(getString(R.string.notification_channel_id), name, importance);
        channel.setDescription(description);
        // Register the channel with the system; you can't change the importance
        // or other notification behaviors after this
        mNotificationManager.createNotificationChannel(channel);
    }

    private void setNotificationTimer() {
        timer = new Timer("Notification Timer");
        Handler handler = new Handler(); //contador de tempo
        //cria o timer para consulta de notificações
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(() -> {
                    consultarMensagens();
                });
            }
        }, 5000, 10000);
    }

    @Override
    protected void onDestroy() {
        timer.cancel();
        if(mNotificationManager != null) {
            mNotificationManager.cancelAll();
        }
        super.onDestroy();
    }

    private void consultarMensagens() {
        RetrofitConfig.getComanditAPI().consultarMensagensComandas(tiposMensagem).enqueue(new Callback<List<ComandaMensagem>>() {
            @Override
            public void onResponse(Call<List<ComandaMensagem>> call, Response<List<ComandaMensagem>> response) {
                if(response.isSuccessful()) {
                    if(!response.body().isEmpty()) {
                        for (ComandaMensagem comandaMensagem : response.body()) {
                            createComandaNotification(comandaMensagem);
                        }
                    }
                } else {
                    showAPIException(ExceptionUtils.parseException(response));
                }
            }

            @Override
            public void onFailure(Call<List<ComandaMensagem>> call, Throwable t) {
                Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void createComandaNotification(ComandaMensagem comandaMensagem) {
        // Create an explicit intent for an Activity in your app
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("comandaMensagem", comandaMensagem);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this, getString(R.string.notification_channel_id))
                        .setSmallIcon(R.drawable.ic_baseline_notifications_active_24)
                        .setContentTitle(getString(R.string.notification_title))
                        .setContentText(comandaMensagem.getMensagem())
                        .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE)
                        .setContentIntent(pendingIntent).setPriority(1);

        // mId allows you to update the notification later on.
        mNotificationManager.notify(getString(R.string.app_name), comandaMensagem.getComanda().getIdComanda(), mBuilder.build());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Bundle extras = intent.getExtras();
        if (extras != null && extras.containsKey("comandaMensagem")) {
            ComandaMensagem comandaMensagem = (ComandaMensagem) extras.getSerializable("comandaMensagem");
            cancelarMensagem(comandaMensagem);
        }
    }

    private void cancelarMensagem(ComandaMensagem comandaMensagem) {
        RetrofitConfig.getComanditAPI().cancelarMensagemComanda(comandaMensagem).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()) {
                    comandasViewModel.setComanda(comandaMensagem.getComanda());
                    if(binding.navView.getSelectedItemId() == binding.navView.getMenu().findItem(R.id.navigation_comandas).getItemId()) {
                        binding.navView.setSelectedItemId(binding.navView.getMenu().findItem(R.id.navigation_pedidos).getItemId());
                    }
                    binding.navView.getMenu().findItem(R.id.navigation_pedidos).setTitle(
                            String.format(getString(R.string.pedidos_comanda_title), comandaMensagem.getComanda().getIdComanda()));
                    Toast.makeText(MainActivity.this, String.format(getString(R.string.chamado_cancelado),
                            comandaMensagem.getComanda().getIdComanda()), Toast.LENGTH_SHORT).show();
                } else {
                    showAPIException(ExceptionUtils.parseException(response));
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showAPIException(APIException e) {
        Log.e(getString(R.string.api_exception), e.getMessage());
        Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
    }

}