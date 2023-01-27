/*-
 * #%L
 * OpenAPI Generator for Spring Boot :: WebFlux
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

package de.qaware.openapigeneratorforspring.webflux.function;

import de.qaware.openapigeneratorforspring.common.annotation.AnnotationsSupplier;
import de.qaware.openapigeneratorforspring.common.annotation.AnnotationsSupplierFactory;
import de.qaware.openapigeneratorforspring.common.paths.HandlerMethodWithInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.web.reactive.function.server.RouterFunction;

import java.lang.reflect.Method;
import java.util.Optional;

@RequiredArgsConstructor
public class RouterFunctionHandlerMethodWithInfoBuilder {

    private final AnnotationsSupplierFactory annotationsSupplierFactory;
    private final ConfigurableListableBeanFactory beanFactory;

    public HandlerMethodWithInfo build(String beanName, RouterFunction<?> routerFunction) {
        Optional<Method> beanFactoryMethod = findBeanFactoryMethod(beanName);
        RouterFunctionAnalysis.Result routerFunctionAnalysisResult = RouterFunctionAnalysis.analyze(routerFunction);
        return new HandlerMethodWithInfo(
                new RouterFunctionHandlerMethod(
                        beanName,
                        beanFactoryMethod.map(annotationsSupplierFactory::createFromMethodWithDeclaringClass)
                                .orElse(AnnotationsSupplier.EMPTY),
                        routerFunction,
                        routerFunctionAnalysisResult
                ),
                routerFunctionAnalysisResult.getPaths(),
                routerFunctionAnalysisResult.getRequestMethods()
        );
    }

    private Optional<Method> findBeanFactoryMethod(String beanName) {
        BeanDefinition mergedBeanDefinition = beanFactory.getMergedBeanDefinition(beanName);
        if (mergedBeanDefinition instanceof RootBeanDefinition rootBeanDefinition) {
            return Optional.ofNullable(rootBeanDefinition.getResolvedFactoryMethod());
        }
        return Optional.empty();
    }


}
