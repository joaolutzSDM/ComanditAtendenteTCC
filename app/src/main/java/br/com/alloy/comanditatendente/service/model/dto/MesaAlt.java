package br.com.alloy.comanditatendente.service.model.dto;

public class MesaAlt {

    private int mesaOrigem;
    private int mesaDestino;

    public MesaAlt(int mesaOrigem, int mesaDestino) {
        this.mesaOrigem = mesaOrigem;
        this.mesaDestino = mesaDestino;
    }

    /**
     * @return the mesaOrigem
     */
    public int getMesaOrigem() {
        return mesaOrigem;
    }
    /**
     * @param mesaOrigem the mesaOrigem to set
     */
    public void setMesaOrigem(int mesaOrigem) {
        this.mesaOrigem = mesaOrigem;
    }
    /**
     * @return the mesaDestino
     */
    public int getMesaDestino() {
        return mesaDestino;
    }
    /**
     * @param mesaDestino the mesaDestino to set
     */
    public void setMesaDestino(int mesaDestino) {
        this.mesaDestino = mesaDestino;
    }

}
