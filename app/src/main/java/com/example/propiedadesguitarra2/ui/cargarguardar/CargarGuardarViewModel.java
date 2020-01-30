package com.example.propiedadesguitarra2.ui.cargarguardar;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class CargarGuardarViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public CargarGuardarViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is notifications fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}