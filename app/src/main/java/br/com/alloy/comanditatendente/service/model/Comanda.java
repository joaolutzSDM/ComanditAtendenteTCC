package br.com.alloy.comanditatendente.service.model;

import java.io.Serializable;
import java.util.Date;

public class Comanda implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private Integer idComanda;
    private Integer numeroMesa;
    private String senhaAcessoMobile;
    private Date horaAlteracao;

    public Comanda() {}

    public Comanda(Integer idComanda) {
        this.idComanda = idComanda;
    }

    public Comanda(Integer idComanda, String senhaAcessoMobile) {
        this.idComanda = idComanda;
        this.senhaAcessoMobile = senhaAcessoMobile;
    }

    public Integer getIdComanda() {
        return idComanda;
    }

    public void setIdComanda(Integer idComanda) {
        this.idComanda = idComanda;
    }

    public Integer getNumeroMesa() {
        return numeroMesa;
    }

    public void setNumeroMesa(Integer numeroMesa) {
        this.numeroMesa = numeroMesa;
    }

    public String getSenhaAcessoMobile() {
        return senhaAcessoMobile;
    }

    public void setSenhaAcessoMobile(String senhaAcessoMobile) {
        this.senhaAcessoMobile = senhaAcessoMobile;
    }

    public Date getHoraAlteracao() {
        return horaAlteracao;
    }

    public void setHoraAlteracao(Date horaAlteracao) {
        this.horaAlteracao = horaAlteracao;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((idComanda == null) ? 0 : idComanda.hashCode());
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
        Comanda other = (Comanda) obj;
        if (idComanda == null) {
            return other.idComanda == null;
        } else return idComanda.equals(other.idComanda);
    }

}
