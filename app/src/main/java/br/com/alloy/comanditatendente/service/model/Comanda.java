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
    private Date horaUltimoPedido;

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

    public Date getHoraUltimoPedido() {
        return horaUltimoPedido;
    }

    public void setHoraUltimoPedido(Date horaUltimoPedido) {
        this.horaUltimoPedido = horaUltimoPedido;
    }

    /**
     *
     * @return o status da comanda - <b>true</b> para aberta, <b>false</b> para fechada
     */
    public boolean isOpen() {
        return numeroMesa != null;
    }

    public boolean hasPedidos() {
        return horaUltimoPedido != null;
    }

    /**
     *
     * @return
     */
//    public boolean hasOrders() {
//        return (qtdPedidos != null && qtdPedidos > 0);
//    }

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

    @Override
    public String toString() {
        return idComanda.toString();
    }

    /**
     * @return string do c??digo para gera????o de QR CODE no app Comandit Atendente
     */
    public String getQRCodeString() {
        return "Comandit-" + idComanda.toString() + "-" + senhaAcessoMobile;
    }

}
