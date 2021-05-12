package br.com.alloy.comanditatendente.service.model;

import java.io.Serializable;

public class Configuracao implements Serializable {

    private static final long serialVersionUID = 1L;

    private String chaveConfiguracao;
    private String valorConfiguracao;

    public Configuracao(String chaveConfiguracao) {
        this.chaveConfiguracao = chaveConfiguracao;
    }

    /**
     * @return the chaveConfiguracao
     */
    public String getChaveConfiguracao() {
        return chaveConfiguracao;
    }
    /**
     * @param chaveConfiguracao the chaveConfiguracao to set
     */
    public void setChaveConfiguracao(String chaveConfiguracao) {
        this.chaveConfiguracao = chaveConfiguracao;
    }
    /**
     * @return the valorConfiguracao
     */
    public String getValorConfiguracao() {
        return valorConfiguracao;
    }
    /**
     * @param valorConfiguracao the valorConfiguracao to set
     */
    public void setValorConfiguracao(String valorConfiguracao) {
        this.valorConfiguracao = valorConfiguracao;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((chaveConfiguracao == null) ? 0 : chaveConfiguracao.hashCode());
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
        Configuracao other = (Configuracao) obj;
        if (chaveConfiguracao == null) {
            return other.chaveConfiguracao == null;
        } else return chaveConfiguracao.equals(other.chaveConfiguracao);
    }

    @Override
    public String toString() {
        return "Configuracao [chaveConfiguracao=" + chaveConfiguracao + ", valorConfiguracao=" + valorConfiguracao
                + "]";
    }

}
