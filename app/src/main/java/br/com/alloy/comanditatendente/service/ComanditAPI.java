package br.com.alloy.comanditatendente.service;

import java.util.List;

import br.com.alloy.comanditatendente.service.model.Comanda;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ComanditAPI {

    //-----Comandas-----

    @GET("comanda/consultar")
    Call<List<Comanda>> consultarComandas();

    @GET("comanda/consultar/abertas")
    Call<List<Comanda>> consultarComandasAbertas();

    @GET("comanda/consultar/fechadas")
    Call<List<Comanda>> consultarComandasFechadas();

    @GET("comanda/consultar/mesa/{mesa}")
    Call<List<Comanda>> consultarPorNumeroMesa(@Path("mesa") Integer mesa);

    @PUT("comanda/fechar")
    Call<List<Comanda>> consultarPorNumeroMesa(@Body Comanda comanda);

}
