package br.com.alloy.comanditatendente.service;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import br.com.alloy.comanditatendente.R;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitConfig {

    private static ComanditAPI comanditAPI;

    public static void initiateRetrofitAPI(Context context) {
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ").create();
        comanditAPI = new Retrofit.Builder()
                .baseUrl(context.getString(R.string.comandit_api_url))
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build().create(ComanditAPI.class);
    }

    public static ComanditAPI getComanditAPI() {
        return comanditAPI;
    }

}
