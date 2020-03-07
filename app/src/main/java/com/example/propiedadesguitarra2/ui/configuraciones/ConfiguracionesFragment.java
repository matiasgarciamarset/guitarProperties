package com.example.propiedadesguitarra2.ui.configuraciones;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Switch;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.propiedadesguitarra2.R;
import com.example.propiedadesguitarra2.StateManager;
import com.example.propiedadesguitarra2.components.SimpleTextComponent;
import com.example.propiedadesguitarra2.model.Pair;

public class ConfiguracionesFragment extends Fragment {

    private StateManager stateManager;

    private SimpleTextComponent nFramesText;
    private SimpleTextComponent sampleRateText;
    private SimpleTextComponent nbuferiiText;
    private SimpleTextComponent canalesEntradaText;
    private SimpleTextComponent npotText;
    private SimpleTextComponent dedoSizeText;
    private SimpleTextComponent softrealtimeRefreshText;
    private SimpleTextComponent btBufferSizeText;
    private Switch debugModeSwitch;
    private Switch imprimirSwitch;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_configuraciones, container, false);

        stateManager = StateManager.get(this.getContext());

        return root;
    }

    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        nFramesText = new SimpleTextComponent((EditText) getView().findViewById(R.id.nFramesText), false);
        sampleRateText = new SimpleTextComponent((EditText) getView().findViewById(R.id.sampleRateText), false);
        nbuferiiText = new SimpleTextComponent((EditText) getView().findViewById(R.id.nbuferiiText), false);
        canalesEntradaText = new SimpleTextComponent((EditText) getView().findViewById(R.id.canalesEntradaText), false);
        npotText = new SimpleTextComponent((EditText) getView().findViewById(R.id.npotText), false);
        dedoSizeText = new SimpleTextComponent((EditText) getView().findViewById(R.id.dedoSizeText), false);
        softrealtimeRefreshText = new SimpleTextComponent((EditText) getView().findViewById(R.id.softrealtimeRefreshText), false);
        debugModeSwitch = (Switch) getView().findViewById(R.id.debugModeSwitch);
        imprimirSwitch = (Switch) getView().findViewById(R.id.imprimirSwitch);
        btBufferSizeText =  new SimpleTextComponent((EditText) getView().findViewById(R.id.btBufferSizeText), false);

        // Guardo cambios en State
        // Como estas variables se aplican solo despues de reiniciar, no se envian al dispositivo hasta que se guarda y luego reinicia
        nFramesText.onChange(v -> stateManager.state.nFrames = v);
        sampleRateText.onChange(v -> stateManager.state.samplerate = v);
        nbuferiiText.onChange(v -> stateManager.state.nbufferii = v);
        canalesEntradaText.onChange(v -> stateManager.state.canalesEntrada = v);
        npotText.onChange(v -> stateManager.state.npot = v);
        dedoSizeText.onChange(v -> stateManager.state.dedoSize = v);
        softrealtimeRefreshText.onChange(v -> stateManager.state.softrealtimeRefresh = v);
        debugModeSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> stateManager.state.debugMode = isChecked);
        imprimirSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> stateManager.state.imprimir = isChecked);
        btBufferSizeText.onChange(v -> stateManager.state.btBufferSize = v);

        // Cargo cambios
        nFramesText.update(stateManager.state.nFrames);
        sampleRateText.update(stateManager.state.samplerate);
        nbuferiiText.update(stateManager.state.nbufferii);
        canalesEntradaText.update(stateManager.state.canalesEntrada);
        npotText.update(stateManager.state.npot);
        dedoSizeText.update(stateManager.state.dedoSize);
        softrealtimeRefreshText.update(stateManager.state.softrealtimeRefresh);
        debugModeSwitch.setChecked(stateManager.state.debugMode);
        imprimirSwitch.setChecked(stateManager.state.imprimir);
        btBufferSizeText.update(stateManager.state.btBufferSize);
    }
}