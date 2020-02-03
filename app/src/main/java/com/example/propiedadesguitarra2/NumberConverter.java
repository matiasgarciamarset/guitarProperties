package com.example.propiedadesguitarra2;

import android.util.Pair;

public class NumberConverter {
    private static final char SEPARATOR = '|';

    // Toma valor con formato "coef" o "coef|exp". Ejemplo "3" o "70|-2"
    public static Pair<Integer, Integer> getCoefAndExp(String serializedValue) {
        int sp_pos = serializedValue.indexOf(SEPARATOR);
        if (sp_pos == -1) {
            return Pair.create(Integer.parseInt(serializedValue), 0);
        }
        Integer coef = Integer.parseInt(serializedValue.substring(0, sp_pos));
        Integer exp = Integer.parseInt(serializedValue.substring(sp_pos+1));
        return Pair.create(coef, exp);
    }

    public static String prettyPrint(Integer coef, Integer exp) {
        Double result = coef * Math.pow(10, exp);

        return truncate(String.format("%.10f",result), exp);
    }

    public static String desserialize(String value) {
        Pair<Integer, Integer> valueP = getCoefAndExp(value);
        return prettyPrint(valueP.first, valueP.second);
    }

    // Crea la codificacion usada para persistir
    public static String serialize(Integer coef, Integer exp) {
        return exp == 0 ? coef.toString() : coef.toString() + SEPARATOR + exp.toString();
    }

    private static String truncate(String number, Integer exp) {
        String[] parts = number.split("\\.");
        return (parts.length < 2 || exp >= 0) ? parts[0] : parts[0].concat("."+parts[1].substring(0, Math.min(exp*-1, parts[1].length())));
    }
}
