package com.example.propiedadesguitarra2.ui.propiedades;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.propiedadesguitarra2.R;
import com.example.propiedadesguitarra2.StateManager;
import com.example.propiedadesguitarra2.ui.propiedades.components.NumberComponent;

public class PropiedadesFragment extends Fragment {

    private PropiedadesViewModel propiedadesViewModel;
    private NumberComponent friccionPorCuerda;
    private Spinner cuerda;
    private StateManager stateManager;

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

        // Configuro propiedades

        // Friccion
        this.friccionPorCuerda = new NumberComponent(
                (EditText) getView().findViewById(R.id.friccNum),
                (NumberPicker) getView().findViewById(R.id.friccDec),
                (SeekBar) getView().findViewById(R.id.friccBar),
                (TextView) getView().findViewById(R.id.friccView),
                '|');

        friccionPorCuerda.onChange((coef, exp) ->
                stateManager.state.cuerdas.get(Integer.parseInt(cuerda.getSelectedItem().toString()))
                        .put("friccion", coef + "|" + exp));

        updateAll();

        cuerda.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                friccionPorCuerda.update(
                        stateManager.state.cuerdas.get(Integer.parseInt(parent.getItemAtPosition(position).toString())).get("friccion"));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void updateAll() {
        friccionPorCuerda.update(
                stateManager.state.cuerdas.get(Integer.parseInt(cuerda.getSelectedItem().toString())).get("friccion"));
    }

}