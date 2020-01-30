package com.example.propiedadesguitarra2.ui.cargarguardar;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.propiedadesguitarra2.R;
import com.example.propiedadesguitarra2.StateManager;

public class CargarGuardarFragment extends Fragment {

    private CargarGuardarViewModel cargarGuardarViewModel;
    private StateManager stateManager;
    private Spinner guardados;
    private EditText guardarText;
    private Button guardar;
    private Button eliminar;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        cargarGuardarViewModel =
                ViewModelProviders.of(this).get(CargarGuardarViewModel.class);
        View root = inflater.inflate(R.layout.fragment_cargar_guardar, container, false);

        stateManager = StateManager.get(this.getContext());

        return root;
    }

    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Cargo archivos guardados
        guardados = (Spinner) getView().findViewById(R.id.guardadosSpin);
        guardarText = (EditText) getView().findViewById(R.id.guardarText);
        guardar = (Button) getView().findViewById(R.id.guardar);
        eliminar = (Button) getView().findViewById(R.id.eliminar);
        updateGuardadosSpinner(this.getContext());
        guardarText.setText(stateManager.currentFile(this.getContext()));

        guardados.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                stateManager.read(parent.getItemAtPosition(position).toString(), view.getContext());
                guardarText.setText(parent.getItemAtPosition(position).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        guardar.setOnClickListener(v -> {
            stateManager.save(guardarText.getText().toString(), v.getContext());
            updateGuardadosSpinner(v.getContext());
        });

        eliminar.setOnClickListener(v -> {
            if (stateManager.delete(guardarText.getText().toString(), v.getContext())) {
                DialogInterface.OnClickListener dialogClickListener = (dialog, which) -> {
                    switch (which){
                        case DialogInterface.BUTTON_POSITIVE:
                            updateGuardadosSpinner(v.getContext());
                            guardarText.setText(stateManager.currentFile(v.getContext()));
                            break;
                        case DialogInterface.BUTTON_NEGATIVE:
                            break;
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setMessage("Seguro desea eliminar?").setPositiveButton("Si", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();
            }
        });
    }

    private void updateGuardadosSpinner(Context context) {
        ArrayAdapter<String> arrayAdapter1 = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, stateManager.fileList(context));
        arrayAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        guardados.setAdapter(arrayAdapter1);
        guardados.setSelection(arrayAdapter1.getPosition(stateManager.currentFile(context)));
    }
}