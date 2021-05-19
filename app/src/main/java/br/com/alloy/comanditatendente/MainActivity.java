package br.com.alloy.comanditatendente;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import br.com.alloy.comanditatendente.databinding.ActivityMainBinding;
import br.com.alloy.comanditatendente.service.RetrofitConfig;
import br.com.alloy.comanditatendente.service.exception.APIException;
import br.com.alloy.comanditatendente.service.exception.ExceptionUtils;
import br.com.alloy.comanditatendente.service.model.Comanda;
import br.com.alloy.comanditatendente.service.model.ComandaMensagem;
import br.com.alloy.comanditatendente.ui.comandas.ComandasViewModel;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements  Runnable {

    NotificationManager mNotificationManager;
    private Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        //instancia o ViewModel da comanda na classe MainActivity para estar assessível em nas telas filhas do app
        RetrofitConfig.initiateRetrofitAPI(this);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment);
        NavController navController = Objects.requireNonNull(navHostFragment).getNavController();
        //Removido método que sincronizava o bottomNavigation com o ActionBar do aplicativo
        //NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);
        timer = new Timer("Notification Timer");
        Handler handler = new Handler(); //contador de tempo
        //cria o timer para consulta de notificações
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(this);
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

    @Override
    public void run() {
        Toast.makeText(this, "Executou o timer", Toast.LENGTH_SHORT).show();
    }

    private void createComandaNotification(ComandaMensagem comandaMensagem) {
        // Create an explicit intent for an Activity in your app
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("comandaMensagem", comandaMensagem);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this, "")
                        .setSmallIcon(R.drawable.ic_baseline_notifications_active_24)
                        .setContentTitle(getString(R.string.notification_title))
                        .setContentText(comandaMensagem.getMensagem())
                        .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE)
                        .setContentIntent(pendingIntent);

        if(mNotificationManager == null) {
            mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }

        // mId allows you to update the notification later on.
        mNotificationManager.notify(comandaMensagem.getIdComanda().getIdComanda(), mBuilder.build());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 0) { //é o request de notificação
            ComandaMensagem mensagem = (ComandaMensagem) data.getSerializableExtra("comandaMensagem");
            RetrofitConfig.getComanditAPI().cancelarMensagemComanda(mensagem).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if(response.isSuccessful()) {
                        Toast.makeText(MainActivity.this, response.body().toString(), Toast.LENGTH_SHORT).show();
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
    }

    private void showAPIException(APIException e) {
        Log.e(getString(R.string.api_exception), e.getMessage());
        Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
    }

}