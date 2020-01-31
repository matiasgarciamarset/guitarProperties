package com.example.propiedadesguitarra2.ui.configuraciones;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.propiedadesguitarra2.R;
import com.example.propiedadesguitarra2.StateManager;
import com.example.propiedadesguitarra2.ui.propiedades.components.NumberComponent;

import java.util.function.Consumer;

public class ConfiguracionesFragment extends Fragment {

    private ConfiguracionesViewModel configuracionesViewModel;

    private StateManager stateManager;

    private EditText nFramesText;
    private EditText sampleRateText;
    private EditText nbuferiiText;
    private EditText canalesEntradaText;
    private EditText npotText;
    private EditText dedoSizeText;
    private EditText softrealtimeRefreshText;
    private Switch debugModeSwitch;
    private Switch imprimirSwitch;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        configuracionesViewModel =
                ViewModelProviders.of(this).get(ConfiguracionesViewModel.class);
        View root = inflater.inflate(R.layout.fragment_configuraciones, container, false);

        stateManager = StateManager.get(this.getContext());

        return root;
    }

    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        nFramesText = (EditText) getView().findViewById(R.id.nFramesText);
        sampleRateText = (EditText) getView().findViewById(R.id.sampleRateText);
        nbuferiiText = (EditText) getView().findViewById(R.id.nbuferiiText);
        canalesEntradaText = (EditText) getView().findViewById(R.id.canalesEntradaText);
        npotText = (EditText) getView().findViewById(R.id.npotText);
        dedoSizeText = (EditText) getView().findViewById(R.id.dedoSizeText);
        softrealtimeRefreshText = (EditText) getView().findViewById(R.id.softrealtimeRefreshText);
        debugModeSwitch = (Switch) getView().findViewById(R.id.debugModeSwitch);
        imprimirSwitch = (Switch) getView().findViewById(R.id.imprimirSwitch);

        // Cargo cambios
        nFramesText.setText(stateManager.state.nFrames.toString());
        sampleRateText.setText(stateManager.state.samplerate.toString());
        nbuferiiText.setText(stateManager.state.nbufferii.toString());
        canalesEntradaText.setText(stateManager.state.canalesEntrada.toString());
        npotText.setText(stateManager.state.npot.toString());
        dedoSizeText.setText(stateManager.state.dedoSize.toString());
        softrealtimeRefreshText.setText(stateManager.state.softrealtimeRefresh.toString());
        debugModeSwitch.setChecked(stateManager.state.debugMode);
        imprimirSwitch.setChecked(stateManager.state.imprimir);

        // Guardo cambios en State
        // Como estas variables se aplican solo despues de reiniciar, no se envian al dispositivo hasta que se guarda y luego reinicia
        nFramesText.addTextChangedListener(changeText(v -> stateManager.state.nFrames = Long.parseLong(v)));
        sampleRateText.addTextChangedListener(changeText(v -> stateManager.state.samplerate = Long.parseLong(v)));
        nbuferiiText.addTextChangedListener(changeText(v -> stateManager.state.nbufferii = Integer.parseInt(v)));
        canalesEntradaText.addTextChangedListener(changeText(v -> stateManager.state.canalesEntrada = Integer.parseInt(v)));
        npotText.addTextChangedListener(changeText(v -> stateManager.state.npot = Integer.parseInt(v)));
        dedoSizeText.addTextChangedListener(changeText(v -> stateManager.state.dedoSize =  Integer.parseInt(v)));
        softrealtimeRefreshText.addTextChangedListener(changeText(v -> stateManager.state.softrealtimeRefresh = Integer.parseInt(v)));
        debugModeSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> stateManager.state.debugMode = isChecked);
        imprimirSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> stateManager.state.imprimir = isChecked);
    }

    private TextWatcher changeText(Consumer<String> method) {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) {
                method.accept(s.toString());
            }
        };
    }
}