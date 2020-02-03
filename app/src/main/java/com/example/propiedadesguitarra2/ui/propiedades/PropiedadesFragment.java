package com.example.propiedadesguitarra2.ui.propiedades;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.propiedadesguitarra2.NumberConverter;
import com.example.propiedadesguitarra2.R;
import com.example.propiedadesguitarra2.StateManager;
import com.example.propiedadesguitarra2.components.SimpleTextComponent;


public class PropiedadesFragment extends Fragment {
    private PropiedadesViewModel propiedadesViewModel;
    private StateManager stateManager;

    private SimpleTextComponent escalaIntensidadText;
    private SimpleTextComponent distanciaEquilibrioResorteText;
    private SimpleTextComponent distanciaEntreNodosText;
    private SimpleTextComponent centroText;
    private SimpleTextComponent maxpText;
    private SimpleTextComponent exppText;
    private SimpleTextComponent ordenMasaText;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        propiedadesViewModel =
                ViewModelProviders.of(this).get(PropiedadesViewModel.class);
        View root = inflater.inflate(R.layout.fragment_propiedades, container, false);

        stateManager = StateManager.get(this.getContext());

        return root;
    }

    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        escalaIntensidadText = new SimpleTextComponent((EditText) getView().findViewById(R.id.escalaIntensidadText));
        escalaIntensidadText.onChange((coef, exp) -> stateManager.state.escalaIntensidad = NumberConverter.serialize(coef, exp));

        distanciaEquilibrioResorteText = new SimpleTextComponent((EditText) getView().findViewById(R.id.distanciaEquilibrioResorteText));
        distanciaEquilibrioResorteText.onChange((coef, exp) -> stateManager.state.distanciaEquilibrioResorte = NumberConverter.serialize(coef, exp));

        distanciaEntreNodosText = new SimpleTextComponent((EditText) getView().findViewById(R.id.distanciaEntreNodosText));
        distanciaEntreNodosText.onChange((coef, exp) -> stateManager.state.distanciaEntreNodos = NumberConverter.serialize(coef, exp));

        centroText = new SimpleTextComponent((EditText) getView().findViewById(R.id.centroText));
        centroText.onChange((coef, exp) -> stateManager.state.centro = NumberConverter.serialize(coef, exp));

        maxpText = new SimpleTextComponent((EditText) getView().findViewById(R.id.maxpText));
        maxpText.onChange((coef, exp) -> stateManager.state.maxp = NumberConverter.serialize(coef, exp));

        exppText = new SimpleTextComponent((EditText) getView().findViewById(R.id.exppText));
        exppText.onChange((coef, exp) -> stateManager.state.expp = NumberConverter.serialize(coef, exp));

        ordenMasaText = new SimpleTextComponent((EditText) getView().findViewById(R.id.ordenMasaText));
        ordenMasaText.onChange((coef, exp) -> stateManager.state.ordenMasa = NumberConverter.serialize(coef, exp));

        updateAll();
    }

    private void updateAll() {
        escalaIntensidadText.update(stateManager.state.escalaIntensidad);
        distanciaEquilibrioResorteText.update(stateManager.state.distanciaEquilibrioResorte);
        distanciaEntreNodosText.update(stateManager.state.distanciaEntreNodos);
        centroText.update(stateManager.state.centro);
        maxpText.update(stateManager.state.maxp);
        exppText.update(stateManager.state.expp);
        ordenMasaText.update(stateManager.state.ordenMasa);
    }
}
