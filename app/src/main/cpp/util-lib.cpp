//
// Created by matias on 08/08/20.
//

#include <jni.h>
#include <cstdlib>

float NUMERO_MAGICO = (335.52 * 512);
float F_DEDO = 1 / 1.059463094;

extern "C" {
    float Java_com_example_propiedadesguitarra2_MetaAlgorithms_rand(JNIEnv* env, jobject clazz) {
        return (float) rand() / RAND_MAX;
    }

    float Java_com_example_propiedadesguitarra2_MetaAlgorithms_calcularMasa(JNIEnv* env,
            jclass clazz,
            jfloat frecuencia,
            jint nodos) {

        return (float) pow((frecuencia * nodos) / NUMERO_MAGICO, 2);
    }

    float Java_com_example_propiedadesguitarra2_MetaAlgorithms_calcularFactor(JNIEnv* env,
                                                                            jclass clazz,
                                                                            jfloat maxFriccPunta,
                                                                            jfloat fricc,
                                                                            jfloat orden,
                                                                            jint ultimo,
                                                                            jint pos) {

        return (maxFriccPunta - fricc) *
           (exp(-orden * (pos) * (pos)) + exp(-orden * (pos - ultimo) * (pos - ultimo)));
    }

    float Java_com_example_propiedadesguitarra2_MetaAlgorithms_calcularFriccion(JNIEnv* env,
                                                                              jclass clazz,
                                                                              jfloat maxp,
                                                                              jfloat expp,
                                                                              jfloat centro,
                                                                              jint fi) {

        return maxp * exp(-expp * (fi-centro) * (fi-centro));
    }

    int Java_com_example_propiedadesguitarra2_MetaAlgorithms_calcularNodoTraste(JNIEnv* env,
                                                                                jclass clazz,
                                                                                jint nodos,
                                                                                jint pos){
        return (int) (nodos * (1. - pow(F_DEDO, pos)));
    }

}