package com.example.propiedadesguitarra2;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.util.Pair;
import android.widget.TextView;

import com.example.propiedadesguitarra2.model.State;
import com.example.propiedadesguitarra2.ui.cargarguardar.BluetoothService;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.function.BiConsumer;


public class StateManager {

    public State state = new State();

    private String currentFile;

    private static StateManager stateManager = null;

    private StringBuffer mOutStringBuffer;
    private BluetoothService bService;

    public static synchronized StateManager get(Context context) {
        if (stateManager == null)
            stateManager = new StateManager(context);
        return stateManager;
    }

    private StateManager(Context context) {
        if (fileList(context).length == 0) {
            save("default", context);
        } else {
            read(fileList(context)[0], context);
        }
        bService = new BluetoothService(context);
    }

    public void save(String fileName, Context context) {
        try {
            FileOutputStream fos = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            ObjectOutputStream os = new ObjectOutputStream(fos);
            os.writeObject(state);
            os.close();
            fos.close();
            currentFile = fileName;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void read(String fileName, Context context) {
        if (fileName.equals(currentFile)) return;
        try {
            FileInputStream fis = context.openFileInput(fileName);
            ObjectInputStream is = new ObjectInputStream(fis);
            state = (State) is.readObject();
            is.close();
            fis.close();
            currentFile = fileName;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String[] fileList(Context context) {
        return context.fileList();
    }

    public String currentFile(Context context) {
        return currentFile;
    }

    public boolean delete(String fileName, Context context) {
        String[] files = fileList(context);
        if (files.length <= 1) {
            return false;
        }
        String other = null;
        for (String file : files) {
            if (!file.equals(fileName)) {
                other = file;
                break;
            }
        }
        read(other, context);
        return context.deleteFile(fileName);
    }

    public Boolean sendByBluetooth(String message) {
        // Chequeo que el servicio este conectado
        if (bService.getState() != 3) {
            return false;
        }

        if (message.length() > 0) {
            byte[] send = message.getBytes();
            bService.write(send);
        }
        return true;
    }

    public void connectBluetooth(BluetoothDevice device, BiConsumer<Integer, String> method) {
        bService.connect(device);
        bService.onConnectionStatusChange(method);
    }
}
