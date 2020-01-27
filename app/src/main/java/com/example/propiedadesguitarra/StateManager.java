package com.example.propiedadesguitarra;

import android.app.Application;
import android.content.Context;

import com.example.propiedadesguitarra.model.State;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Arrays;


public class StateManager {

    public State state = new State();

    private String currentFile;

    public StateManager(Context context) {
        if (fileList(context).length == 0) {
            save("default", context);
        } else {
            read(fileList(context)[0], context);
        }
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
        String other = Arrays.stream(files)
                .filter(name -> !name.equals(fileName))
                .findAny()
                .get();
        read(other, context);
        return context.deleteFile(fileName);
    }
}
