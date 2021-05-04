package br.com.alloy.comanditatendente.service.model;

import java.io.Serializable;
import java.math.BigDecimal;

public class Pedido implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    //campo de controle dos itens do pedido numa consulta (índice)
    private Integer item;
    private Long idPedido;
    private Comanda comanda;
    private Produto produto;
    private Integer quantidadePedido;
    private BigDecimal valorPedido;
    private BigDecimal valorTotal;
    private String observacaoPedido;
    private PedidoHistorico pedidoHistorico;

    //Construtor Padrão
    public Pedido() {}

    //Construtor para salvar Pedidos
    public Pedido(Comanda comanda, Produto produto, Integer quantidadePedido) {
        this.comanda = comanda;
        this.produto = produto;
        this.quantidadePedido = quantidadePedido;
    }

    public Integer getItem() {
        return item;
    }
    public void setItem(Integer item) {
        this.item = item;
    }
    public Long getIdPedido() {
        return idPedido;
    }
    public void setIdPedido(Long idPedido) {
        this.idPedido = idPedido;
    }
    public Comanda getComanda() {
        return comanda;
    }
    public void setComanda(Comanda comanda) {
        this.comanda = comanda;
    }
    public Produto getProduto() {
        return produto;
    }
    public void setProduto(Produto produto) {
        this.produto = produto;
    }
    public Integer getQuantidadePedido() {
        return quantidadePedido;
    }
    public void setQuantidadePedido(Integer quantidadePedido) {
        this.quantidadePedido = quantidadePedido;
    }
    public BigDecimal getValorPedido() {
        return valorPedido;
    }
    public void setValorPedido(BigDecimal valorPedido) {
        this.valorPedido = valorPedido;
    }
    public BigDecimal getValorTotal() {
        return valorTotal;
    }
    public void setValorTotal(BigDecimal valorTotal) {
        this.valorTotal = valorTotal;
    }
    public String getObservacaoPedido() {
        return observacaoPedido;
    }
    public void setObservacaoPedido(String observacaoPedido) {
        this.observacaoPedido = observacaoPedido;
    }
    public PedidoHistorico getPedidoHistorico() {
        return pedidoHistorico;
    }
    public void setPedidoHistorico(PedidoHistorico pedidoHistorico) {
        this.pedidoHistorico = pedidoHistorico;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((idPedido == null) ? 0 : idPedido.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Pedido other = (Pedido) obj;
        if (idPedido == null) {
            return other.idPedido == null;
        } else return idPedido.equals(other.idPedido);
    }

    @Override
    public String toString() {
        return "Pedido [item=" + item + ", idPedido=" + idPedido + ", comanda=" + comanda + ", produto=" + produto
                + ", quantidadePedido=" + quantidadePedido + ", valorPedido=" + valorPedido + ", valorTotal="
                + valorTotal + ", observacaoPedido=" + observacaoPedido + ", pedidoHistorico=" + pedidoHistorico + "]";
    }

}
