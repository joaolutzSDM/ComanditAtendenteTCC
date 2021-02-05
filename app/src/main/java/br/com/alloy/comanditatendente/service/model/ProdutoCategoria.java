package br.com.alloy.comanditatendente.service.model;

import java.io.Serializable;

public class ProdutoCategoria implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private Integer idProdutoCategoria;
    private String nomeCategoria;
    private String nomeCategoriaCardapio;
    private Boolean ativo;

    public Integer getIdProdutoCategoria() {
        return idProdutoCategoria;
    }
    public void setIdProdutoCategoria(Integer idProdutoCategoria) {
        this.idProdutoCategoria = idProdutoCategoria;
    }
    public String getNomeCategoria() {
        return nomeCategoria;
    }
    public void setNomeCategoria(String nomeCategoria) {
        this.nomeCategoria = nomeCategoria;
    }
    public String getNomeCategoriaCardapio() {
        return nomeCategoriaCardapio;
    }
    public void setNomeCategoriaCardapio(String nomeCategoriaCardapio) {
        this.nomeCategoriaCardapio = nomeCategoriaCardapio;
    }
    public Boolean getAtivo() {
        return ativo;
    }
    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((idProdutoCategoria == null) ? 0 : idProdutoCategoria.hashCode());
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
        ProdutoCategoria other = (ProdutoCategoria) obj;
        if (idProdutoCategoria == null) {
            return other.idProdutoCategoria == null;
        } else return idProdutoCategoria.equals(other.idProdutoCategoria);
    }
    @Override
    public String toString() {
        return "ProdutoCategoria [idProdutoCategoria=" + idProdutoCategoria + ", nomeCategoria=" + nomeCategoria
                + ", nomeCategoriaCardapio=" + nomeCategoriaCardapio + ", ativo=" + ativo + "]";
    }

}
