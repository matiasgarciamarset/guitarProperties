package com.example.propiedadesguitarra2.ui.propiedadescuerda;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class PropiedadesCuerdaViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public PropiedadesCuerdaViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is home fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}