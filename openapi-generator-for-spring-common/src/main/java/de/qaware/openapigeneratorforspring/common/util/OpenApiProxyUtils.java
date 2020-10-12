package de.qaware.openapigeneratorforspring.common.util;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;

import javax.annotation.Nullable;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

public class OpenApiProxyUtils {
    private static final Object DO_NOT_INVOKE = new Object();

    public static <T> T immutableProxy(T delegate) {
        Set<String> setterNames = findSetterNames(delegate.getClass());
        return proxy(delegate,
                method -> setterNames.contains(method.getName()) ? method : null,
                (setterMethod, firstArg) -> DO_NOT_INVOKE
        );
    }

    public static <T> T smartImmutableProxy(T delegate) {
        // make also maps completely immutable
        return smartImmutableProxy(delegate, (currentMapValue, newMapValue) -> currentMapValue);
    }

    public static <T> T smartImmutableProxy(T delegate, BiFunction<Map<Object, Object>, Map<?, ?>, Map<?, ?>> onMapMerge) {
        Set<String> setterNames = findSetterNames(delegate.getClass());
        Map<String, Method> gettersBySetterName = buildGettersBySetterName(delegate.getClass(), setterNames);
        return proxy(delegate,
                method -> gettersBySetterName.get(method.getName()),
                (getter, newValue) -> {
                    Object currentValue = getter.invoke(delegate);
                    if (currentValue instanceof Map && newValue instanceof Map) {
                        //noinspection unchecked
                        return onMapMerge.apply((Map<Object, Object>) currentValue, (Map<?, ?>) newValue);
                    } else if (currentValue == null) {
                        return newValue;
                    }
                    return DO_NOT_INVOKE;
                });
    }

    private static <T, M> T proxy(T delegate, Function<Method, M> methodMapper, InvocationHandler<M> invocationHandler) {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(delegate.getClass());
        enhancer.setCallback((MethodInterceptor) (obj, method, args, methodProxy) -> {
            M mappedMethod = methodMapper.apply(method);
            if (mappedMethod == null) {
                return methodProxy.invoke(delegate, args);
            }
            if (args.length != 1) {
                throw new IllegalStateException("Although " + method + " is assumed to be a setter, it doesn't take exactly one argument but " + args.length);
            }
            Object mappedArgument = invocationHandler.handle(mappedMethod, args[0]);
            if (mappedArgument != DO_NOT_INVOKE) {
                methodProxy.invoke(delegate, new Object[]{mappedArgument});
            }
            // setter method doesn't return anything, so this return value doesn't matter
            return null;
        });
        //noinspection unchecked
        return (T) enhancer.create();
    }

    @FunctionalInterface
    private interface InvocationHandler<M> {
        @Nullable
        Object handle(M mappedMethod, Object firstArg) throws Exception;
    }

    public static Map<String, Method> buildGettersBySetterName(Class<?> clazz, Set<String> setterNames) {
        return Arrays.stream(clazz.getMethods())
                .filter(method -> method.getName().startsWith("get"))
                .map(method -> Pair.of(method.getName().replace("get", "set"), method))
                .filter(pair -> setterNames.contains(pair.getKey()))
                .collect(Collectors.toMap(Pair::getKey, Pair::getValue));
    }

    public static Set<String> findSetterNames(Class<?> clazz) {
        return Arrays.stream(clazz.getMethods())
                .filter(method -> method.getName().startsWith("set"))
                .map(Method::getName)
                .collect(Collectors.toSet());
    }

    public static Map<?, ?> addNonExistingKeys(Map<Object, Object> currentMapValue, Map<?, ?> newMapValue) {
        newMapValue.entrySet().stream()
                .filter(newEntry -> !currentMapValue.containsKey(newEntry.getKey()))
                .forEach(newEntry -> currentMapValue.put(newEntry.getKey(), newEntry.getValue()));
        return currentMapValue;
    }
}
