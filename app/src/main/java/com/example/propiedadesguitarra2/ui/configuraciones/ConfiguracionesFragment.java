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

    private SimpleTextComponent nFramesText = new SimpleTextComponent(false, 0f, null);
    private SimpleTextComponent sampleRateText = new SimpleTextComponent(false, 44100f, 192000f);
    private SimpleTextComponent nbuferiiText = new SimpleTextComponent(false, 32f, null);
    private SimpleTextComponent canalesEntradaText = new SimpleTextComponent(false, 0f, null);
    private SimpleTextComponent npotText = new SimpleTextComponent(false, 0f, null);
    private SimpleTextComponent dedoSizeText = new SimpleTextComponent(false, 0f, null);
    private SimpleTextComponent softrealtimeRefreshText = new SimpleTextComponent(false, 0f, null);
    private SimpleTextComponent btBufferSizeText =  new SimpleTextComponent(false, 0f, null);

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_configuraciones, container, false);

        stateManager = StateManager.get(this.getContext());

        return root;
    }

    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        nFramesText.setView((EditText) getView().findViewById(R.id.nFramesText));
        sampleRateText.setView((EditText) getView().findViewById(R.id.sampleRateText));
        nbuferiiText.setView((EditText) getView().findViewById(R.id.nbuferiiText));
        canalesEntradaText.setView((EditText) getView().findViewById(R.id.canalesEntradaText));
        npotText.setView((EditText) getView().findViewById(R.id.npotText));
        dedoSizeText.setView((EditText) getView().findViewById(R.id.dedoSizeText));
        softrealtimeRefreshText.setView((EditText) getView().findViewById(R.id.softrealtimeRefreshText));
        btBufferSizeText.setView((EditText) getView().findViewById(R.id.btBufferSizeText));

        // Guardo cambios en State
        // Como estas variables se aplican solo despues de reiniciar, no se envian al dispositivo hasta que se guarda y luego reinicia
        nFramesText.onChange(v -> stateManager.state.nFrames = v);
        sampleRateText.onChange(v -> stateManager.state.samplerate = v);
        nbuferiiText.onChange(v -> stateManager.state.nbufferii = v);
        canalesEntradaText.onChange(v -> stateManager.state.canalesEntrada = v);
        npotText.onChange(v -> stateManager.state.npot = v);
        dedoSizeText.onChange(v -> stateManager.state.dedoSize = v);
        softrealtimeRefreshText.onChange(v -> stateManager.state.softrealtimeRefresh = v);
        btBufferSizeText.onChange(v -> stateManager.state.btBufferSize = v);

        // Cargo cambios
        nFramesText.update(stateManager.state.nFrames);
        sampleRateText.update(stateManager.state.samplerate);
        nbuferiiText.update(stateManager.state.nbufferii);
        canalesEntradaText.update(stateManager.state.canalesEntrada);
        npotText.update(stateManager.state.npot);
        dedoSizeText.update(stateManager.state.dedoSize);
        softrealtimeRefreshText.update(stateManager.state.softrealtimeRefresh);
        btBufferSizeText.update(stateManager.state.btBufferSize);
    }
}