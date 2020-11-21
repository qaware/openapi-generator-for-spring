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
        if (mergedBeanDefinition instanceof RootBeanDefinition) {
            RootBeanDefinition rootBeanDefinition = (RootBeanDefinition) mergedBeanDefinition;
            return Optional.ofNullable(rootBeanDefinition.getResolvedFactoryMethod());
        }
        return Optional.empty();
    }


}
