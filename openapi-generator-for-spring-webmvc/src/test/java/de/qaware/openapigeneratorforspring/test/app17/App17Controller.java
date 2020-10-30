package de.qaware.openapigeneratorforspring.test.app17;

import lombok.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Set;

@RestController
public class App17Controller {

    @GetMapping("/mapping1")
    public List<SomeDto> mapping1(@RequestBody @Nullable SomeDto someDto) {
        return null;
    }

//    @GetMapping("/mapping2")
//    public SomeDto mapping2(@RequestBody List<SomeDto> someDto) {
//        return null;
//    }
//
//    @GetMapping("/mapping3")
//    public SomeDto mapping3() {
//        return null;
//    }

    @Value
    private static class SomeDto {
        //        String stringProperty1;
        @Nullable
        SomeDto someDtoProperty;
        List<SomeDto> listOfSomeDtos;
        //        @Nullable
//        List<SomeDto> nullableListOfSomeDtos;
//        Set<List<SomeDto>> setOfListOfSomeDtos;
        @Nullable
        Set<List<SomeDto>> nullableSetOfListOfSomeDtos;
    }
}
