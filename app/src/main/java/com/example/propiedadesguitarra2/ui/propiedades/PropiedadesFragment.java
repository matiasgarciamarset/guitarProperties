package com.example.propiedadesguitarra2.ui.propiedades;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.propiedadesguitarra2.R;
import com.example.propiedadesguitarra2.StateManager;
import com.example.propiedadesguitarra2.components.SimpleTextComponent;
import com.example.propiedadesguitarra2.model.Pair;


public class PropiedadesFragment extends Fragment {
    private StateManager stateManager;

    private SimpleTextComponent escalaIntensidadText = new SimpleTextComponent();
    private SimpleTextComponent distanciaEquilibrioResorteText = new SimpleTextComponent();
    private SimpleTextComponent distanciaEntreNodosText = new SimpleTextComponent();
    private SimpleTextComponent centroText = new SimpleTextComponent();
    private SimpleTextComponent maxpText = new SimpleTextComponent();
    private SimpleTextComponent exppText = new SimpleTextComponent();
    private SimpleTextComponent ordenMasaText = new SimpleTextComponent();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_propiedades, container, false);

        stateManager = StateManager.get(this.getContext());

        return root;
    }

    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        escalaIntensidadText.setView((EditText) getView().findViewById(R.id.escalaIntensidadText));
        escalaIntensidadText.onChange(v -> {
            stateManager.state.escalaIntensidad = v;
            stateManager.runAlgorithms("escalaIntensidad");
            stateManager.sendValueByBluetooth("escalaIntensidad", v);
        });

        distanciaEquilibrioResorteText.setView((EditText) getView().findViewById(R.id.distanciaEquilibrioResorteText));
        distanciaEquilibrioResorteText.onChange(v -> {
            stateManager.state.distanciaEquilibrioResorte = v;
            stateManager.runAlgorithms("distanciaEquilibrioResorte");
            stateManager.sendValueByBluetooth("distanciaEquilibrioResorte", v);
        });

        distanciaEntreNodosText.setView((EditText) getView().findViewById(R.id.distanciaEntreNodosText));
        distanciaEntreNodosText.onChange(v -> {
            stateManager.state.distanciaEntreNodos = v;
            stateManager.runAlgorithms("distanciaEntreNodos");
            stateManager.sendValueByBluetooth("distanciaEntreNodos", v);
        });

        centroText.setView((EditText) getView().findViewById(R.id.centroText));
        centroText.onChange(v -> {
            stateManager.state.centro = v;
            stateManager.runAlgorithms("centro");
            stateManager.sendValueByBluetooth("centro", v);
        });

        maxpText.setView((EditText) getView().findViewById(R.id.maxpText));
        maxpText.onChange(v -> {
            stateManager.state.maxp = v;
            stateManager.runAlgorithms("maxp");
            stateManager.sendValueByBluetooth("maxp", v);
        });

        exppText.setView((EditText) getView().findViewById(R.id.exppText));
        exppText.onChange(v -> {
            stateManager.state.expp = v;
            stateManager.runAlgorithms("expp");
            stateManager.sendValueByBluetooth("expp", v);
        });

        ordenMasaText.setView((EditText) getView().findViewById(R.id.ordenMasaText));
        ordenMasaText.onChange(v -> {
            stateManager.state.ordenMasa = v;
            stateManager.runAlgorithms("ordenMasa");
            stateManager.sendValueByBluetooth("ordenMasa", v);
        });

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
