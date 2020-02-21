package com.example.propiedadesguitarra2.model;

import java.io.Serializable;

public class Pair<T, U> implements Serializable {
    private static final long serialVersionUID = 6529685098267757690L;

    public T first;
    public U second;

    private Pair(T one, U second) {
        first = one;
        this.second = second;
    }

    public static Pair<Integer, Integer> create(Integer one, Integer second) {
        return new Pair<Integer, Integer>(one, second);
    }
}