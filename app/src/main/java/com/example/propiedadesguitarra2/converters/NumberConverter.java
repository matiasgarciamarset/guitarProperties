package com.example.propiedadesguitarra2.converters;

public class NumberConverter {

    // Crea la codificacion usada para persistir
    public static StringBuffer serialize(Float value) {
        return new StringBuffer(value.toString());
    }

}
