package by.valvik.musicadvisor.context.singleton;

import java.lang.reflect.Constructor;
import java.lang.reflect.Parameter;

public record SingletonCtor(Constructor<?> ctor) {

    public Parameter[] getParameters() {

        return ctor.getParameters();

    }

}
