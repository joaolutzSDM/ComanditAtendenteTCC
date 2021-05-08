package br.com.alloy.comanditatendente.service;

import android.content.Context;

import br.com.alloy.comanditatendente.R;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitConfig {

    private static ComanditAPI comanditAPI;

    public static void initiateRetrofitAPI(Context context) {
        comanditAPI = new Retrofit.Builder()
                .baseUrl(context.getString(R.string.comandit_api_url))
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(ComanditAPI.class);
    }

    public static ComanditAPI getComanditAPI(Context context) {
        return comanditAPI;
    }

}
