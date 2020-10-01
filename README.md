# OpenAPI v3 generator for Spring Boot

This project is a library for conveniently generating
OpenApi v3 specifications from Spring controllers.

## Module structure

`openapi-generator-for-spring-model` contains
Jackson-serializable POJOs to represent the [OpenApi v3
model](https://github.com/OAI/OpenAPI-Specification/blob/3.0.1/versions/3.0.1.md).

`openapi-generator-for-spring-api` contains the "public"
API to extend and adapt the functionality of this library.

`openapi-generator-for-spring-common` contains shared code to support
Spring WebMVC and WebFlux. It provides default implementations
for most of the `openapi-generator-for-spring-api` module.

`openapi-generator-for-spring-autoconfigure` contains shared
code for WebMVC and WebFlux for Spring Boot auto configuration.

`openapi-generator-for-spring-webmvc` supports
Spring WebMVC via Spring Boot auto configuration.

`openapi-generator-for-spring-webflux` supports
Spring WebFlux via Spring Boot auto configuration.
