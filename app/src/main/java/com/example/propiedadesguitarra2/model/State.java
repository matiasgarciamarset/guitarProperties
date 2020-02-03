package com.example.propiedadesguitarra2.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class State implements Serializable {

    // Configuraciones que requieren reinicio
    public String nFrames = "1|8"; // Cantidad de iteraciones del programa
    public String samplerate = "44100";
    public String nbufferii = "64";
    public String canalesEntrada = "2";
    public String npot = "2048"; // Tamaño tabla energia potencial
    public String dedoSize = "64";
    public String softrealtimeRefresh = "200";
    public Boolean debugMode = false;
    public Boolean imprimir = false;

    // Configuraciones real-time
    public String escalaIntensidad = "1";
    public String distanciaEquilibrioResorte = "20";
    public String distanciaEntreNodos = "5|4";
    public String centro = "25|-2"; // Donde se pone el dedo en la parte izq de la guitarra
    public String maxp = "1|-1"; // Metaparametros de la friccion en las puntas
    public String expp = "6|-1"; // Metaparametros de la friccion en las puntas - este es el "orden" en el codigo
    public String ordenMasa = "1|-1";

    public Map<Integer, Map<String, String>> cuerdas = new HashMap<>();

    public State() {
        // Configuracion por default
        for (int i=1; i<=6; i++) {
            cuerdas.put(i, new HashMap<>());
            cuerdas.get(i).put("friccion", "721|-5");
            cuerdas.get(i).put("frecuencia", "821069|-4");
            cuerdas.get(i).put("nodos", "400");
            cuerdas.get(i).put("maxFriccionEnPunta", "4|-1");
            cuerdas.get(i).put("anchoPuntas", "3|-1");
            cuerdas.get(i).put("distanciaCuerdaDiapason", "-180");
            cuerdas.get(i).put("distanciaCuerdaTraste", "-150");
        }
    }
}
