package de.qaware.openapigeneratorforspring.common.util;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

public class OpenApiProxyUtils {

    public static <T> T smartImmutableProxy(T instance) {
        return smartImmutableProxy(instance, (currentMapValue, newMapValue) -> currentMapValue);
    }

    public static <T> T smartImmutableProxy(T instance, BiFunction<Map<Object, Object>, Map<?, ?>, Map<?, ?>> onMapMerge) {
        Set<String> setterNames = Arrays.stream(instance.getClass().getMethods())
                .filter(method -> method.getName().startsWith("set"))
                .map(Method::getName)
                .collect(Collectors.toSet());

        Map<String, Method> getterBySetterName = Arrays.stream(instance.getClass().getMethods())
                .filter(method -> method.getName().startsWith("get"))
                .map(method -> Pair.of(method.getName().replace("get", "set"), method))
                .filter(pair -> setterNames.contains(pair.getKey()))
                .collect(Collectors.toMap(Pair::getKey, Pair::getValue));

        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(instance.getClass());
        enhancer.setCallback((MethodInterceptor) (obj, method, args, methodProxy) -> {
            Method correspondingGetter = getterBySetterName.get(method.getName());
            if (correspondingGetter == null) {
                return methodProxy.invoke(instance, args);
            }
            Object currentValue = correspondingGetter.invoke(instance);
            if (currentValue instanceof Map && args.length == 1 && args[0] instanceof Map) {
                @SuppressWarnings("unchecked")
                Map<?, ?> mergedValue = onMapMerge.apply((Map<Object, Object>) currentValue, (Map<?, ?>) args[0]);
                methodProxy.invoke(instance, new Object[]{mergedValue});
            } else if (currentValue == null) {
                methodProxy.invoke(instance, args);
            }
            // setter method doesn't return anything
            return null;
        });

        //noinspection unchecked
        return (T) enhancer.create();
    }

    public static Map<?, ?> addNonExistingKeys(Map<Object, Object> currentMapValue, Map<?, ?> newMapValue) {
        newMapValue.entrySet().stream()
                .filter(newEntry -> !currentMapValue.containsKey(newEntry.getKey()))
                .forEach(newEntry -> currentMapValue.put(newEntry.getKey(), newEntry.getValue()));
        return currentMapValue;
    }
}
