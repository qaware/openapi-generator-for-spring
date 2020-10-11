package de.qaware.openapigeneratorforspring.common.info;

import de.qaware.openapigeneratorforspring.common.annotation.AnnotationsSupplier;
import de.qaware.openapigeneratorforspring.common.annotation.AnnotationsSupplierFactory;
import de.qaware.openapigeneratorforspring.common.mapper.InfoAnnotationMapper;
import de.qaware.openapigeneratorforspring.model.info.Contact;
import de.qaware.openapigeneratorforspring.model.info.Info;
import de.qaware.openapigeneratorforspring.model.info.License;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeansException;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.ClassUtils;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.Optional;

import static de.qaware.openapigeneratorforspring.common.util.OpenApiObjectUtils.setIfNotNull;

@RequiredArgsConstructor
public class DefaultOpenApiInfoSupplier implements OpenApiInfoSupplier, ApplicationContextAware {
    private final OpenApiInfoConfigurationProperties properties;
    private final AnnotationsSupplierFactory annotationsSupplierFactory;
    private final InfoAnnotationMapper infoAnnotationMapper;
    private final OpenApiVersionSupplier openApiVersionSupplier;

    @Nullable
    private ApplicationContext applicationContext;

    @Override
    public Info get() {
        Info info = findSpringBootApplicationClass().map(springBootApplicationClass -> {
            AnnotationsSupplier annotationsSupplier = annotationsSupplierFactory.createFromAnnotatedElement(springBootApplicationClass);
            // TODO parse more from OpenAPIDefinition annotation
            return Optional.ofNullable(annotationsSupplier.findFirstAnnotation(OpenAPIDefinition.class))
                    .map(OpenAPIDefinition::info)
                    .map(infoAnnotationMapper::map)
                    .orElseGet(() -> Info.builder()
                            .title("API for " + springBootApplicationClass.getSimpleName())
                            .build()
                    );
        }).orElseGet(() -> Info.builder().build()); // this fallback should never happen, it means we're not running with Spring Boot

        // application properties should take effect with higher prio
        setIfNotNull(properties.getTitle(), info::setTitle);
        setIfNotNull(properties.getDescription(), info::setDescription);
        setIfNotNull(properties.getTermsOfService(), info::setTermsOfService);
        setIfNotNull(properties.getContact(), contactConfig -> {
            Contact contact = Optional.ofNullable(info.getContact())
                    .orElseGet(Contact::new);
            setIfNotNull(contactConfig.getName(), contact::setName);
            setIfNotNull(contactConfig.getUrl(), contact::setUrl);
            setIfNotNull(contactConfig.getEmail(), contact::setEmail);
            setIfNotNull(contactConfig.getExtensions(), contact::setExtensions);
            info.setContact(contact);
        });
        setIfNotNull(properties.getLicense(), licenseConfig -> {
            License license = Optional.ofNullable(info.getLicense())
                    .orElseGet(License::new);
            setIfNotNull(licenseConfig.getName(), license::setName);
            setIfNotNull(licenseConfig.getUrl(), license::setUrl);
            setIfNotNull(licenseConfig.getExtensions(), license::setExtensions);
            info.setLicense(license);
        });
        setIfNotNull(properties.getVersion(), info::setVersion);
        setIfNotNull(properties.getExtensions(), info::setExtensions);

        if (info.getVersion() == null) {
            info.setVersion(openApiVersionSupplier.get());
        }

        return info;
    }

    private Optional<Class<?>> findSpringBootApplicationClass() {
        if (applicationContext != null) {
            Map<String, Object> applicationBeans = applicationContext.getBeansWithAnnotation(SpringBootApplication.class);
            if (applicationBeans.size() == 1) {
                return Optional.of(applicationBeans.values().iterator().next())
                        .map(Object::getClass)
                        // remove Spring proxy classes
                        .map(ClassUtils::getUserClass);
            }
        }
        return Optional.empty();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
