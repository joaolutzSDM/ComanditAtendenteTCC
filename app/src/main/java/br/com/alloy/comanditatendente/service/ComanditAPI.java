package br.com.alloy.comanditatendente.service;

import java.util.List;

import br.com.alloy.comanditatendente.service.model.Comanda;
import br.com.alloy.comanditatendente.service.model.ComandaMensagem;
import br.com.alloy.comanditatendente.service.model.Configuracao;
import br.com.alloy.comanditatendente.service.model.MesaAlt;
import br.com.alloy.comanditatendente.service.model.Pedido;
import br.com.alloy.comanditatendente.service.model.Produto;
import br.com.alloy.comanditatendente.service.model.ProdutoCategoria;
import br.com.alloy.comanditatendente.service.model.enums.TipoMensagem;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
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
    Call<List<Comanda>> consultarComandasMesa(@Path("mesa") Integer mesa);

    @PUT("comanda/abrir")
    Call<Comanda> abrirComanda(@Body Comanda comanda);

    @PUT("comanda/fechar")
    Call<Comanda> fecharComanda(@Body Comanda comanda);

    @PUT("comanda/alterarMesa")
    Call<String> alterarMesa(@Body MesaAlt mesaAlt);

    @POST("comanda/mensagem/cancelar")
    Call<ResponseBody> cancelarMensagemComanda(@Body ComandaMensagem mensagem);

    @POST("comanda/mensagens/consultar")
    Call<List<ComandaMensagem>> consultarMensagensComandas(@Body List<TipoMensagem> tipos);

    //-----Configuração-----

    //RECUPERAR_CONFIGURACAO_POR_NOME
    @POST("configuracao/consultar")
    Call<Configuracao> consultarConfiguracao(@Body Configuracao configuracao);

    // -----Produtos-----

    @GET("produto/consultar/categorias")
    Call<List<ProdutoCategoria>> consultarCategoriasProduto();

    @POST("produto/consultar/categoria")
    Call<List<Produto>> consultarProdutosPorCategoria(@Body ProdutoCategoria produtoCategoria);

    // -----Pedidos-----

    @Headers("msg: cadastrado")
    @POST("pedido/cadastrar")
    Call<Pedido> cadastrarPedido(@Body Pedido pedido);

    @POST("pedido/consultar/comanda/resumo")
    Call<List<Pedido>> consultarPedidosComandaResumo(@Body Comanda comanda);

    @Headers("msg: cancelado")
    @DELETE("pedido/cancelar")
    Call<Pedido> cancelarPedido(@Body Pedido pedido);

    @Headers("msg: transferido")
    @PUT("pedido/transferir")
    Call<Pedido> transferirPedido(@Body Pedido pedido);

    @POST("pedido/consultar/cupomfiscal")
    Call<ResponseBody> consultarPedidosCupomFiscal(@Body Comanda comanda);

}
