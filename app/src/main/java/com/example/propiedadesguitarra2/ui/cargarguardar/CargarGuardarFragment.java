package com.example.propiedadesguitarra2.ui.cargarguardar;

import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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

    private BluetoothAdapter mBluetoothAdapter;
    private Button bluetoothButton;
    private TextView bluetoothView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        cargarGuardarViewModel =
                ViewModelProviders.of(this).get(CargarGuardarViewModel.class);
        View root = inflater.inflate(R.layout.fragment_cargar_guardar, container, false);

        stateManager = StateManager.get(this.getContext());
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        return root;
    }

    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        bluetoothButton = (Button) getView().findViewById(R.id.bluetoothButton);
        bluetoothView = (TextView) getView().findViewById(R.id.bluetoothView);

        bluetoothButton.setOnClickListener(v -> {
            if (mBluetoothAdapter == null) {
                bluetoothView.setText(R.string.bluetooth_no_available);
                return;
            }
            // If BT is not on, request that it be enabled.
            // setupChat() will then be called during onActivityResult
            if (!mBluetoothAdapter.isEnabled()) {
                Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableIntent, 3);
            } else {
                Intent serverIntent = new Intent(getActivity(), DeviceListActivity.class);
                startActivityForResult(serverIntent, 1);
            }
        });

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
                stateManager.sendByBluetooth(parent.getItemAtPosition(position).toString()); // TODO only for testing
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

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 1:
                if (resultCode == Activity.RESULT_OK) {
                    connectDevice(data);
                }
                break;
        }
    }

    private void updateGuardadosSpinner(Context context) {
        ArrayAdapter<String> arrayAdapter1 = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, stateManager.fileList(context));
        arrayAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        guardados.setAdapter(arrayAdapter1);
        guardados.setSelection(arrayAdapter1.getPosition(stateManager.currentFile(context)));
    }

    private void connectDevice(Intent data) {
        // Get the device MAC address
        Bundle extras = data.getExtras();
        if (extras == null) {
            return;
        }
        String address = extras.getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
        // Get the BluetoothDevice object
        BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
        // Attempt to connect to the device
        stateManager.connectBluetooth(device, (status, dev) -> getActivity().runOnUiThread(
                () -> bluetoothView.setText(status == 2 ? "Conectando ..." :
                        ((status == 3 ? "Conectado a dispositivo: " :
                                "Hubo un error al conectar - Error: ") + dev)))
        );
    }
}