package br.com.alloy.comanditatendente;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import br.com.alloy.comanditatendente.databinding.ActivityMainBinding;
import br.com.alloy.comanditatendente.ui.comandas.ComandasViewModel;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        //instancia o ViewModel da comanda na classe MainActivity para estar assessível em nas telas filhas do app
        ComandasViewModel comandaViewModel = new ViewModelProvider(this).get(ComandasViewModel.class);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_comandas, R.id.navigation_pedidos).build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        //Removido método que sincronizava o bottomNavigation com o ActionBar do aplicativo
        //NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);
    }

}