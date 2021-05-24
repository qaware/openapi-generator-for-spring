/*-
 * #%L
 * OpenAPI Generator for Spring Boot :: Common
 * %%
 * Copyright (C) 2020 QAware GmbH
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

package de.qaware.openapigeneratorforspring.common.util;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;

import javax.annotation.Nullable;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

public class OpenApiProxyUtils {
    private static final Object DO_NOT_INVOKE = new Object();
    private static final String VALUE_FIELD_NAME = "value";

    public static <T, A extends Annotation> Function<T, A> createAnnotationProxyWithValueFactory(Class<A> clazz, Class<T> valueClazz) {
        Optional<Method> valueMethod = Arrays.stream(clazz.getDeclaredMethods())
                .filter(m -> VALUE_FIELD_NAME.equals(m.getName()))
                .findAny();
        if (!valueMethod.isPresent() || !valueClazz.equals(valueMethod.get().getReturnType())) {
            throw new IllegalStateException("The annotation " + clazz + " should have a " + VALUE_FIELD_NAME + " method with return type " + valueClazz.getName());
        }
        return value -> clazz.cast(Proxy.newProxyInstance(
                clazz.getClassLoader(),
                new Class<?>[]{clazz},
                (proxy, method, args) -> {
                    if (method.getName().equals(VALUE_FIELD_NAME)) {
                        return value;
                    }
                    return method.invoke(proxy, args);
                }
        ));
    }

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
        Object handle(M mappedMethod, Object firstArg) throws InvocationTargetException, IllegalAccessException;
    }

    private static Map<String, Method> buildGettersBySetterName(Class<?> clazz, Set<String> setterNames) {
        return Arrays.stream(clazz.getMethods())
                .filter(method -> method.getName().startsWith("get"))
                .map(method -> Pair.of(method.getName().replace("get", "set"), method))
                .filter(pair -> setterNames.contains(pair.getKey()))
                .collect(Collectors.toMap(Pair::getKey, Pair::getValue));
    }

    private static Set<String> findSetterNames(Class<?> clazz) {
        return Arrays.stream(clazz.getMethods())
                .map(Method::getName)
                .filter(name -> name.startsWith("set"))
                .collect(Collectors.toSet());
    }

    @SuppressWarnings("java:S1452")
    public static Map<?, ?> addNonExistingKeys(Map<Object, Object> currentMapValue, Map<?, ?> newMapValue) {
        newMapValue.entrySet().stream()
                .filter(newEntry -> !currentMapValue.containsKey(newEntry.getKey()))
                .forEach(newEntry -> currentMapValue.put(newEntry.getKey(), newEntry.getValue()));
        return currentMapValue;
    }
}
