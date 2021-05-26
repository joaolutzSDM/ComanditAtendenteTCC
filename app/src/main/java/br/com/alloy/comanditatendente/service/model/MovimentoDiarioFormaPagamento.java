package br.com.alloy.comanditatendente.service.model;

import java.io.Serializable;
import java.math.BigDecimal;

public class MovimentoDiarioFormaPagamento implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private Integer idFormaPagto;
    private String nomeFormaPagto;
    private BigDecimal percentualAliquota;

    /**
     *
     */
    public MovimentoDiarioFormaPagamento() {
    }

    /**
     * @param idFormaPagto
     */
    public MovimentoDiarioFormaPagamento(Integer idFormaPagto) {
        this.idFormaPagto = idFormaPagto;
    }

    /**
     * @param idFormaPagto
     * @param nomeFormaPagto
     */
    public MovimentoDiarioFormaPagamento(Integer idFormaPagto, String nomeFormaPagto) {
        this(idFormaPagto);
        this.nomeFormaPagto = nomeFormaPagto;
    }

    /**
     * @param idFormaPagto
     * @param nomeFormaPagto
     * @param percentualAliquota
     */
    public MovimentoDiarioFormaPagamento(Integer idFormaPagto, String nomeFormaPagto, BigDecimal percentualAliquota) {
        this(idFormaPagto, nomeFormaPagto);
        this.percentualAliquota = percentualAliquota;
    }

    /**
     * @return the idFormaPagto
     */
    public Integer getIdFormaPagto() {
        return idFormaPagto;
    }

    /**
     * @param idFormaPagto the idFormaPagto to set
     */
    public void setIdFormaPagto(Integer idFormaPagto) {
        this.idFormaPagto = idFormaPagto;
    }

    /**
     * @return the nomeFormaPagto
     */
    public String getNomeFormaPagto() {
        return nomeFormaPagto;
    }

    /**
     * @param nomeFormaPagto the nomeFormaPagto to set
     */
    public void setNomeFormaPagto(String nomeFormaPagto) {
        this.nomeFormaPagto = nomeFormaPagto;
    }

    /**
     * @return the percentualAliquota
     */
    public BigDecimal getPercentualAliquota() {
        return percentualAliquota;
    }

    /**
     * @param percentualAliquota the percentualAliquota to set
     */
    public void setPercentualAliquota(BigDecimal percentualAliquota) {
        this.percentualAliquota = percentualAliquota;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((idFormaPagto == null) ? 0 : idFormaPagto.hashCode());
        return result;
    }

    /**
     * @param obj
     * @return
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final MovimentoDiarioFormaPagamento other = (MovimentoDiarioFormaPagamento) obj;
        return this.idFormaPagto.equals(other.getIdFormaPagto());
    }

    @Override
    public String toString() {
        return nomeFormaPagto;
    }

}
