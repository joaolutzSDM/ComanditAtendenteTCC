package br.com.alloy.comanditatendente.service.model;

import java.io.Serializable;
import java.util.Date;

public class PedidoHistorico implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private Long idPedidoHistorico;
    private Date dataPedido;
    private Produto produto;
    private Integer quantidadePedidoHistorico;

    public PedidoHistorico() {
    }

    public PedidoHistorico(Long idPedidoHistorico) {
        this.idPedidoHistorico = idPedidoHistorico;
    }

    public Long getIdPedidoHistorico() {
        return idPedidoHistorico;
    }

    public void setIdPedidoHistorico(Long idPedidoHistorico) {
        this.idPedidoHistorico = idPedidoHistorico;
    }

    public Date getDataPedido() {
        return dataPedido;
    }

    public void setDataPedido(Date dataPedido) {
        this.dataPedido = dataPedido;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public Integer getQuantidadePedidoHistorico() {
        return quantidadePedidoHistorico;
    }

    public void setQuantidadePedidoHistorico(Integer quantidadePedidoHistorico) {
        this.quantidadePedidoHistorico = quantidadePedidoHistorico;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((idPedidoHistorico == null) ? 0 : idPedidoHistorico.hashCode());
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
        PedidoHistorico other = (PedidoHistorico) obj;
        if (idPedidoHistorico == null) {
            return other.idPedidoHistorico == null;
        } else return idPedidoHistorico.equals(other.idPedidoHistorico);
    }

    @Override
    public String toString() {
        return "PedidoHistorico [idPedidoHistorico=" + idPedidoHistorico + ", dataPedido=" + dataPedido + ", produto="
                + produto + ", quantidadePedidoHistorico=" + quantidadePedidoHistorico + "]";
    }

}
