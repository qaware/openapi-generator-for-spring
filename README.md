# OpenAPI v3 generator for Spring Boot

[![Build Status](https://github.com/qaware/openapi-generator-for-spring/workflows/build/badge.svg?branch=master)](https://github.com/qaware/openapi-generator-for-spring/actions?query=workflow%3A%22build%22)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=qaware_openapi-generator-for-spring&metric=alert_status)](https://sonarcloud.io/dashboard?id=qaware_openapi-generator-for-spring)
[![Code Coverage](https://sonarcloud.io/api/project_badges/measure?project=qaware_openapi-generator-for-spring&metric=coverage)](https://sonarcloud.io/dashboard?id=qaware_openapi-generator-for-spring)
[![Maven Central](https://img.shields.io/maven-central/v/de.qaware.tools.openapi-generator-for-spring/openapi-generator-for-spring-starter)](https://mvnrepository.com/artifact/de.qaware.tools.openapi-generator-for-spring/openapi-generator-for-spring-starter)

This library automagically generates a [OpenApi v3 specification](https://github.com/OAI/OpenAPI-Specification) at
runtime for Spring Boot applications.

It aims at fully analyzing Spring request mappings augmented by Swagger annotations to provide a self-descriptive API
specification of your application.

## Features

* Spring WebMVC support including content negotiation support
* Spring WebFlux support
  including [Router Functions](https://docs.spring.io/spring-framework/docs/current/reference/html/web-reactive.html#webflux-fn)
* Integrated [Swagger UI](https://github.com/swagger-api/swagger-ui)
* [Swagger Annotations v3](https://github.com/swagger-api/swagger-core/tree/master/modules/swagger-annotations) support
* No dependency on
  [swagger-core module](https://github.com/swagger-api/swagger-core/tree/master/modules/swagger-core)
  or
  [swagger-models module](https://github.com/swagger-api/swagger-core/tree/master/modules/swagger-models)
* Extended support for `Schema` building, including self-referencing types
  and [Jackson polymorphism](https://github.com/FasterXML/jackson-docs/wiki/JacksonPolymorphicDeserialization)

Note that some features are not fully implemented yet,
see [issues](https://github.com/qaware/openapi-generator-for-spring/issues).

## Getting started

Inside your Spring Boot application, add the following (maven) dependency:

```
<dependency>
    <groupId>de.qaware.tools.openapi-generator-for-spring</groupId>
    <artifactId>openapi-generator-for-spring-starter</artifactId>
    <version>4.0.0</version>
</dependency>
```

After starting your application, the OpenApi v3 specification of your application is provided at `/v3/api-docs` as JSON.
This specification can be viewed by visiting `/swagger-ui` inside your browser (relative to context path).

Have a look at the
[Demo for WebMVC](demo/openapi-generator-for-spring-demo-webmvc)
and
[Demo for WebFlux](demo/openapi-generator-for-spring-demo-webflux)
for a first impression.

## Configuration Properties

The following configuration properties are available with the prefix `openapi-generator-for-spring.`:

* [Open API Properties](openapi-generator-for-spring-common/src/main/java/de/qaware/openapigeneratorforspring/common/OpenApiConfigurationProperties.java)
* [Open API Info Properties](openapi-generator-for-spring-common/src/main/java/de/qaware/openapigeneratorforspring/common/info/OpenApiInfoConfigurationProperties.java)
* [Open API Swagger UI Properties](openapi-generator-for-spring-ui/src/main/java/de/qaware/openapigeneratorforspring/ui/OpenApiSwaggerUiConfigurationProperties.java)

The following extension properties are currently part of this library:

* [Java8 Time](openapi-generator-for-spring-common/src/main/java/de/qaware/openapigeneratorforspring/common/schema/resolver/type/extension/java8/Java8TimeTypeResolverConfigurationProperties.java)

## How To

### How to filter paths, operations and parameters within the specification?

The library supports filtering at the following stages during OpenApi specification building, in this order:

1. [HandlerMethodFilter](openapi-generator-for-spring-api/src/main/java/de/qaware/openapigeneratorforspring/common/filter/handlermethod/HandlerMethodFilter.java)
   filters before passing on the found handler methods to the path building.
1. [OperationPreFilter](openapi-generator-for-spring-api/src/main/java/de/qaware/openapigeneratorforspring/common/filter/operation/OperationPreFilter.java)
   filters before the `Operation` model object is built.
1. [OperationParameterPreFilter](openapi-generator-for-spring-api/src/main/java/de/qaware/openapigeneratorforspring/common/filter/operation/parameter/OperationParameterPreFilter.java)
   filters before a `Parameter` model object of an operation is built.
1. [OperationParameterPostFilter](openapi-generator-for-spring-api/src/main/java/de/qaware/openapigeneratorforspring/common/filter/operation/parameter/OperationParameterPostFilter.java)
   filters after a `Parameter` has been built.
1. [OperationPostFilter](openapi-generator-for-spring-api/src/main/java/de/qaware/openapigeneratorforspring/common/filter/operation/OperationPostFilter.java)
   filters after a `Operation` has been built. All information has been set except referenced components.
1. [PathItemFilter](openapi-generator-for-spring-api/src/main/java/de/qaware/openapigeneratorforspring/common/filter/pathitem/PathItemFilter.java)
   filters after a `PathItem` is fully constructed. All information has been set except referenced components.

Insert a bean extending one or more of the above interfaces, which will be picked up by the library.

The later the filter is called the more work has been done, but also more information is supplied to make the filtering
decision. Possibly referenced components must be manually cleared later on in an extra customization step if filtering
happened too late. It is thus recommended applying filtering as early as possible.

### How to obtain a grouped OpenAPI specification?

Grouping is realized by applying filters, preferably a `OperationPreFilter`, while building the OpenAPI specification.
Query and header parameters of the HTTP request to `/v3/api-docs` can be obtained within the filter by auto-wiring the
bean of type `OpenApiRequestParameterProvider`.

This way, you can control which operations are considered for the specification when `/v3/api-docs?group=MyGroup`. The
Swagger UI can also be customized to display more than one specification by providing a bean of type
`OpenApiSwaggerUiApiDocsUrisSupplier`.

See the `*Configuration` class of this
[integration test](openapi-generator-for-spring-test/src/test/java/de/qaware/openapigeneratorforspring/test/app10)
or this
[integration test](openapi-generator-for-spring-test/src/test/java/de/qaware/openapigeneratorforspring/test/app18)
for a fully working WebMVC or WebFlux example, respectively. Note that the test cases invoke the endpoint with different
header or query parameters.

### How to customize the specification?

The library offers various customizers, which allow the library user to (hopefully) adapt to any use case which comes to
mind.

The following shows some examples for customizers:

* [SchemaCustomizer](openapi-generator-for-spring-api/src/main/java/de/qaware/openapigeneratorforspring/common/schema/customizer/SchemaCustomizer.java)
  customizes a constructed
  [Schema](openapi-generator-for-spring-model/src/main/java/de/qaware/openapigeneratorforspring/model/media/Schema.java)
  . An example is the
  [SchemaCustomizerForNullable](openapi-generator-for-spring-common/src/main/java/de/qaware/openapigeneratorforspring/common/schema/customizer/SchemaCustomizerForNullable.java)
  .
* [OperationCustomizer](openapi-generator-for-spring-api/src/main/java/de/qaware/openapigeneratorforspring/common/operation/customizer/OperationCustomizer.java)
  customizes a constructed
  [Operation](openapi-generator-for-spring-model/src/main/java/de/qaware/openapigeneratorforspring/model/operation/Operation.java)
  . An example is
  the [DefaultOperationIdCustomizer](openapi-generator-for-spring-common/src/main/java/de/qaware/openapigeneratorforspring/common/operation/customizer/DefaultOperationIdCustomizer.java)
  which sets the Operation ID from an
  [OperationIdProvider]() bean.
* This
  [integration test](openapi-generator-for-spring-test/src/test/java/de/qaware/openapigeneratorforspring/test/app9/App9Configuration.java)
  shows an example how to override the default identifier when referencing request bodies.

Feel free to investigate the
[api module](openapi-generator-for-spring-api/src/main/java/de/qaware/openapigeneratorforspring/common)
for more details. The relevant interfaces all have the suffix `Customizer` and extend the `Ordered` interface.

#### Examples
OperationCustomizer bean that uses the class name of the RestController to set it as OpenAPI tag
```
    /**
     * Provide an OperationCustomizer to use the class name of the REST controller as Tag. This way the API endpoints are grouped per REST controller.
     *
     * @return OperationCustomizer
     */
    @Bean
    public OperationCustomizer operationTagCustomizer() {
        return (operation, operationBuilderContext) -> {
            if (CollectionUtils.isEmpty(operation.getTags())) {
                operationBuilderContext.getHandlerMethod(SpringWebHandlerMethod.class).ifPresent(handlerMethod -> {
                    String declaringClassName = handlerMethod.getMethod().getMethod().getDeclaringClass().getSimpleName();
                    operation.setTags(singletonList(declaringClassName));
                    operationBuilderContext.getReferencedItemConsumer(ReferencedTagsConsumer.class)
                            .accept(singletonList(Tag.builder().name(declaringClassName).build()));
                });
            }
        };
    }

```

OperationIdProvider to generate deep-links to specific endpoints that are compatible to the SpringFox style
```
    /**
     * Provide an OperationIdProvider to generate deep-links that are compatible to the SpringFox style.
     *
     * @return OperationIdProvider
     */
    @Bean
    public OperationIdProvider operationIdProvider() {
        return operationInfo -> operationInfo.getHandlerMethod().getIdentifier() + "Using" + operationInfo.getRequestMethod().name();
    }
```



### How to customize the included Swagger UI?

The Swagger UI `index.html` is generated from a
[Mustache template](openapi-generator-for-spring-ui/src/main/resources/swagger-ui/index.html.mustache)
and uses the
[Swagger UI Webjar](https://github.com/webjars/swagger-ui)
distribution.

The default implementation of
[OpenApiSwaggerUiApiDocsUrisSupplier](openapi-generator-for-spring-ui/src/main/java/de/qaware/openapigeneratorforspring/ui/OpenApiSwaggerUiApiDocsUrisSupplier.java)
uses the given URI to the API Docs endpoint and names the entry `Default`. This name is ignored as there is only one
entry in the Swagger UI.

See this
[integration test](openapi-generator-for-spring-test/src/test/java/de/qaware/openapigeneratorforspring/test/app10/App10Configuration.java)
for an example how to customize the offered API Docs within the Swagger UI.

### How to substitute a type for Schema resolution?

Define the following bean of type `InitialTypeBuilder` if you want to resolve the type `YourType` always as if it was of
type `String`:

```
@Bean
public InitialTypeBuilder openApiSchemaTypeSubstitutionForYourType() {
    return (javaType, annotationsSupplier, recursiveBuilder) -> {
        if(javaType.getRawClass().equals(YourType.class)) {
            return recursiveBuilder.build(String.class, annotationsSupplier);
        }
        return null;
    };
}
```

### How to handle error responses elegantly?

Spring (Boot) offers to handle exceptions from handler methods via
`@ExceptionHandler` annotated methods. This mechanism can be plugged into the specification by using
an `OperationCustomizer`, which scans a `SpringWebHandlerMethod` for its exception signature and adds
additional `ApiResponse`s to the `Operation` for each exception.

One can even figure out the `@ExceptionHandler` method, as Spring would do, to automatically determine the `Schema` of
the error response and also scan for `@ReponseStatus` to determine the error code.

It is also possible to throw an error when there is an exception declared but no appropriate `@ExceptionHandler` method
is found.

See [this configuration of the integration test](openapi-generator-for-spring-test/src/test/java/de/qaware/openapigeneratorforspring/test/app32/App32Configuration.java)
for a fully worked out example.

## Why not another library?

This library is based on experience while using [Spring Fox](https://github.com/springfox/springfox)
and [SpringDoc OpenApi](https://github.com/springdoc/springdoc-openapi). As those libraries have turned out to be not
flexible enough for our internal projects, this library aims at being fully customizable.

Composing Spring beans in combination with Spring Boot autoconfiguration enable this flexibility. The library offers
opinionated (and hopefully sane) default implementations but marks all of them with `@ConditionalOnMissingBean`. It is
encouraged to override those defaults by providing own bean implementations.

Default implementations in `common` module will use the `api`
module interfaces as much as possible. This way it is ensured that the provided api module actually offers useful
interfaces.

## Module structure

All the following module names are prefixed with `openapi-generator-for-spring-`.

**[model](openapi-generator-for-spring-model)** contains Jackson-serializable POJOs to represent
the [OpenApi v3 model](https://github.com/OAI/OpenAPI-Specification/blob/3.0.1/versions/3.0.1.md).

**[api](openapi-generator-for-spring-api)** contains the public API to extend and adapt the functionality of this
library.

**[common](openapi-generator-for-spring-common)** contains the main algorithm parsing the swagger-annotated handler
methods. It provides default implementations for most of the interfaces in `openapi-generator-for-spring-api` module.

**[autoconfigure](openapi-generator-for-spring-autoconfigure)** contains the Spring Boot auto-configuration of common
beans.

**[web](openapi-generator-for-spring-web)** shared support code for Spring WebMVC and WebFlux including shared
auto-configuration.

**[webmvc](openapi-generator-for-spring-webmvc)** supports Spring WebMVC via Spring Boot auto-configuration.

**[webflux](openapi-generator-for-spring-webflux)** supports Spring WebFlux via Spring Boot auto-configuration.

**[ui](openapi-generator-for-spring-ui)** Provides Swagger UI below `/swagger-ui`. Offers UI Resource transformation for
WebMVC and WebFlux and sets up correct redirect routes.

**[starter](openapi-generator-for-spring-starter)** Opinionated choice of dependencies. Enables Swagger UI and support
for WebMVC and WebFlux if present.

**[test](openapi-generator-for-spring-test)** contains all integration tests for WebMVC and WebFlux.

**[shaded](openapi-generator-for-spring-shaded)** contains shaded dependencies to avoid interference with Spring Boot
autoconfiguration. This library does not want to trigger Mustache or WebJar exposure for users of this library. Shading
is the only mechanism which still allows to use such code but hide it from Spring Boot.

Each module aims to have minimal dependencies. General library dependencies are:

* Spring Core (Web/WebMVC/WebFlux are optional)
* Jackson
* Swagger Annotations
* WebJar for Swagger UI (shaded)
* Mustache (shaded)
* Apache Commons

## Development & Contributing

Please open an issue before posting a pull request unless you have a very obvious fix or improvement to contribute.

When developing with IntelliJ, run `mvn clean package -DskipTests` first in order to get the integration tests also
running from within the IDE.
