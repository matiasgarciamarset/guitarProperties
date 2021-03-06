package com.example.propiedadesguitarra2.ui.propiedadescuerda;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.propiedadesguitarra2.R;
import com.example.propiedadesguitarra2.StateManager;
import com.example.propiedadesguitarra2.components.NumberComponent;
import com.example.propiedadesguitarra2.components.SimpleTextComponent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class PropiedadesCuerdaFragment extends Fragment {

    private Switch todasSwitch;
    private NumberComponent friccionPorCuerda = new NumberComponent(0f, 1f);
    private NumberComponent frecuenciaPorCuerda = new NumberComponent(0f, null);
    private NumberComponent anchoPuntas = new NumberComponent(0f, null);
    private NumberComponent maxFriccPuntas = new NumberComponent(0f, 1f);
    private Spinner cuerda;
    private StateManager stateManager;
    private SimpleTextComponent nodosText = new SimpleTextComponent(false, 0f, null);
    private SimpleTextComponent diapasonText = new SimpleTextComponent(true, null, 0f);
    private SimpleTextComponent trasteText = new SimpleTextComponent(true, null, 0f);
    private Spinner cuerdas;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_propiedades_cuerda, container, false);

        stateManager = StateManager.get(this.getContext());

        return root;
    }

    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Obtengo tamano de pantalla
        DisplayMetrics displayMetrics = new DisplayMetrics();
        this.getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        System.out.println("Resolucion en ancho " + width);
        // Si la pantalla es chica, elimino texts no necesarios
        if (width < 900) {
            ((TextView) getView().findViewById(R.id.textFactor122)).setText("F");
            ((TextView) getView().findViewById(R.id.textFactor123)).setText("F");
            ((TextView) getView().findViewById(R.id.textFactor124)).setText("F");
            ((TextView) getView().findViewById(R.id.textFactor126)).setText("F");
        }

        cuerda = (Spinner) getView().findViewById(R.id.cuerdaSpi);
        cuerda.setSelection(0, true);

        todasSwitch = (Switch) getView().findViewById(R.id.todasSwitch);

        // Configuro propiedades

        // Cuerdas
        cuerdas = (Spinner) getView().findViewById(R.id.cuerdaSpi);
        List<String> cuerdasTexto = new ArrayList<>();
        for (int i=1; i <= stateManager.state.cantCuerdas; ++i) {
            cuerdasTexto.add(String.valueOf(i));
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, cuerdasTexto);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        cuerdas.setAdapter(adapter);

        // Friccion
        this.friccionPorCuerda.setView(
                (EditText) getView().findViewById(R.id.friccNum),
                (EditText) getView().findViewById(R.id.friccFactor),
                (SeekBar) getView().findViewById(R.id.friccBar));

        friccionPorCuerda.onChange(v -> updateAndSend("friccion", v));

        // Frecuencia
        this.frecuenciaPorCuerda.setView(
                (EditText) getView().findViewById(R.id.frecuText),
                (EditText)  getView().findViewById(R.id.frecFactor),
                (SeekBar) getView().findViewById(R.id.frecBar));

        frecuenciaPorCuerda.onChange(v -> updateAndSend("frecuencia", v));

        this.anchoPuntas.setView(
                (EditText) getView().findViewById(R.id.anchoPuntaText),
                (EditText) getView().findViewById(R.id.anchoPuntaFactor),
                (SeekBar) getView().findViewById(R.id.anchoPuntaBar));

        anchoPuntas.onChange(v -> updateAndSend("anchoPuntas", v));

        // Max fricc puntas
        this.maxFriccPuntas.setView(
                (EditText) getView().findViewById(R.id.maxFriccPuntasText),
                (EditText) getView().findViewById(R.id.maxFriccPuntasFactor),
                (SeekBar) getView().findViewById(R.id.maxFriccPuntasBar));

        maxFriccPuntas.onChange(v -> updateAndSend("maxFriccionEnPunta", v));

        // Otros
        nodosText.setView((EditText) getView().findViewById(R.id.nodosText));
        nodosText.onChange(v -> updateAndSend("nodos", v));

        diapasonText.setView((EditText) getView().findViewById(R.id.diapasonText));
        diapasonText.onChange(v -> updateAndSend("distanciaCuerdaDiapason", v));

        trasteText.setView((EditText) getView().findViewById(R.id.trasteText));
        trasteText.onChange(v -> updateAndSend("distanciaCuerdaTraste", v));

        updateAll();

        cuerda.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                updateAll();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void updateAndSend(String variable, Float value) {
        getSelectedString().forEach(cuerda -> cuerda.put(variable, value));
        stateManager.runAlgorithms(variable);
        stateManager.sendValueByBluetooth(variable, value);
    }

    private void updateAll() {
        Integer selected = Integer.parseInt(cuerda.getSelectedItem().toString()) - 1;
        friccionPorCuerda.update(stateManager.state.cuerdas.get(selected).get("friccion"));
        frecuenciaPorCuerda.update(stateManager.state.cuerdas.get(selected).get("frecuencia"));
        nodosText.update(stateManager.state.cuerdas.get(selected).get("nodos"));
        anchoPuntas.update(stateManager.state.cuerdas.get(selected).get("anchoPuntas"));
        maxFriccPuntas.update(stateManager.state.cuerdas.get(selected).get("maxFriccionEnPunta"));
        diapasonText.update(stateManager.state.cuerdas.get(selected).get("distanciaCuerdaDiapason"));
        trasteText.update(stateManager.state.cuerdas.get(selected).get("distanciaCuerdaTraste"));
    }

    private Collection<Map<String, Float>> getSelectedString() {
        if (todasSwitch.isChecked()) {
            // Devulevo todas las cuerdas
            return stateManager.state.cuerdas.values();
        } else {
            return Collections.singleton(
                    stateManager.state.cuerdas.get(Integer.parseInt(cuerda.getSelectedItem().toString()) - 1)
            );
        }
    }
}