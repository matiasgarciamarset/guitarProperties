package com.example.propiedadesguitarra2.ui.cargarguardar;

import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Spanned;
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
import androidx.fragment.app.FragmentActivity;

import com.example.propiedadesguitarra2.R;
import com.example.propiedadesguitarra2.StateManager;
import com.example.propiedadesguitarra2.converters.StateFormater;


public class CargarGuardarFragment extends Fragment {

    private StateManager stateManager;
    private Spinner guardados;
    private EditText guardarText;
    private Button guardar;
    private Button eliminar;
    private Button mostrar;

    private BluetoothAdapter mBluetoothAdapter;
    private Button bluetoothButton;
    private TextView bluetoothView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

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

        mostrar = (Button) getView().findViewById(R.id.mostrarButton);

        mostrar.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            Spanned mostrar = StateFormater.prettyPrint(stateManager.state);
            builder.setMessage(mostrar)
                    .setPositiveButton("Copiar", (dialog, id) -> {
                        ClipboardManager clipboard = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                        ClipData clip = ClipData.newPlainText("", mostrar.toString());
                        clipboard.setPrimaryClip(clip);
                    })
                    .setNegativeButton("Cancelar", (dialog, id) -> {
                    }).show();
        });

        // Cargo archivos guardados
        guardados = (Spinner) getView().findViewById(R.id.guardadosSpin);
        guardarText = (EditText) getView().findViewById(R.id.guardarText);
        guardar = (Button) getView().findViewById(R.id.guardar);
        eliminar = (Button) getView().findViewById(R.id.eliminar);
        updateGuardadosSpinner(getActivity().getBaseContext());
        guardarText.setText(stateManager.currentFile());

        guardados.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                stateManager.read(parent.getItemAtPosition(position).toString(), getActivity().getBaseContext());
                guardarText.setText(parent.getItemAtPosition(position).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        guardar.setOnClickListener(v -> {
            stateManager.save(guardarText.getText().toString(), getActivity().getBaseContext());
            updateGuardadosSpinner(getActivity().getBaseContext());
        });

        eliminar.setOnClickListener(v -> {
            DialogInterface.OnClickListener dialogClickListener = (dialog, which) -> {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        if (stateManager.delete(guardarText.getText().toString(), getActivity().getBaseContext())) {
                            updateGuardadosSpinner(getActivity().getBaseContext());
                            guardarText.setText(stateManager.currentFile());
                        }
                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        break;
                }
            };

            String message = stateManager.fileList(getActivity().getBaseContext()).length > 1 ? getString(R.string.delete_confirm) :
                    getString(R.string.delete_unique_confirm);
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setMessage(message).setPositiveButton(R.string.yes, dialogClickListener)
                    .setNegativeButton(R.string.no, dialogClickListener).show();
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
        guardados.setSelection(arrayAdapter1.getPosition(stateManager.currentFile()));
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
        stateManager.connectBluetooth(device, handler);
    }

    private final Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
        FragmentActivity activity = getActivity();
         if (msg.what == BluetoothService.CONNECTION_STATUS) {
             switch (msg.arg1) {
                 case 3:
                     bluetoothView.setText(getString(R.string.bt_connected) + msg.getData().getString("device"));
                     break;
                 case 2:
                     bluetoothView.setText(R.string.bt_connecting);
                     break;
                 case 0:
                     bluetoothView.setText(getString(R.string.bt_error) + msg.getData().getString("error"));
                     break;
             }
         }
        }
    };
}