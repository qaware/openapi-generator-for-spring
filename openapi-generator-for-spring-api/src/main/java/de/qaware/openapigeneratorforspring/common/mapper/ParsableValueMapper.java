package de.qaware.openapigeneratorforspring.common.mapper;

@FunctionalInterface
public interface ParsableValueMapper {
    Object parse(String value);
}
