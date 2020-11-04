package de.qaware.openapigeneratorforspring.common.operation.response;

import de.qaware.openapigeneratorforspring.common.paths.HandlerMethod;

public interface ApiResponseCodeMapper {
    String map(HandlerMethod operationInfo);
}
