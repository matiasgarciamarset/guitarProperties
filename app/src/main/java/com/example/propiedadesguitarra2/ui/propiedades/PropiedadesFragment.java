package com.example.propiedadesguitarra2.ui.propiedades;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
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

import com.example.propiedadesguitarra2.R;
import com.example.propiedadesguitarra2.StateManager;
import com.example.propiedadesguitarra2.ui.propiedades.components.NumberComponent;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

public class PropiedadesFragment extends Fragment {

    private PropiedadesViewModel propiedadesViewModel;
    private Switch todasSwitch;
    private NumberComponent friccionPorCuerda;
    private NumberComponent frecuenciaPorCuerda;
    private Spinner cuerda;
    private StateManager stateManager;
    private SimpleStringGuitarField nodosText;
    private SimpleStringGuitarField anchoPuntaText;
    private SimpleStringGuitarField maxFriccPuntasText;
    private SimpleStringGuitarField diapasonText;
    private SimpleStringGuitarField trasteText;

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
                        cuerda.put("friccion", StateManager.parseToString(coef, exp))));

        // Frecuencia
        this.frecuenciaPorCuerda = new NumberComponent(
                (EditText) getView().findViewById(R.id.frecuText),
                (NumberPicker) getView().findViewById(R.id.frecDec),
                (SeekBar) getView().findViewById(R.id.frecBar),
                (TextView) getView().findViewById(R.id.frecView));

        frecuenciaPorCuerda.onChange((coef, exp) ->
                getSelectedString().forEach(cuerda ->
                        cuerda.put("frecuencia", StateManager.parseToString(coef, exp))));

        // Otros
        nodosText = new SimpleStringGuitarField((EditText) getView().findViewById(R.id.nodosText), "nodos");
        anchoPuntaText =  new SimpleStringGuitarField((EditText) getView().findViewById(R.id.anchoPuntaText), "anchoPuntas");
        maxFriccPuntasText =  new SimpleStringGuitarField((EditText) getView().findViewById(R.id.maxFriccPuntasText), "maxFriccionEnPunta");
        diapasonText =  new SimpleStringGuitarField((EditText) getView().findViewById(R.id.diapasonText), "distanciaCuerdaDiapason");
        trasteText =  new SimpleStringGuitarField((EditText) getView().findViewById(R.id.trasteText), "distanciaCuerdaTraste");


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
        friccionPorCuerda.update(
                stateManager.state.cuerdas.get(selected).get("friccion"));
        frecuenciaPorCuerda.update(
                stateManager.state.cuerdas.get(selected).get("frecuencia"));
        nodosText.update(selected);
        anchoPuntaText.update(selected);
        maxFriccPuntasText.update(selected);
        diapasonText.update(selected);
        trasteText.update(selected);
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


    private class SimpleStringGuitarField {
        private String item;
        private EditText field;

        SimpleStringGuitarField(EditText field, String item) {
            this.field = field;
            this.item = item;

            field.addTextChangedListener(onSimpleFieldChange(item));
        }

        public void update(Integer string) {
            field.setText(stateManager.prettyPrint(stateManager.state.cuerdas.get(string).get(item)));
        }

        private TextWatcher onSimpleFieldChange(String item) {
            return new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) { }

                @Override
                public void afterTextChanged(Editable s) {
                    if (s.length() == 0 || s.toString().equals("-")) {
                        getSelectedString().forEach(cuerda ->
                                cuerda.put(item, StateManager.parseToString(0d, 0)));
                        return;
                    }
                    String[] split = s.toString().split("\\.");
                    Double coef;
                    Integer exp;
                    if (split.length == 1) {
                        coef = Double.parseDouble(split[0]);
                        exp = 0;
                    } else {
                        coef = Double.parseDouble(split[0].concat(split[1]));
                        exp = split[1].length() * -1;
                    }
                    getSelectedString().forEach(cuerda ->
                            cuerda.put(item, StateManager.parseToString(coef, exp)));
                }
            };
        }

    }
}