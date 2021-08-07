package by.valvik.musicadvisor.context.singleton;

import java.lang.reflect.Field;
import java.util.Map;

public record SingletonDefinition(String name,
                                  Class<?> singletonClass,
                                  SingletonCtor singletonCtor,
                                  SingletonMethod singletonMethod,
                                  Map<Field, Class<?>> dependencies) {
}
