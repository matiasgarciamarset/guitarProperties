package com.example.propiedadesguitarra2;

import android.content.Context;
import android.util.Pair;

import com.example.propiedadesguitarra2.model.State;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;


public class StateManager {

    public State state = new State();

    private String currentFile;

    private static final char SEPARATOR = '|';

    private static StateManager stateManager = null;

    public static StateManager get(Context context) {
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

    // Toma valor con formato "coef" o "coef|exp". Ejemplo "3" o "70|-2"
    public static Pair<Double, Integer> convertToNumbers(String formatedValue) {
        int sp_pos = formatedValue.indexOf(SEPARATOR);
        if (sp_pos == -1) {
            return Pair.create(Double.parseDouble(formatedValue), 0);
        }
        Double coef = Double.parseDouble(formatedValue.substring(0, sp_pos));
        Integer exp = Integer.parseInt(formatedValue.substring(sp_pos+1));
        return Pair.create(coef, exp);
    }

    public static String prettyPrint(String value) {
        Pair<Double, Integer> valueP = convertToNumbers(value);
        return prettyPrint(valueP.first, valueP.second);
    }

    public static String prettyPrint(Double coef, Integer exp) {
        Double result = coef * Math.pow(10, exp);

        return truncate(String.format("%.10f",result), exp);
    }

    // Crea la codificacion usada para persistir
    public static String parseToString(Double coef, Integer exp) {
        return exp == 0 ? truncate(coef.toString(), 0) : truncate(coef.toString(), 0) + SEPARATOR + exp.toString();
    }

    private static String truncate(String number, Integer exp) {
        String[] parts = number.split("\\.");
        return (parts.length < 2 || exp >= 0) ? parts[0] : parts[0].concat("."+parts[1].substring(0, Math.min(exp*-1, parts[1].length())));
    }
}
