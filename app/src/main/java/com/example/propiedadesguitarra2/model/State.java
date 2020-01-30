package com.example.propiedadesguitarra2.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class State implements Serializable {

    public Map<Integer, Map<String, String>> cuerdas = new HashMap<>();

    // Configuraciones que requieren reinicio
    public Long nFrames = 100000000L; // Cantidad de iteraciones del programa
    public Long samplerate = 44100L;
    public Integer nbufferii = 64;
    public Integer canalesEntrada = 2;
    public Integer npot = 2048; // Tamano tabla energia potencial
    public Boolean debugMode = false;
    public Integer dedoSize = 64;
    public Integer softrealtimeRefresh = 200;

    public State() {
        // Configuracion por default
        for (int i=1; i<=6; i++) {
            cuerdas.put(i, new HashMap<>());
            cuerdas.get(i).put("friccion", "721|-5");
        }
    }
}
