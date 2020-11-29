package de.qaware.openapigeneratorforspring.test.app33;

import lombok.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@RestController
public class App33Controller {

    @GetMapping(value = "/mapping1")
    public SomeDto mapping1() {
        return null;
    }

    @GetMapping(value = "/mapping2/{pathParam}")
    public SomeReturnEnum mapping2(@PathVariable("pathParam") SomeParameterEnum parameter1) {
        return null;
    }

    @Value
    static class SomeDto {
        @Min(0)
        int intValue1WithMin;
        @DecimalMin("-1")
        long longValue1WithDecimalMin;
        @Max(100)
        int intValue1WithMax;
        @DecimalMax("10000")
        long longValue1WithDecimalMax;
    }

    enum SomeReturnEnum {
        VALUE_1,
        VALUE_2
    }

    enum SomeParameterEnum {
        VALUE_3,
        VALUE_4
    }
}
