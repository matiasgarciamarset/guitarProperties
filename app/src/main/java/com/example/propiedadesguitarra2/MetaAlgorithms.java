package com.example.propiedadesguitarra2;

import com.example.propiedadesguitarra2.model.State;

public class MetaAlgorithms {

    Integer cantMaximaNodos = -1;
    Integer[] nodos;
    Integer sumNodos = 0;

    static {
        System.loadLibrary("util-lib");
    }

    native float rand();
    static native float calcularMasa(float frecuencia, int nodos);
    static native float calcularFactor(float maxFriccPunta, float fricc, float orden, int ultimo, int pos);
    static native float calcularFriccion(float maxp, float expp, float centro, int fi);
    static native int calcularNodoTraste(int nodos, int pos);

    /*
     * Recalcula la matriz correspondiente dependiendo del valor editado
     */
    public Boolean generate(String attributeName, State state) {
        initDefaults(state);
        switch (attributeName) {
            case "frecuencia":
                defineMassByNode(state);
                break;
            case "nodos":
            case "cantCuerdas":
                defineMassByNode(state);
                frictionWithoutFinger(state);
                minAndFrets(state);
                break;
            case "ordenMasa":
                defineMassByNode(state);
                break;
            case "anchoPuntas":
                frictionWithoutFinger(state);
                break;
            case "friccion":
                frictionWithoutFinger(state);
                break;
            case "maxFriccionEnPunta":
                frictionWithoutFinger(state);
                break;
            case "dedoSize":
                frictionWithFinger(state);
                break;
            case "maxp":
                frictionWithFinger(state);
                break;
            case "expp":
                frictionWithFinger(state);
                break;
            case "distanciaCuerdaDiapason":
                minAndFrets(state);
                break;
            case "distanciaCuerdaTraste":
                minAndFrets(state);
                break;
            default:
                 return false;
        }
        return true;
    }

    private void defineMassByNode(State state) {
        Float[] masaPorNodo = new Float[sumNodos];

        int v = 0;
        float ordenMasa = state.ordenMasa;

        for (int i = 0; i < state.cuerdas.size(); i++) {
            float masa =
               calcularMasa(state.cuerdas.get(i).get("frecuencia"), nodos[i]);

            if (masa > .9f) {
                System.out.println("El valor de masa se va de lo permitido, reseteando a 0.9");
                masa = .9f;
            }

            float ordenXmasa = ordenMasa * masa;
            for (int j = 0; j < nodos[i]; ++j) {
                masaPorNodo[v] = masa - (1f - rand()) * ordenXmasa;
                v++;
            }
        }

        state.masaPorNodo = masaPorNodo;
        state.masaPorNodoEdited = true;
    }

    // USA: nodos, anchoPuntas, friccion, maxFriccionEnPunta
    private void frictionWithoutFinger(State state) {
        Float[] friccionSinDedo = new Float[sumNodos];
        int v = 0;

        for (int j = 0; j < state.cuerdas.size(); ++j) {
            Integer ultimo = nodos[j].intValue();

            Float orden = state.cuerdas.get(j).get("anchoPuntas");
            Float fricc = state.cuerdas.get(j).get("friccion");
            Float maxFriccionEnPunta = state.cuerdas.get(j).get("maxFriccionEnPunta");

            for (int i = 0; i < ultimo; ++i) {

                float factor = calcularFactor(maxFriccionEnPunta, fricc, orden, ultimo, i);
                float suma = factor + fricc;

                if (suma >= 1.) {
                    System.out.println("El factor de la friccion sin dedo se va de lo permitido, reseteando a 0.9");
                    friccionSinDedo[v] = .9f;
                } else {
                    friccionSinDedo[v] = suma;
                }
                v++;
            }
        }
        state.friccionSinDedo = friccionSinDedo;
        state.friccionSinDedoEdited = true;
    };

    // USA: dedoSize, maxp, expp
    private void frictionWithFinger(State state) {
        Integer dedoSize = state.dedoSize.intValue();

        Float[] friccionConDedo = new Float[dedoSize];

        Float maxp = state.maxp;
        Float expp = state.expp;
        Integer centro = dedoSize / 2;
        for (int i = 0; i < dedoSize; i++) {

            float friccion = calcularFriccion(maxp, expp, centro, i);

            if (friccion > .9f) {
                System.out.printf("El valor de la friccion con dedo se va de lo permitido, reseteando a 0.9");
                friccion = .9f;
            }

            friccionConDedo[i] = friccion;
        }
        state.friccionConDedo = friccionConDedo;
        state.friccionConDedoEdited = true;
    };

    // USA: nodos, distanciaCuerdaDiapason, distanciaCuerdaTraste
    private void minAndFrets(State state) {
        Float[] minimosYtrastes = new Float[state.cuerdas.size() * cantMaximaNodos];
        Float fdedo = 1 / 1.059463094f;

        for (int j = 0; j < state.cuerdas.size(); j++) {
            Float distanciaCuerdaDiapason = state.cuerdas.get(j).get("distanciaCuerdaDiapason");
            Float distanciaCuerdaTraste = state.cuerdas.get(j).get("distanciaCuerdaTraste");
            for (int i = 0; i < nodos[j]; i++) {
                if (i < nodos[j] / 2) {
                    minimosYtrastes[i + j * cantMaximaNodos] = distanciaCuerdaDiapason;
                } else {
                    minimosYtrastes[i + j * cantMaximaNodos] = 10 * distanciaCuerdaDiapason;
                }
            }
            for (int i = 0; i < 12; i++) {
                Integer nodotraste = calcularNodoTraste(nodos[j], i);
                minimosYtrastes[nodotraste] = distanciaCuerdaTraste;
            }
        }

        state.minimosYtrastes = minimosYtrastes;
        state.minimosYtrastesEdited = true;
    };

    private void initDefaults(State state) {
        cantMaximaNodos = -1;
        sumNodos = 0;
        nodos = new Integer[state.cuerdas.size()];
        for (int i = 0; i < state.cuerdas.size(); i++) {
            nodos[i] = state.cuerdas.get(i).get("nodos").intValue();
            sumNodos += nodos[i];
            if (cantMaximaNodos < nodos[i]) cantMaximaNodos = nodos[i];
        }
    }
}
