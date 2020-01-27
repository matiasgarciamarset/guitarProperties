package com.example.propiedadesguitarra.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class State implements Serializable {

    public Map<Integer, Map<String, String>> cuerdas = new HashMap<>();

    public State() {
        // Solo por ahora
        for (int i=1; i<=6; i++) {
            cuerdas.put(i, new HashMap<>());
            cuerdas.get(i).put("friccion", "721|-5");
        }
    }
}
