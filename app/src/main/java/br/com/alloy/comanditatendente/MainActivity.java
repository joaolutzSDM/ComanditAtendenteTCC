package br.com.alloy.comanditatendente;

import android.os.Bundle;
import android.view.MenuItem;

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

public class MainActivity extends AppCompatActivity {

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
    }

    public void navigateToFragment(MenuItem menuItem) {
        NavigationUI.onNavDestinationSelected(menuItem, navController);
//        Navigation.findNavController(this, R.id.nav_host_fragment);
//        Navigation.findNavController(view).navigate(R.id.action_navigation_comandas_to_navigation_pedidos);
    }

}