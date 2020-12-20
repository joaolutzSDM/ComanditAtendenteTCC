package br.com.alloy.comanditatendente.ui.comandas;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ComandasViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public ComandasViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is dashboard fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}