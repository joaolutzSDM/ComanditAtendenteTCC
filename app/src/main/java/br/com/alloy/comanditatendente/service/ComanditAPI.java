package br.com.alloy.comanditatendente.service;

import java.util.List;

import br.com.alloy.comanditatendente.service.model.Comanda;
import br.com.alloy.comanditatendente.service.model.ComandaMensagem;
import br.com.alloy.comanditatendente.service.model.Configuracao;
import br.com.alloy.comanditatendente.service.model.MovimentoDiarioFormaPagamento;
import br.com.alloy.comanditatendente.service.model.Pedido;
import br.com.alloy.comanditatendente.service.model.Produto;
import br.com.alloy.comanditatendente.service.model.ProdutoCategoria;
import br.com.alloy.comanditatendente.service.model.dto.ComandaPagamento;
import br.com.alloy.comanditatendente.service.model.dto.MesaAlt;
import br.com.alloy.comanditatendente.service.model.enums.TipoMensagem;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
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

    @HTTP(method = "DELETE", path = "comanda/mensagem/cancelar", hasBody = true)
    Call<ResponseBody> cancelarMensagemComanda(@Body ComandaMensagem mensagem);

    @POST("comanda/mensagem/consultar")
    Call<List<ComandaMensagem>> consultarMensagensComandas(@Body List<TipoMensagem> tipos);

    @POST("comanda/pagamento")
    Call<Comanda> cadastrarPagamentoComanda(@Body ComandaPagamento comandaPagamento);

    //-----Configura????o-----

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
    @HTTP(method = "DELETE", path = "pedido/cancelar", hasBody = true)
    Call<Pedido> cancelarPedido(@Body Pedido pedido);

    @Headers("msg: transferido")
    @PUT("pedido/transferir")
    Call<Pedido> transferirPedido(@Body Pedido pedido);

    @POST("pedido/consultar/cupomfiscal")
    Call<ResponseBody> consultarPedidosCupomFiscal(@Body Comanda comanda);

    // -----Movimento Diario-----

    @GET("movimentoDiario/formaPagamento/consultar")
    Call<List<MovimentoDiarioFormaPagamento>> consultarFormasDePagamento();

}
