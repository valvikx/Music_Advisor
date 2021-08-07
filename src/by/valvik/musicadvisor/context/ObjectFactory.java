package by.valvik.musicadvisor.context;

import by.valvik.musicadvisor.context.annotation.Inject;
import by.valvik.musicadvisor.context.configurator.ObjectConfigurator;
import by.valvik.musicadvisor.context.singleton.SingletonDefinition;
import by.valvik.musicadvisor.context.singleton.SingletonMethod;
import by.valvik.musicadvisor.exception.ConfigurationException;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.List;

import static java.util.Objects.nonNull;

public class ObjectFactory {

    private final ApplicationContext context;

    private final List<? extends ObjectConfigurator> configurators;

    public ObjectFactory(ApplicationContext context) {

        this.context = context;

        this.configurators = context.getConfig().getConfiguratorObjects();

    }

    public Object create(SingletonDefinition definition) {

        Object object = createByDefinition(definition);

        configurators.forEach(oc -> oc.configure(object, context));

        return object;

    }

    public <T> T create(Class<T> tClass) {

        try {

            T t = tClass.getDeclaredConstructor().newInstance();

            configurators.forEach(oc -> oc.configure(t, context));

            return t;

        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {

            throw new ConfigurationException(e);

        }

    }

    private Object createByDefinition(SingletonDefinition definition) {

        try {

            if (nonNull(definition.singletonCtor())) {

                Constructor<?> ctor = definition.singletonCtor().ctor();

                Parameter[] parameters = ctor.getParameters();

                if (parameters.length == 0) {

                    return ctor.newInstance();

                }

                Object[] parameterObjects = getParameterObjects(parameters);

                return ctor.newInstance(parameterObjects);

            }

            SingletonMethod singletonMethod = definition.singletonMethod();

            Parameter[] parameters = singletonMethod.parameters();

            if (parameters.length == 0) {

                return singletonMethod.method().invoke(singletonMethod.configObject());

            }

            Object[] parameterObjects = getParameterObjects(parameters);

            return singletonMethod.method().invoke(singletonMethod.configObject(), parameterObjects);

        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {

            throw new ConfigurationException(e);

        }

    }

    private Object[] getParameterObjects(Parameter[] parameters) {

        return Arrays.stream(parameters)
                     .map(p -> {

                         Class<?> parameterType = p.getType();

                         if (p.isAnnotationPresent(Inject.class)) {

                             String qualifier = p.getAnnotation(Inject.class).qualifier();

                             if (!qualifier.isEmpty()) {

                                 return context.getObject(qualifier, parameterType);

                             }

                         }

                         return context.getObject(parameterType);

                     })
                     .toArray();

    }

}