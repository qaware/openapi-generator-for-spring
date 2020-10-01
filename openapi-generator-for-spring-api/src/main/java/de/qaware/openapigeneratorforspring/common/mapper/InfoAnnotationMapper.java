package de.qaware.openapigeneratorforspring.common.mapper;


import de.qaware.openapigeneratorforspring.model.info.Info;

import javax.annotation.Nullable;

public interface InfoAnnotationMapper {
    @Nullable
    Info map(io.swagger.v3.oas.annotations.info.Info infoAnnotation);
}
