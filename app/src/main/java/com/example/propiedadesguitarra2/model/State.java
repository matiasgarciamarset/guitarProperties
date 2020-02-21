package com.example.propiedadesguitarra2.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/*
 * La representacion es Pair<Integer, Integer> con <coef, exp> que es coef x 10 ^ exp
 */
public class State implements Serializable {

    private static final long serialVersionUID = 931824761779387905L;

    // Configuraciones que requieren reinicio
    public Pair<Integer, Integer> nFrames = Pair.create(1, 8); // Cantidad de iteraciones del programa
    public Pair<Integer, Integer> samplerate = Pair.create(4410, 0);
    public Pair<Integer, Integer> nbufferii = Pair.create(64, 0);
    public Pair<Integer, Integer> canalesEntrada = Pair.create(2, 0);
    public Pair<Integer, Integer> npot = Pair.create(2048, 0); // Tamaño tabla energia potencial
    public Pair<Integer, Integer> dedoSize = Pair.create(64, 0);
    public Pair<Integer, Integer> softrealtimeRefresh = Pair.create(200, 0);
    public Boolean debugMode = false;
    public Boolean imprimir = true;

    // Configuraciones real-time
    public Pair<Integer, Integer> escalaIntensidad = Pair.create(1, 0);
    public Pair<Integer, Integer> distanciaEquilibrioResorte = Pair.create(20, 0);
    public Pair<Integer, Integer> distanciaEntreNodos = Pair.create(5, 4);
    public Pair<Integer, Integer> centro = Pair.create(25, -2); // Donde se pone el dedo en la parte izq de la guitarra
    public Pair<Integer, Integer> maxp = Pair.create(1, -1); // Metaparametros de la friccion en las puntas
    public Pair<Integer, Integer> expp = Pair.create(6, -1); // Metaparametros de la friccion en las puntas - este es el "orden" en el codigo
    public Pair<Integer, Integer> ordenMasa = Pair.create(1, -1);

    public Float[] masaPorNodo;
    public boolean masaPorNodoEdited = false;
    public Float[] friccionSinDedo;
    public boolean friccionSinDedoEdited = false;
    public Float[] friccionConDedo;
    public boolean friccionConDedoEdited = false;
    public Float[] minimosYtrastes;
    public boolean minimosYtrastesEdited = false;

    public Map<Integer, Map<String, Pair<Integer, Integer>>> cuerdas = new HashMap<>();


    public State() {
        // Configuracion por default
        for (int i=0; i<6; i++) {
            cuerdas.put(i, new HashMap<>());
            cuerdas.get(i).put("friccion",  Pair.create(721, -5));
            cuerdas.get(i).put("frecuencia",  Pair.create(821069, -4));
            cuerdas.get(i).put("nodos",  Pair.create(400, 0));
            cuerdas.get(i).put("maxFriccionEnPunta",  Pair.create(4, -1));
            cuerdas.get(i).put("anchoPuntas",  Pair.create(3, -1));
            cuerdas.get(i).put("distanciaCuerdaDiapason",  Pair.create(-180, 0));
            cuerdas.get(i).put("distanciaCuerdaTraste",  Pair.create(-150, 0));
        }
    }
}
