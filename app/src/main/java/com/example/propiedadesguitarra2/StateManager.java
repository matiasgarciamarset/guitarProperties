package com.example.propiedadesguitarra2;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.os.Handler;

import com.example.propiedadesguitarra2.converters.StateFormater;
import com.example.propiedadesguitarra2.model.State;
import com.example.propiedadesguitarra2.storage.LocalStorageManager;
import com.example.propiedadesguitarra2.ui.cargarguardar.BluetoothService;


public class StateManager {

    public State state = new State();

    private volatile static StateManager stateManager = null;

    private LocalStorageManager localStorageManager;
    private BluetoothService bService;
    private MetaAlgorithms metaAlgorithms;

    private Handler bt_handler;

    public static synchronized StateManager get(Context context) {
        if (stateManager == null)
            stateManager = new StateManager(context);
        return stateManager;
    }

    private StateManager(Context context) {
        localStorageManager = new LocalStorageManager();
        if (localStorageManager.storedStates(context).length == 0) {
            localStorageManager.save("default", context, new State());
        } else {
            state = localStorageManager.read(localStorageManager.storedStates(context)[0], context);
        }
        bService = new BluetoothService(context);
        metaAlgorithms = new MetaAlgorithms();
    }

    public void save(String fileName, Context context) {
        localStorageManager.save(fileName, context, state);
    }

    public boolean read(String fileName, Context context) {
        State newState = localStorageManager.read(fileName, context);
        if (newState != null) {
            state = newState;
            return sendAllByBluetooth();
        }
        return false;
    }

    public boolean delete(String fileName, Context context) {
        State newState = localStorageManager.delete(fileName, context);
        if (newState != null) {
            state = newState;
            return sendAllByBluetooth();
        }
        return false;
    }

    public String currentFile() {
        return localStorageManager.currentFile();
    }

    public String[] fileList(Context context) {
        return localStorageManager.storedStates(context);
    }

    public Boolean sendAllByBluetooth() {
        // Chequeo que el servicio este conectado
        if (bService.getState() != 3) {
            return false;
        }

        String allVariables = StateFormater.compressAll(state);
        if (allVariables.length() > 0) {
            byte[] send = allVariables.getBytes();
            bService.write(send);
        }
        return true;
    }

    public Boolean runAlgorithms(String variableName) {
        return metaAlgorithms.generate(variableName, state);
    }

    public Boolean sendValueByBluetooth(String variableName, Float value) {
        // Chequeo que el servicio este conectado
        if (bService.getState() != 3) {
            return false;
        }

        // Envio variables generadas si corresponde
        if (state.masaPorNodoEdited) {
            String send = StateFormater.generateValue("masaPorNodo", state.masaPorNodo);
            if (send != null) bService.write(send.getBytes());
            state.masaPorNodoEdited = false;
        }

        if (state.friccionSinDedoEdited) {
            String send = StateFormater.generateValue("friccionSinDedo", state.friccionSinDedo);
            if (send != null) bService.write(send.getBytes());
            state.friccionSinDedoEdited = false;
        }

        if (state.friccionConDedoEdited) {
            String send = StateFormater.generateValue("friccionConDedo", state.friccionConDedo);
            if (send != null) bService.write(send.getBytes());
            state.friccionConDedoEdited = false;
        }

        if (state.minimosYtrastesEdited) {
            String send = StateFormater.generateValue("minimosYtrastes", state.minimosYtrastes);
            if (send != null) bService.write(send.getBytes());
            state.minimosYtrastesEdited = false;
        }

        // Envio variable
        String content = StateFormater.generateValue(variableName, value);
        System.out.println(content);
        if (content != null) bService.write(content.getBytes());

        return true;
    }

    public void connectBluetooth(BluetoothDevice device) {
        bService.connect(device, state.btBufferSize.intValue());
        // Sincronizo toda la informacion al establecer conexion
        sendAllByBluetooth();
    }

    public void setBtHandler(Handler handler) {
        this.bt_handler = handler;
        bService.onConnectionStatusChange(bt_handler);
    }
}
