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
specification of your endpoints is provided at `/v3/api-docs`. This specification can be viewed at `/swagger-ui`.

## Configuration Properties

The following configuration properties are available with the prefix `openapi-generator-for-spring.`:

* [Open API Properties](openapi-generator-for-spring-common/src/main/java/de/qaware/openapigeneratorforspring/common/OpenApiConfigurationProperties.java)
* [Open API Info Properties](openapi-generator-for-spring-common/src/main/java/de/qaware/openapigeneratorforspring/common/info/OpenApiInfoConfigurationProperties.java)
* [Open API Swagger UI Properties](openapi-generator-for-spring-ui/src/main/java/de/qaware/openapigeneratorforspring/ui/OpenApiSwaggerUiConfigurationProperties.java)

The following extension properties are currently part of this library:
* [Java8 Time](openapi-generator-for-spring-common/src/main/java/de/qaware/openapigeneratorforspring/common/schema/resolver/type/extension/java8/Java8TimeTypeResolverConfigurationProperties.java)

## How To

### Obtain "grouped" OpenAPI specification

Grouping is realized by applying filters, preferably a `OperationPreFilter`,
while building the OpenAPI specification. Query and header parameters can be
obtained during the build phase the bean `OpenApiRequestParameterProvider`.

See this [integration test](openapi-generator-for-spring-test/src/test/java/de/qaware/openapigeneratorforspring/test/app10/App10Test.java) 
for an example.

## Basic design principles

This library is based on experience while using [Spring
Fox](https://github.com/springfox/springfox) and [SpringDoc
OpenApi](https://github.com/springdoc/springdoc-openapi).
As those libraries have turned out to be not flexible
enough, this library aims at being fully customizable.



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

