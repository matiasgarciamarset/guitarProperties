package com.example.propiedadesguitarra2.converters;

import com.example.propiedadesguitarra2.model.State;

import java.util.Arrays;
import java.util.TreeMap;
import java.util.stream.Collectors;

// Los datos de las cuerdas son ignorados ya que la app usa los datos generados con eso
public class NumberCompressor {
    private static TreeMap<String, String> numberAssignator = new TreeMap<>();

    private static char START_CHARACTER = '&';
    private static char END_CHARACTER = ';';

    static {
        numberAssignator.put("nFrames", "0");
        numberAssignator.put("samplerate", "1");
        numberAssignator.put("nbufferii", "2");
        numberAssignator.put("canalesEntrada", "3");
        numberAssignator.put("npot", "4");
        numberAssignator.put("dedoSize", "5");
        numberAssignator.put("softrealtimeRefresh", "6");
        numberAssignator.put("debugMode", "7");
        numberAssignator.put("imprimir", "8");
        numberAssignator.put("escalaIntensidad", "9");
        numberAssignator.put("distanciaEquilibrioResorte", "10");
        numberAssignator.put("distanciaEntreNodos", "11");
        numberAssignator.put("centro", "12");
        numberAssignator.put("maxp", "13");
        numberAssignator.put("expp", "14");
        numberAssignator.put("ordenMasa", "15");
        numberAssignator.put("masaPorNodo", "16");
        numberAssignator.put("friccionSinDedo", "17");
        numberAssignator.put("friccionConDedo", "18");
        numberAssignator.put("minimosYtrastes", "19");
    }

    public static String compressAll(State state) {
        return
                generateValue("nFrames", state.nFrames) + "," +
                generateValue("samplerate", state.samplerate) + "," +
                generateValue("nbufferii", state.nbufferii) + "," +
                generateValue("canalesEntrada", state.canalesEntrada) + "," +
                generateValue("npot", state.npot) + "," +
                generateValue("dedoSize", state.dedoSize) + "," +
                generateValue("softrealtimeRefresh", state.softrealtimeRefresh) + "," +
                generateValue("debugMode", state.debugMode) + "," +
                generateValue("imprimir", state.imprimir) + "," +
                generateValue("escalaIntensidad", state.escalaIntensidad) + "," +
                generateValue("distanciaEquilibrioResorte", state.distanciaEquilibrioResorte) + "," +
                generateValue("distanciaEntreNodos", state.distanciaEntreNodos) + "," +
                generateValue("centro", state.centro) + "," +
                generateValue("maxp", state.maxp) + "," +
                generateValue("expp", state.expp) + "," +
                generateValue("ordenMasa", state.ordenMasa) + "," +
                generateValue("masaPorNodo", state.masaPorNodo) + "," +
                generateValue("friccionSinDedo", state.friccionSinDedo) + "," +
                generateValue("friccionConDedo", state.friccionConDedo) + "," +
                generateValue("minimosYtrastes", state.minimosYtrastes);
    }

    public static String generateValue(String variableName, Float[] list) {
        if (!numberAssignator.containsKey(variableName))
            return null;
        if (list == null || list.length == 0) return null;
        return START_CHARACTER  +
                numberAssignator.get(variableName) + ":[" +
                Arrays.stream(list)
                    .map(NumberConverter::serialize)
                    .collect(Collectors.joining(","))
                + "]" + END_CHARACTER;
    }

    public static String generateValue(String variableName, Float value) {
        if (!numberAssignator.containsKey(variableName))
            return null;
        return START_CHARACTER + numberAssignator.get(variableName) + ":" + NumberConverter.serialize(value) + END_CHARACTER;
    }

    public static String generateValue(String variableName, Boolean value) {
        if (!numberAssignator.containsKey(variableName))
            return null;
        return START_CHARACTER + numberAssignator.get(variableName) + ":" + (value ? "1" : "0") + END_CHARACTER;
    }
}
