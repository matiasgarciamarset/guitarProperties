package com.example.propiedadesguitarra2.ui.propiedadescuerda;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.propiedadesguitarra2.NumberConverter;
import com.example.propiedadesguitarra2.R;
import com.example.propiedadesguitarra2.StateManager;
import com.example.propiedadesguitarra2.components.NumberComponent;
import com.example.propiedadesguitarra2.components.SimpleTextComponent;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

public class PropiedadesCuerdaFragment extends Fragment {

    private PropiedadesCuerdaViewModel propiedadesCuerdaViewModel;
    private Switch todasSwitch;
    private NumberComponent friccionPorCuerda;
    private NumberComponent frecuenciaPorCuerda;
    private Spinner cuerda;
    private StateManager stateManager;
    private SimpleTextComponent nodosText;
    private SimpleTextComponent anchoPuntaText;
    private SimpleTextComponent maxFriccPuntasText;
    private SimpleTextComponent diapasonText;
    private SimpleTextComponent trasteText;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        propiedadesCuerdaViewModel =
                ViewModelProviders.of(this).get(PropiedadesCuerdaViewModel.class);
        View root = inflater.inflate(R.layout.fragment_propiedades_cuerda, container, false);

        stateManager = StateManager.get(this.getContext());

        return root;
    }

    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        cuerda = (Spinner) getView().findViewById(R.id.cuerdaSpi);
        cuerda.setSelection(0, true);

        todasSwitch = (Switch) getView().findViewById(R.id.todasSwitch);

        // Configuro propiedades

        // Friccion
        this.friccionPorCuerda = new NumberComponent(
                (EditText) getView().findViewById(R.id.friccNum),
                (NumberPicker) getView().findViewById(R.id.friccDec),
                (SeekBar) getView().findViewById(R.id.friccBar),
                (TextView) getView().findViewById(R.id.friccView));

        friccionPorCuerda.onChange((coef, exp) ->
                getSelectedString().forEach(cuerda ->
                        cuerda.put("friccion", NumberConverter.serialize(coef, exp))));

        // Frecuencia
        this.frecuenciaPorCuerda = new NumberComponent(
                (EditText) getView().findViewById(R.id.frecuText),
                (NumberPicker) getView().findViewById(R.id.frecDec),
                (SeekBar) getView().findViewById(R.id.frecBar),
                (TextView) getView().findViewById(R.id.frecView));

        frecuenciaPorCuerda.onChange((coef, exp) ->
                getSelectedString().forEach(cuerda ->
                        cuerda.put("frecuencia", NumberConverter.serialize(coef, exp))));

        // Otros
        nodosText = new SimpleTextComponent((EditText) getView().findViewById(R.id.nodosText));
        nodosText.onChange((coef, exp) -> getSelectedString().forEach(cuerda -> cuerda.put("nodos", NumberConverter.serialize(coef, exp))));

        anchoPuntaText =  new SimpleTextComponent((EditText) getView().findViewById(R.id.anchoPuntaText));
        anchoPuntaText.onChange((coef, exp) -> getSelectedString().forEach(cuerda -> cuerda.put("anchoPuntas", NumberConverter.serialize(coef, exp))));

        maxFriccPuntasText =  new SimpleTextComponent((EditText) getView().findViewById(R.id.maxFriccPuntasText));
        maxFriccPuntasText.onChange((coef, exp) -> getSelectedString().forEach(cuerda -> cuerda.put("maxFriccionEnPunta", NumberConverter.serialize(coef, exp))));

        diapasonText =  new SimpleTextComponent((EditText) getView().findViewById(R.id.diapasonText));
        diapasonText.onChange((coef, exp) -> getSelectedString().forEach(cuerda -> cuerda.put("distanciaCuerdaDiapason", NumberConverter.serialize(coef, exp))));

        trasteText =  new SimpleTextComponent((EditText) getView().findViewById(R.id.trasteText));
        trasteText.onChange((coef, exp) -> getSelectedString().forEach(cuerda -> cuerda.put("distanciaCuerdaTraste", NumberConverter.serialize(coef, exp))));

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

    private void updateAll() {
        Integer selected = Integer.parseInt(cuerda.getSelectedItem().toString());
        friccionPorCuerda.update(stateManager.state.cuerdas.get(selected).get("friccion"));
        frecuenciaPorCuerda.update(stateManager.state.cuerdas.get(selected).get("frecuencia"));
        nodosText.update(stateManager.state.cuerdas.get(selected).get("nodos"));
        anchoPuntaText.update(stateManager.state.cuerdas.get(selected).get("anchoPuntas"));
        maxFriccPuntasText.update(stateManager.state.cuerdas.get(selected).get("maxFriccionEnPunta"));
        diapasonText.update(stateManager.state.cuerdas.get(selected).get("distanciaCuerdaDiapason"));
        trasteText.update(stateManager.state.cuerdas.get(selected).get("distanciaCuerdaTraste"));
    }

    private Collection<Map<String, String>> getSelectedString() {
        if (todasSwitch.isChecked()) {
            // Devulevo todas las cuerdas
            return stateManager.state.cuerdas.values();
        } else {
            return Collections.singleton(
                    stateManager.state.cuerdas.get(Integer.parseInt(cuerda.getSelectedItem().toString()))
            );
        }
    }
}