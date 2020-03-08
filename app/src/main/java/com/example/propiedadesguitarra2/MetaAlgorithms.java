package com.example.propiedadesguitarra2;

import com.example.propiedadesguitarra2.model.State;

import java.util.function.Consumer;

public class MetaAlgorithms {

    Integer cantMaximaNodos = -1;
    Integer[] nodos;
    Integer sumNodos = 0;

    /*
     * Recalcula la matriz correspondiente dependiendo del valor editado
     */
    public Boolean generate(String attributeName, State state) {
        initDefaults(state);
        switch (attributeName) {
            case "frecuencia":
                defineMassByNode.accept(state);
                break;
            case "nodos":
                defineMassByNode.accept(state);
                frictionWithoutFinger.accept(state);
                minAndFrets.accept(state);
                break;
            case "ordenMasa":
                defineMassByNode.accept(state);
                break;
            case "anchoPuntas":
                frictionWithoutFinger.accept(state);
                break;
            case "friccion":
                frictionWithoutFinger.accept(state);
                break;
            case "maxFriccionEnPunta":
                frictionWithoutFinger.accept(state);
                break;
            case "dedoSize":
                frictionWithFinger.accept(state);
                break;
            case "maxp":
                frictionWithFinger.accept(state);
                break;
            case "expp":
                frictionWithFinger.accept(state);
                break;
            case "distanciaCuerdaDiapason":
                minAndFrets.accept(state);
                break;
            case "distanciaCuerdaTraste":
                minAndFrets.accept(state);
                break;
             default:
                 return false;
        }
        return true;
    }

    private Consumer<State> defineMassByNode = state -> {
        Float[] masaPorNodo = new Float[sumNodos];
        int v = 0;
        float numeroMagico = 335.52f;
        Float ordenMasa = state.ordenMasa;

        for (int i = 0; i < state.cuerdas.size(); i++) {
            Float frecuencia = state.cuerdas.get(i).get("frecuencia");
            Double masa = Math.pow(((frecuencia * nodos[i]) / (numeroMagico * 512)), 2);

            if (masa >= .9) {
                System.out.println("El valor de masa se va de lo permitido, reseteando a 0.9");
                masa = .9;
            }

            Float ordenXmasa = ordenMasa * masa.floatValue();
            for (int j = 0; j < nodos[i]; ++j) {
                Double result = masa - (1.0 - Math.random()) * ordenXmasa;
                masaPorNodo[v] = result.floatValue();
                v++;
            }
        }

        state.masaPorNodo = masaPorNodo;
        state.masaPorNodoEdited = true;
    };

    // USA: nodos, anchoPuntas, friccion, maxFriccionEnPunta
    private Consumer<State> frictionWithoutFinger = state ->  {
        Float[] friccionSinDedo = new Float[sumNodos];
        int v = 0;

        for (int j = 0; j < state.cuerdas.size(); ++j) {
            Integer ultimo = nodos[j].intValue();

            Float orden = state.cuerdas.get(j).get("anchoPuntas");
            Float fricc = state.cuerdas.get(j).get(("friccion"));
            Float maxFriccionEnPunta = state.cuerdas.get(j).get(("maxFriccionEnPunta"));
            for (int i = 0; i < ultimo; ++i) {

                Double factor =
                        (maxFriccionEnPunta - fricc) *
                                (Math.exp(-orden * (i) * (i)) + Math.exp(-orden * (i - ultimo) * (i - ultimo)));

                if (factor + fricc > 1.) {
                    System.out.println("El factor de la friccion sin dedo se va de lo permitido, reseteando a 0.9");
                    friccionSinDedo[v] = .9f;
                } else {
                    friccionSinDedo[v] = factor.floatValue() + fricc;
                }
                v++;
            }
        }
        state.friccionSinDedo = friccionSinDedo;
        state.friccionSinDedoEdited = true;
    };

    // USA: dedoSize, maxp, expp
    private Consumer<State> frictionWithFinger = state ->  {
        Integer dedoSize = state.dedoSize.intValue();

        Float[] friccionConDedo = new Float[dedoSize];

        Float maxp = state.maxp;
        Float expp = state.expp;
        Integer centro = dedoSize / 2;
        for (int i = 0; i < dedoSize; i++) {
            float fi=i;
            Double friccion = maxp * Math.exp(-expp * (fi-centro) * (fi-centro));

            if (friccion > .9) {
                System.out.printf("El valor de la friccion con dedo se va de lo permitido, reseteando a 0.9");
                friccion = 0.9;
            }

            friccionConDedo[i] = friccion.floatValue();
        }
        state.friccionConDedo = friccionConDedo;
        state.friccionConDedoEdited = true;
    };

    // USA: nodos, distanciaCuerdaDiapason, distanciaCuerdaTraste
    private Consumer<State> minAndFrets = state ->  {
        Float[] minimosYtrastes = new Float[state.cuerdas.size() * cantMaximaNodos];

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
                Double fdedo = 1 / 1.059463094;
                Integer nodotraste = (int) (nodos[j] * (1.0 - Math.pow(fdedo, i)));
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
