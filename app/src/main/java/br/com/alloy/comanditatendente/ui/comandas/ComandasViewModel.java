package br.com.alloy.comanditatendente.ui.comandas;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import br.com.alloy.comanditatendente.service.model.Comanda;

public class ComandasViewModel extends ViewModel {

    private MutableLiveData<List<Comanda>> comandas;
    private MutableLiveData<Comanda> comanda;
    private MutableLiveData<Integer> mesa;
    private MutableLiveData<Integer> qtdMesas;
    private MutableLiveData<CharSequence[]> mesas;

    public ComandasViewModel() {
        comandas = new MutableLiveData<>();
        comanda = new MutableLiveData<>();
        mesa = new MutableLiveData<>();
        qtdMesas = new MutableLiveData<>();
        mesas = new MutableLiveData<>();
    }

    public LiveData<List<Comanda>> getComandas() {
        return comandas;
    }

    public void setComandas(List<Comanda> comandas) {
        this.comandas.setValue(comandas);
    }

    public LiveData<Comanda> getComanda() {
        return comanda;
    }

    public Comanda getComandaValue() {
        return comanda.getValue();
    }

    public void setComanda(Comanda comanda) {
        this.comanda.setValue(comanda);
    }

    public LiveData<Integer> getMesa() {
        return mesa;
    }

    public Integer getMesaValue() {
        return mesa.getValue();
    }

    public void setMesa(Integer mesa) {
        this.mesa.setValue(mesa);
    }

    public boolean isComandaSelected() {
        return comanda.getValue() != null;
    }

    public boolean isMesaSelected() {
        return mesa.getValue() != null;
    }

    public CharSequence[] getMesas() {
        return mesas.getValue();
    }

    public void setMesas(CharSequence[] mesas) {
        this.mesas.setValue(mesas);
    }

    public LiveData<Integer> getQtdMesas() {
        return qtdMesas;
    }

    public void setQtdMesas(Integer qtdMesas) {
        this.qtdMesas.setValue(qtdMesas);
    }

}