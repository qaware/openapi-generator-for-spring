package de.qaware.openapigeneratorforspring.common.info;


import de.qaware.openapigeneratorforspring.model.info.Info;

import java.util.function.Supplier;

/**
 * Supplier for Open Api {@link Info}. Can be provided as a bean.
 */
public interface OpenApiInfoSupplier extends Supplier<Info> {
}
