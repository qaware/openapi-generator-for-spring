package de.qaware.openapigeneratorforspring.test.app44;

import de.qaware.openapigeneratorforspring.test.app44.package1.Base1;
import de.qaware.openapigeneratorforspring.test.app44.package1.SomeClass1;
import de.qaware.openapigeneratorforspring.test.app44.package1.SomeClass2;
import de.qaware.openapigeneratorforspring.test.app44.package1.sub.SomeClassInSubPackage1;
import de.qaware.openapigeneratorforspring.test.app44.package1.sub.SomeClassInSubPackage2;
import lombok.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
public class App44Controller {

    @GetMapping(path = "/")
    public List<Base1> mapping1() {
        return Arrays.asList(
                new SomeClass1("property1"),
                new SomeClass2("property7"),
                new SomeClassInSubPackage1("property4"),
                new SomeClassInSubPackage2("property5"),
                new SomeClassInUpperPackage1("property3"),
                new SomeClassInUpperPackage2("property8"),
                new de.qaware.openapigeneratorforspring.test.app44.package2.SomeClass1("property2"),
                new de.qaware.openapigeneratorforspring.test.app44.package2.SomeClass2("property6"),
                new InnerClass1("property9")
        );
    }

    @Value
    public static class InnerClass1 implements Base1 {
        String property9;
    }
}
