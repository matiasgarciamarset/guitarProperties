package com.example.propiedadesguitarra2.converters;

import com.example.propiedadesguitarra2.model.Pair;

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

    public static String prettyPrint(Pair<Integer, Integer> value) {
        Double result = value.first * Math.pow(10, value.second);

        if (value.second < 0) {
            return truncate(String.format("%.10f",result), value.second);
        }
        return truncate(String.format("%.10f",result), value.second);
    }

    public static String desserialize(String value) {
        Pair<Integer, Integer> valueP = getCoefAndExp(value);
        return prettyPrint(valueP);
    }

    // Crea la codificacion usada para persistir
    public static String serialize(Pair<Integer, Integer> value) {
        return value.second == 0 ? value.first.toString() : value.first.toString() + SEPARATOR + value.second.toString();
    }

    // Crea la codificacion usada para persistir
    public static String serialize(Float value) {
        return value.toString();
    }

    private static String truncate(String number, Integer exp) {
        String[] parts = number.split("\\.");
        return (parts.length < 2 || exp >= 0) ? parts[0] : parts[0].concat("."+parts[1].substring(0, Math.min(exp*-1, parts[1].length())));
    }
}
