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
    public Float samplerate = 44100f; // 44100 48000 96000 192000
    public Float nbufferii = 64f; // > 32
    public Float canalesEntrada = 2f; // > 0
    public Float npot = 2048f; // Tamaño tabla energia potencial > 0
    public Float dedoSize = 64f; // > 0
    public Float palanca = 1f; // > 0
    public Boolean debugMode = false;
    public Boolean imprimir = true;
    public Float btBufferSize = 2048f; // > 0
    public Float cantCuerdas = 6f;

    // Configuraciones real-time
    public Float escalaIntensidad = 1f; // > 0 < 100
    public Float distanciaEquilibrioResorte = 20f; // > 0
    public Float distanciaEntreNodos = 50000f; // > 0
    public Float centro = 0.25f; // Donde se pone el dedo en la parte izq de la guitarra > 0 <= 0.5
    public Float maxp = 0.1f; // Metaparametros de la friccion en las puntas entre > 0  < 1
    public Float expp = 0.6f; // Metaparametros de la friccion en las puntas - este es el "orden" en el codigo > 0
    public Float ordenMasa = 0.1f; // entre > 0  < 0.5

    public Float[] masaPorNodo;
    public boolean masaPorNodoEdited = false;
    public Float[] friccionSinDedo;
    public boolean friccionSinDedoEdited = false;
    public Float[] friccionConDedo;
    public boolean friccionConDedoEdited = false;
    public Float[] minimosYtrastes;
    public boolean minimosYtrastesEdited = false;

    public Map<Integer, Map<String, Float>> cuerdas = new HashMap<>();

    // Solo para uso interno ->
    public boolean automaticSync = true;


    public State() {
        // Configuracion por default
        for (int i=0; i<cantCuerdas; i++) {
            setDefaultValues(i);
        }
    }

    public void setDefaultValues(int i) {
        cuerdas.put(i, new HashMap<>());
        cuerdas.get(i).put("friccion", 0.0721f); // entre 0 y 1
        cuerdas.get(i).put("frecuencia", 82.1069f); // > 0
        cuerdas.get(i).put("nodos", 400f); // > 0
        cuerdas.get(i).put("maxFriccionEnPunta", 0.4f); // entre 0 y 1
        cuerdas.get(i).put("anchoPuntas", 0.3f); // > 0
        cuerdas.get(i).put("distanciaCuerdaDiapason", -180f); // < 0 (no igual)
        cuerdas.get(i).put("distanciaCuerdaTraste", -150f); // < 0 (no igual)
    }
}
