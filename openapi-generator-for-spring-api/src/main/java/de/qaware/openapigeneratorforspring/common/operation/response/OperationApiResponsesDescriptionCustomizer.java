package de.qaware.openapigeneratorforspring.common.operation.response;

@SuppressWarnings("squid:S1214") // suppress warning about constant in interface
public interface OperationApiResponsesDescriptionCustomizer extends OperationApiResponsesCustomizer {
    int ORDER = OperationApiResponsesCustomizer.DEFAULT_ORDER + 1000; // make sure we run pretty late!

    @Override
    default int getOrder() {
        return ORDER;
    }
}
