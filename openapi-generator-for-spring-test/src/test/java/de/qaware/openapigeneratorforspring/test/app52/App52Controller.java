package de.qaware.openapigeneratorforspring.test.app52;

import lombok.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping
class App52Controller {

    @GetMapping("mapping1")
    public DtoWithMap mapping1() {
        return null;
    }


    @GetMapping("mapping2")
    public Map<String, Object> mapping2() {
        return null;
    }

    @Value
    private static class DtoWithMap {
        Map<String, String> innerStringStringMap;
        Map<String, InnerDto> innerDtoMap;
    }

    @Value
    private static class InnerDto {
        String property1;
        int property2;
    }
}
