package br.com.alloy.comanditatendente.service.model;

import java.io.Serializable;
import java.math.BigDecimal;

public class Produto implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private Integer idProduto;
    private String nomeProduto;
    private String nomeProdutoCardapio;
    private String ingredientesProdutoCardapio;
    private BigDecimal valorProduto;
    private Boolean disponivel = Boolean.TRUE;
    private Boolean ativo = Boolean.TRUE;
    private ProdutoCategoria produtoCategoria;

    public Produto() {}

    public Produto(Integer idProduto, String nomeProduto, BigDecimal valorProduto) {
        this.idProduto = idProduto;
        this.nomeProduto = nomeProduto;
        this.valorProduto = valorProduto;
    }

    public Integer getIdProduto() {
        return idProduto;
    }
    public void setIdProduto(Integer idProduto) {
        this.idProduto = idProduto;
    }
    public String getNomeProduto() {
        return nomeProduto;
    }
    public void setNomeProduto(String nomeProduto) {
        this.nomeProduto = nomeProduto;
    }
    public String getNomeProdutoCardapio() {
        return nomeProdutoCardapio;
    }
    public void setNomeProdutoCardapio(String nomeProdutoCardapio) {
        this.nomeProdutoCardapio = nomeProdutoCardapio;
    }
    public String getIngredientesProdutoCardapio() {
        return ingredientesProdutoCardapio;
    }
    public void setIngredientesProdutoCardapio(String ingredientesProdutoCardapio) {
        this.ingredientesProdutoCardapio = ingredientesProdutoCardapio;
    }
    public BigDecimal getValorProduto() {
        return valorProduto;
    }
    public void setValorProduto(BigDecimal valorProduto) {
        this.valorProduto = valorProduto;
    }
    public Boolean getDisponivel() {
        return disponivel;
    }
    public void setDisponivel(Boolean disponivel) {
        this.disponivel = disponivel;
    }
    public Boolean getAtivo() {
        return ativo;
    }
    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }
    public ProdutoCategoria getProdutoCategoria() {
        return produtoCategoria;
    }
    public void setProdutoCategoria(ProdutoCategoria produtoCategoria) {
        this.produtoCategoria = produtoCategoria;
    }
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((idProduto == null) ? 0 : idProduto.hashCode());
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
        Produto other = (Produto) obj;
        if (idProduto == null) {
            return other.idProduto == null;
        } else return idProduto.equals(other.idProduto);
    }
    @Override
    public String toString() {
        return "Produto [idProduto=" + idProduto + ", nomeProduto=" + nomeProduto + ", nomeProdutoCardapio="
                + nomeProdutoCardapio + ", ingredientesProdutoCardapio=" + ingredientesProdutoCardapio
                + ", valorProduto=" + valorProduto + ", disponivel=" + disponivel + ", ativo=" + ativo
                + ", produtoCategoria=" + produtoCategoria + "]";
    }

}
