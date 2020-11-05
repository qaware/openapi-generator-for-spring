# OpenAPI v3 generator for Spring Boot

This library automagically generates a OpenApi v3 specification at runtime for Spring Boot applications.

It aims at fully supporting the Swagger Annotations to provide
a self-descriptive API specifcation of your application.

## Getting started

Inside your Spring Boot application, add the following (maven) dependency:
```
<dependency>
	<groupId>de.qaware.openapi-generator-for-spring</groupId>
	<artifactId>openapi-generator-for-spring-starter</artifactId>
	<!-- TODO use most recent released version -->
	<version>PUT-VERSION-HERE</version>
</dependency>
```

After starting your application, 
the [OpenApi v3](https://github.com/OAI/OpenAPI-Specification/blob/3.0.1/versions/3.0.1.md) compliant
specification of your application is provided at `/v3/api-docs`. 
This specification can be viewed at `/swagger-ui` inside your browser.

Have a look at the [Demo for WebMVC](demo/openapi-generator-for-spring-demo-webmvc) 
and [Demo for WebFlux](demo/openapi-generator-for-spring-demo-webflux).

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
1. [OperationParameterPreFilter](openapi-generator-for-spring-api/src/main/java/de/qaware/openapigeneratorforspring/common/filter/operation/parameter/OperationParameterPreFilter.java)
1. [OperationParameterPostFilter](openapi-generator-for-spring-api/src/main/java/de/qaware/openapigeneratorforspring/common/filter/operation/parameter/OperationParameterPostFilter.java)
1. [OperationPostFilter](openapi-generator-for-spring-api/src/main/java/de/qaware/openapigeneratorforspring/common/filter/operation/OperationPostFilter.java)
1. [PathItemFilter](openapi-generator-for-spring-api/src/main/java/de/qaware/openapigeneratorforspring/common/filter/pathitem/PathItemFilter.java)

Insert a bean extending one or more of the above interfaces, which will be picked up by the library.

The later the filter is called, the more work has already been done, but
also more information is supplied to make the filtering decision. Note
that referenced components might be cleared later on if filtering happened
too late. It is thus recommended applying filtering as early as possible.

### How to obtain a grouped OpenAPI specification?

Grouping is realized by applying filters, preferably a `OperationPreFilter`,
while building the OpenAPI specification. Query and header parameters can be
obtained during the build phase the bean `OpenApiRequestParameterProvider`.

See this [integration test](openapi-generator-for-spring-test/src/test/java/de/qaware/openapigeneratorforspring/test/app10/App10Test.java) 
for an example.

### How to customize the specification?

TODO write about customization

## Basic design principles

This library is based on experience while using [Spring
Fox](https://github.com/springfox/springfox) and [SpringDoc
OpenApi](https://github.com/springdoc/springdoc-openapi).
As those libraries have turned out to be not flexible
enough, this library aims at being fully customizable.

TODO extend this

## Module structure

All the following module names are prefixed with `openapi-generator-for-spring-`.

**[model](openapi-generator-for-spring-model)** contains
Jackson-serializable POJOs to represent the [OpenApi v3
model](https://github.com/OAI/OpenAPI-Specification/blob/3.0.1/versions/3.0.1.md).

**[api](openapi-generator-for-spring-api)** contains the public
API to extend and adapt the functionality of this library.

**[common](openapi-generator-for-spring-common)** contains the main algorithm parsing the swagger-annotated handler methods.
 It provides default implementations for most of the interfaces in `openapi-generator-for-spring-api` module.

**[autoconfigure](openapi-generator-for-spring-autoconfigure)** contains the Spring Boot 
auto configuration of common beans.

**[web](openapi-generator-for-spring-web)** shared support code for Spring WebMVC and WebFlux
 including shared auto configuration.

**[webmvc](openapi-generator-for-spring-webmvc)** supports Spring WebMVC via Spring Boot auto configuration.

**[webflux](openapi-generator-for-spring-webflux)** supports Spring WebFlux via Spring Boot auto configuration.

**[ui](openapi-generator-for-spring-ui)** Provides Swagger UI below `/swagger-ui`. 
Also offers UI Resource transformation for WebMVC and WebFlux and sets up correct redirect routes.  

**[starter](openapi-generator-for-spring-starter)** Opinionated choice of
*dependencies. Enables Swagger UI and support for WebMVC and WebFlux if present.

**[test](openapi-generator-for-spring-test)** contains all integration tests for WebMVC and WebFlux.

Each module aims to have minimal dependencies. General library dependencies are:
* Jackson 
* Spring Core (Web/WebMVC/WebFlux are optional)
* Swagger Annotations
* WebJar for Swagger UI
* Apache Commons

