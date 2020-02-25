package com.example.propiedadesguitarra2.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/*
 * La representacion es en Float
 */
public class State implements Serializable {

    private static final long serialVersionUID = 931824761779387905L;

    // Configuraciones que requieren reinicio
    public Float nFrames = 100000000f; // Cantidad de iteraciones del programa
    public Float samplerate = 4410f;
    public Float nbufferii = 64f;
    public Float canalesEntrada = 2f;
    public Float npot = 2048f; // Tamaño tabla energia potencial
    public Float dedoSize = 64f;
    public Float softrealtimeRefresh = 200f;
    public Boolean debugMode = false;
    public Boolean imprimir = true;
    public Float btBufferSize = 2048f;

    // Configuraciones real-time
    public Float escalaIntensidad = 1f;
    public Float distanciaEquilibrioResorte = 20f;
    public Float distanciaEntreNodos = 50000f;
    public Float centro = 0.25f; // Donde se pone el dedo en la parte izq de la guitarra
    public Float maxp = 0.1f; // Metaparametros de la friccion en las puntas
    public Float expp = 0.6f; // Metaparametros de la friccion en las puntas - este es el "orden" en el codigo
    public Float ordenMasa = 0.1f;

    public Float[] masaPorNodo;
    public boolean masaPorNodoEdited = false;
    public Float[] friccionSinDedo;
    public boolean friccionSinDedoEdited = false;
    public Float[] friccionConDedo;
    public boolean friccionConDedoEdited = false;
    public Float[] minimosYtrastes;
    public boolean minimosYtrastesEdited = false;

    public Map<Integer, Map<String, Float>> cuerdas = new HashMap<>();


    public State() {
        // Configuracion por default
        for (int i=0; i<6; i++) {
            cuerdas.put(i, new HashMap<>());
            cuerdas.get(i).put("friccion", 0.0721f);
            cuerdas.get(i).put("frecuencia", 82.1069f);
            cuerdas.get(i).put("nodos", 400f);
            cuerdas.get(i).put("maxFriccionEnPunta", 0.4f);
            cuerdas.get(i).put("anchoPuntas", 0.3f);
            cuerdas.get(i).put("distanciaCuerdaDiapason", -180f);
            cuerdas.get(i).put("distanciaCuerdaTraste", -150f);
        }
    }
}
