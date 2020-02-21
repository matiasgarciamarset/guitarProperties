package com.example.propiedadesguitarra2;

import com.example.propiedadesguitarra2.model.Pair;
import com.example.propiedadesguitarra2.model.State;

import java.math.BigDecimal;
import java.util.function.Consumer;

public class MetaAlgorithms {

    private Integer lasUsedStateHash = null;

    /*
     * Recalcula la matriz correspondiente dependiendo del valor editado
     */
    public Boolean generate(String attributeName, State state) {
        if (!wasChanged(state)) {
            return false;
        }
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

    private static Consumer<State> defineMassByNode = state -> {
        Integer[] nodos = new Integer[state.cuerdas.size()];
        Integer sumNodos = 0;
        for (int i = 0; i < state.cuerdas.size(); i++) {
            nodos[i] = toNumber(state.cuerdas.get(i).get("nodos")).intValue();
            sumNodos += nodos[i];
        }

        Float[] masaPorNodo = new Float[sumNodos];
        int v = 0;
        float numeroMagico = 335.52f;
        Float ordenMasa = toNumber(state.ordenMasa).floatValue();

        for (int i = 0; i < state.cuerdas.size(); i++) {
            Float frecuencia = toNumber(state.cuerdas.get(i).get("frecuencia")).floatValue();
            Double masa = Math.pow((frecuencia * nodos[i]) / (numeroMagico * 512), 2);

            if (masa >= .9) masa = 0.9;

            Double ordenXmasa = ordenMasa * masa;
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
    private static Consumer<State> frictionWithoutFinger = state ->  {
        Integer[] nodos = new Integer[state.cuerdas.size()];
        Integer sumNodos = 0;
        for (int i = 0; i < state.cuerdas.size(); i++) {
            nodos[i] = toNumber(state.cuerdas.get(i).get("nodos")).intValue();
            sumNodos += nodos[i];
        }

        Float[] friccionSinDedo = new Float[sumNodos];
        int v = 0;

        for (int j = 0; j < state.cuerdas.size(); ++j) {
            Integer ultimo = nodos[j].intValue();

            Float orden = toNumber(state.cuerdas.get(j).get("anchoPuntas")).floatValue();
            Float fricc = toNumber(state.cuerdas.get(j).get(("friccion"))).floatValue();
            Float maxFriccionEnPunta = toNumber(state.cuerdas.get(j).get(("maxFriccionEnPunta"))).floatValue();
            for (int i = 0; i < ultimo; ++i) {

                Double factor =
                        (maxFriccionEnPunta - fricc) *
                                (Math.exp(-orden * (i) * (i)) + Math.exp(-orden * (i - ultimo) * (i - ultimo)));

                if (factor + fricc > 1.) {
                    System.out.println("El factor de la friccion sin dedo se va de lo permitido, reseteando a 0.9");
                    friccionSinDedo[v] = 0.9f;
                } else {
                    Double sum = factor + fricc;
                    friccionSinDedo[v] = sum.floatValue();
                }
                v++;
            }
        }
        state.friccionSinDedo = friccionSinDedo;
        state.friccionSinDedoEdited = true;
    };

    // USA: dedoSize, maxp, expp
    private static Consumer<State> frictionWithFinger = state ->  {
        Integer dedoSize = toNumber(state.dedoSize).intValue();

        Float[] friccionConDedo = new Float[dedoSize];

        Double maxp = toNumber(state.maxp).doubleValue();
        Double expp = toNumber(state.expp).doubleValue();
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
    private static Consumer<State> minAndFrets = state ->  {
        Integer cantMaximaNodos = -1;
        Integer[] nodos = new Integer[state.cuerdas.size()];
        for (int i = 0; i < state.cuerdas.size(); i++) {
            nodos[i] = toNumber(state.cuerdas.get(i).get("nodos")).intValue();
            if (cantMaximaNodos < nodos[i]) cantMaximaNodos = nodos[i];
        }

        Float[] minimosYtrastes = new Float[state.cuerdas.size() * cantMaximaNodos];

        for (int j = 0; j < state.cuerdas.size(); j++) {
            Float distanciaCuerdaDiapason = toNumber(state.cuerdas.get(j).get("distanciaCuerdaDiapason")).floatValue();
            Float distanciaCuerdaTraste = toNumber(state.cuerdas.get(j).get("distanciaCuerdaTraste")).floatValue();
            for (int i = 0; i < nodos[j]; i++) {
                if (i < nodos[j] / 2) {
                    minimosYtrastes[i + j * cantMaximaNodos] = distanciaCuerdaDiapason;
                } else {
                    minimosYtrastes[i + j * cantMaximaNodos] = 10 * distanciaCuerdaDiapason;
                }
            }
            for (int i = 0; i < 12; i++) {
                Double fdedo = 1 / 1.059463094;
                Integer nodotraste = (int) Math.round(nodos[j] * (1.0 - Math.pow(fdedo, i)));
                minimosYtrastes[nodotraste] = distanciaCuerdaTraste;
            }
        }

        state.minimosYtrastes = minimosYtrastes;
        state.minimosYtrastesEdited = true;
    };

    private static BigDecimal toNumber(Pair<Integer, Integer> value) {
        return new BigDecimal(value.first * Math.pow(10, value.second));
    }

    private boolean wasChanged(State state) {
        int hash = state.hashCode();
        if (lasUsedStateHash == null || hash != lasUsedStateHash) {
            lasUsedStateHash = hash;
            return true;
        }
        return false;
    }

}
