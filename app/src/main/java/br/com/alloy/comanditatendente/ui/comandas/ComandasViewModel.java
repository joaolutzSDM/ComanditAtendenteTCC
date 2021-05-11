package br.com.alloy.comanditatendente.ui.comandas;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;
import java.util.Objects;

import br.com.alloy.comanditatendente.service.model.Comanda;

public class ComandasViewModel extends ViewModel {

    private MutableLiveData<List<Comanda>> comandas;
    private MutableLiveData<Comanda> comanda;
    private MutableLiveData<Integer> mesa;
    private CharSequence[] mesas;

    public ComandasViewModel() {
        comandas = new MutableLiveData<>();
        comanda = new MutableLiveData<>();
        mesa = new MutableLiveData<>();
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

    public Comanda getComandaForRequest() {
        return new Comanda(Objects.requireNonNull(comanda.getValue()).getIdComanda(),
                comanda.getValue().getSenhaAcessoMobile());
    }

    public void setComanda(Comanda comanda) {
        this.comanda.setValue(comanda);
    }

    public LiveData<Integer> getMesa() {
        return mesa;
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
        return mesas;
    }

    public void setMesas(CharSequence[] mesas) {
        this.mesas = mesas;
    }

}