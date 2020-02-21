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

        nFramesText = new SimpleTextComponent((EditText) getView().findViewById(R.id.nFramesText));
        sampleRateText = new SimpleTextComponent((EditText) getView().findViewById(R.id.sampleRateText));
        nbuferiiText = new SimpleTextComponent((EditText) getView().findViewById(R.id.nbuferiiText));
        canalesEntradaText = new SimpleTextComponent((EditText) getView().findViewById(R.id.canalesEntradaText));
        npotText = new SimpleTextComponent((EditText) getView().findViewById(R.id.npotText));
        dedoSizeText = new SimpleTextComponent((EditText) getView().findViewById(R.id.dedoSizeText));
        softrealtimeRefreshText = new SimpleTextComponent((EditText) getView().findViewById(R.id.softrealtimeRefreshText));
        debugModeSwitch = (Switch) getView().findViewById(R.id.debugModeSwitch);
        imprimirSwitch = (Switch) getView().findViewById(R.id.imprimirSwitch);

        // Guardo cambios en State
        // Como estas variables se aplican solo despues de reiniciar, no se envian al dispositivo hasta que se guarda y luego reinicia
        nFramesText.onChange((c, e) -> stateManager.state.nFrames = Pair.create(c, e));
        sampleRateText.onChange((c, e) -> stateManager.state.samplerate = Pair.create(c, e));
        nbuferiiText.onChange((c, e) -> stateManager.state.nbufferii = Pair.create(c, e));
        canalesEntradaText.onChange((c, e) -> stateManager.state.canalesEntrada = Pair.create(c, e));
        npotText.onChange((c, e) -> stateManager.state.npot = Pair.create(c, e));
        dedoSizeText.onChange((c, e) -> stateManager.state.dedoSize = Pair.create(c, e));
        softrealtimeRefreshText.onChange((c, e) -> stateManager.state.softrealtimeRefresh = Pair.create(c, e));
        debugModeSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> stateManager.state.debugMode = isChecked);
        imprimirSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> stateManager.state.imprimir = isChecked);

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
    }
}