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


public class PropiedadesFragment extends Fragment {
    private StateManager stateManager;

    private SimpleTextComponent escalaIntensidadText = new SimpleTextComponent(true, 0f, 100f);
    private SimpleTextComponent distanciaEquilibrioResorteText = new SimpleTextComponent(true, 0f, null);
    private SimpleTextComponent distanciaEntreNodosText = new SimpleTextComponent(true, 0f, null);
    private SimpleTextComponent centroText = new SimpleTextComponent(true, 0f, .5f);
    private SimpleTextComponent maxpText = new SimpleTextComponent(true, 0f, 1f);
    private SimpleTextComponent exppText = new SimpleTextComponent(true, 0f, null);
    private SimpleTextComponent ordenMasaText = new SimpleTextComponent(true, 0f, .5f);
    private SimpleTextComponent cantCuerdas =  new SimpleTextComponent(false, 0f, 50f);

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

        cantCuerdas.setView((EditText) getView().findViewById(R.id.cantCuerdas));
        cantCuerdas.onFocusChange(v -> {
            // Defino valores por defecto para nuevas cuerdas
            for (int i = stateManager.state.cantCuerdas.intValue(); i < v; ++i)
                stateManager.state.setDefaultValues(i);
            // Si quito cuerdas, elimino las cuerdas correspondientes
            for (int i = stateManager.state.cantCuerdas.intValue() - 1; v.intValue() < i; --i)
                stateManager.state.cuerdas.remove(i);

            stateManager.state.cantCuerdas = v;
            stateManager.runAlgorithms("cantCuerdas");
            stateManager.sendValueByBluetooth("cantCuerdas", v);
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
        cantCuerdas.update(stateManager.state.cantCuerdas);
    }
}
