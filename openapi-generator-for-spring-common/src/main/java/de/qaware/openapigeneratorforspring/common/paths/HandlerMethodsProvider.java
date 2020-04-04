package de.qaware.openapigeneratorforspring.common.paths;

import java.util.List;

public interface HandlerMethodsProvider {

    List<HandlerMethodWithInfo> getHandlerMethods();

}
