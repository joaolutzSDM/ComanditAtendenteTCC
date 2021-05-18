package br.com.alloy.comanditatendente.service.model;

import java.io.Serializable;

import br.com.alloy.comanditatendente.service.model.enums.TipoMensagem;

public class ComandaMensagem implements Serializable {

    private Integer idComandaMensagem;
    private Comanda idComanda;
    private TipoMensagem tipoMensagem;
    private String mensagem;
    private Boolean recebido;

    public ComandaMensagem(Comanda idComanda) {
        this.idComanda = idComanda;
    }

    public ComandaMensagem(Comanda idComanda, TipoMensagem tipoMensagem) {
        this(idComanda);
        this.tipoMensagem = tipoMensagem;
    }

    /**
     * @return the idComandaMensagem
     */
    public Integer getIdComandaMensagem() {
        return idComandaMensagem;
    }
    /**
     * @param idComandaMensagem the idComandaMensagem to set
     */
    public void setIdComandaMensagem(Integer idComandaMensagem) {
        this.idComandaMensagem = idComandaMensagem;
    }
    /**
     * @return the idComanda
     */
    public Comanda getIdComanda() {
        return idComanda;
    }
    /**
     * @param idComanda the idComanda to set
     */
    public void setIdComanda(Comanda idComanda) {
        this.idComanda = idComanda;
    }
    /**
     * @return the tipoMensagem
     */
    public TipoMensagem getTipoMensagem() {
        return tipoMensagem;
    }
    /**
     * @param tipoMensagem the tipoMensagem to set
     */
    public void setTipoMensagem(TipoMensagem tipoMensagem) {
        this.tipoMensagem = tipoMensagem;
    }
    /**
     * @return the mensagem
     */
    public String getMensagem() {
        return mensagem;
    }
    /**
     * @param mensagem the mensagem to set
     */
    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }
    /**
     * @return the recebido
     */
    public Boolean getRecebido() {
        return recebido;
    }
    /**
     * @param recebido the recebido to set
     */
    public void setRecebido(Boolean recebido) {
        this.recebido = recebido;
    }

}
