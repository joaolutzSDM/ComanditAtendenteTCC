package br.com.alloy.comanditatendente.service;

import java.util.List;

import br.com.alloy.comanditatendente.service.model.Comanda;
import br.com.alloy.comanditatendente.service.model.MesaAlt;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
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
    Call<List<Comanda>> consultarComandasMesa(@Path("mesa") Integer mesa);

    @PUT("comanda/abrir")
    Call<Comanda> abrirComanda(@Body Comanda comanda);

    @PUT("comanda/fechar")
    Call<Comanda> fecharComanda(@Body Comanda comanda);

    @PUT("comanda/alterarMesa")
    Call<String> alterarMesa(@Body MesaAlt mesaAlt);

    //-----Configuração-----

    //RECUPERAR_CONFIGURACAO_POR_NOME

    // -----Pedidos-----

//            CONSULTAR_PEDIDOS_COMANDA,
//            CONSULTAR_PEDIDOS_CUPOM_FISCAL,
//            CADASTRAR_PEDIDO,
//            CANCELAR_PEDIDO,
//            TRANSFERIR_PEDIDO,

}
