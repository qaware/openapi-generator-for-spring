package de.qaware.openapigeneratorforspring.common.schema.resolver.type;

import de.qaware.openapigeneratorforspring.common.schema.resolver.type.initial.InitialSchema;

public interface TypeResolverSupport {
    boolean supports(InitialSchema initialSchema);
}
