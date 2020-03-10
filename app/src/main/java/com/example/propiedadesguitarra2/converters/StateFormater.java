package com.example.propiedadesguitarra2.converters;

import android.text.Html;
import android.text.Spanned;

import com.example.propiedadesguitarra2.model.State;

import java.util.Arrays;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.stream.Collectors;

// Los datos de las cuerdas son ignorados ya que la app usa los datos generados con eso
public class StateFormater {
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
        numberAssignator.put("cantCuerdas", "20");
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
                generateValue("minimosYtrastes", state.minimosYtrastes)+ "," +
                generateValue("cantCuerdas", state.cantCuerdas);
    }

    public static Spanned prettyPrint(State state) {
        return Html.fromHtml("<small><ul>"+
                printValue("cantCuerdas", state.cantCuerdas) + "\n" +
                printValue("nFrames", state.nFrames) + "\n" +
                printValue("samplerate", state.samplerate) + "\n" +
                printValue("nbufferii", state.nbufferii) + "\n" +
                printValue("canalesEntrada", state.canalesEntrada) + "\n" +
                printValue("npot", state.npot) + "\n" +
                printValue("dedoSize", state.dedoSize) + "\n" +
                printValue("softrealtimeRefresh", state.softrealtimeRefresh) + "\n" +
                printValue("debugMode", state.debugMode) + "\n" +
                printValue("imprimir", state.imprimir) + "\n" +
                printValue("escalaIntensidad", state.escalaIntensidad) + "\n" +
                printValue("distanciaEquilibrioResorte", state.distanciaEquilibrioResorte) + "\n" +
                printValue("distanciaEntreNodos", state.distanciaEntreNodos) + "\n" +
                printValue("centro", state.centro) + "\n" +
                printValue("maxp", state.maxp) + "\n" +
                printValue("expp", state.expp) + "\n" +
                printValue("ordenMasa", state.ordenMasa) + "\n" +
                printCuerdas(state.cuerdas) + "\n" +
                printValue("masaPorNodo", state.masaPorNodo) + "\n" +
                printValue("friccionSinDedo", state.friccionSinDedo) + "\n" +
                printValue("friccionConDedo", state.friccionConDedo) + "\n" +
                printValue("minimosYtrastes", state.minimosYtrastes)
                + "</lu></small>");
    }

    private static String printCuerdas(Map<Integer, Map<String, Float>> cuerdas) {
        Function<Map<String, Float>, String> printCuerda = c -> {
            StringBuffer result = new StringBuffer("{ <br/><br/>");
            c.forEach((k, v) -> result.append("&nbsp;&nbsp;<b>"+ k+ "</b>")
                    .append(": ")
                    .append(v)
                    .append("<br/><br/>"));
            return result.append("}").toString();
        };
        return "<li><div><b>cuerdas</b>: [" +
                cuerdas.values().stream()
                        .map(printCuerda)
                        .collect(Collectors.joining(",<br/><br/>"))
                + "] </div></li>";
    }

    private static String printValue(String variableName, Float[] list) {
        if (list == null || list.length == 0) return null;
        return "<li>&nbsp;<b>" +variableName + "</b> <br/><br/> &nbsp;[" +
                Arrays.stream(list)
                        .map(NumberConverter::serialize)
                        .collect(Collectors.joining(", "))
                + "]</li>";
    }

    private static String printValue(String variableName, Float value) {
        return "<li>&nbsp;<b>"+variableName + "</b>: " + NumberConverter.serialize(value)+ "</li>";
    }

    private static String printValue(String variableName, Boolean value) {
        return "<li> <b>"+variableName + "</b>: " + (value ? "true" : "false")+ "</li>";
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
