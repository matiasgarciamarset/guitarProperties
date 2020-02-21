package com.example.propiedadesguitarra2.storage;

import android.content.Context;

import com.example.propiedadesguitarra2.model.State;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class LocalStorageManager {

    private String currentFile;

    public void save(String fileName, Context context, State state) {
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

    public State read(String fileName, Context context) {
        if (currentFile != null && currentFile.equals(fileName)) {
            // No se va a leer un archivo que ya se esta leyendo
            return null;
        }
        FileInputStream fis = null;
        ObjectInputStream is = null;
        try {
            fis = context.openFileInput(fileName);
            is = new ObjectInputStream(fis);
            State estado = (State) is.readObject();
            currentFile = fileName;
            return estado;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (is != null) is.close();
                if (fis != null) fis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String[] storedStates(Context context) {
        return context.fileList();
    }

    public String currentFile() {
        return currentFile;
    }

    public synchronized State delete(String fileName, Context context) {
        String[] files = storedStates(context);
        // Elimino archivo
        if (!context.deleteFile(fileName))
            return null;
        String other = "default";
        // Retorno stado a cargar
        if (files.length <= 1) {
            // Cargo configuracion por defecto
            save(other, context, new State());
        } else {
            // Busco alguno ya guardado
            for (String file : files) {
                if (!file.equals(fileName)) {
                    other = file;
                    break;
                }
            }
        }
        return read(other, context);
    }
}
