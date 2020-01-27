package com.example.propiedadesguitarra;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.propiedadesguitarra.components.NumberComponent;

public class MainActivity extends AppCompatActivity {

    private StateManager stateManager;
    private NumberComponent friccionPorCuerda;
    private Spinner cuerda;

    private Spinner guardados;
    private EditText guardarText;
    private Button guardar;
    private Button eliminar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        stateManager = new StateManager(this);

        cuerda = (Spinner) findViewById(R.id.cuerdaSpi);
        cuerda.setSelection(0, true);

        // Creo botones para friccion
        this.friccionPorCuerda = new NumberComponent(
                (EditText) findViewById(R.id.friccNum),
                (NumberPicker) findViewById(R.id.friccDec),
                (SeekBar) findViewById(R.id.friccBar),
                (TextView) findViewById(R.id.friccView),
                '|');

        // Cargo archivos guardados
        guardados = (Spinner) findViewById(R.id.guardadosSpin);
        guardarText = (EditText) findViewById(R.id.guardarText);
        guardar = (Button) findViewById(R.id.guardar);
        eliminar = (Button) findViewById(R.id.eliminar);
        updateGuardadosSpinner(this);
        guardarText.setText(stateManager.currentFile(this));

        guardados.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                stateManager.read(parent.getItemAtPosition(position).toString(), view.getContext());
                updateAll();
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
                            updateAll();
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


        // COnfiguro variables

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

    private void updateGuardadosSpinner(Context context) {
        ArrayAdapter<String> arrayAdapter1 = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, stateManager.fileList(context));
        arrayAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        guardados.setAdapter(arrayAdapter1);
        guardados.setSelection(arrayAdapter1.getPosition(stateManager.currentFile(context)));
    }

    private void updateAll() {
        friccionPorCuerda.update(
                stateManager.state.cuerdas.get(Integer.parseInt(cuerda.getSelectedItem().toString())).get("friccion"));
    }

}
