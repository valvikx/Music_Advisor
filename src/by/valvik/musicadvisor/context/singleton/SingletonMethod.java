package by.valvik.musicadvisor.context.singleton;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

public record SingletonMethod(Object configObject,
                              Method method,
                              Parameter[] parameters) {
}
