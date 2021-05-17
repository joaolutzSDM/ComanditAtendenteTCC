package br.com.alloy.comanditatendente;

import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import java.util.Objects;

import br.com.alloy.comanditatendente.databinding.ActivityMainBinding;
import br.com.alloy.comanditatendente.service.RetrofitConfig;
import br.com.alloy.comanditatendente.ui.comandas.ComandasViewModel;

public class MainActivity extends AppCompatActivity implements  Runnable {

    private NavController navController;

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
        navController = Objects.requireNonNull(navHostFragment).getNavController();
        //Removido método que sincronizava o bottomNavigation com o ActionBar do aplicativo
        //NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

        Handler handler = new Handler(); //contador de tempo
        handler.postDelayed(this, 2000); //o exemplo 2000 = 2 segundos

    }

    @Override
    public void run() {
        Toast.makeText(this, "Executou o timer", Toast.LENGTH_SHORT).show();
    }

}