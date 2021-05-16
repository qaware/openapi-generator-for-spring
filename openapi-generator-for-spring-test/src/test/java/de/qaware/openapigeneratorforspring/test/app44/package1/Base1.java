package de.qaware.openapigeneratorforspring.test.app44.package1;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import de.qaware.openapigeneratorforspring.test.app44.App44Controller;
import de.qaware.openapigeneratorforspring.test.app44.SomeClassInUpperPackage1;
import de.qaware.openapigeneratorforspring.test.app44.SomeClassInUpperPackage2;
import de.qaware.openapigeneratorforspring.test.app44.package1.sub.SomeClassInSubPackage1;
import de.qaware.openapigeneratorforspring.test.app44.package1.sub.SomeClassInSubPackage2;

@JsonTypeInfo(use = JsonTypeInfo.Id.MINIMAL_CLASS)
@JsonSubTypes({
        @Type(value = SomeClass1.class, name = "isIgnoredForMinimalClass"),
        @Type(SomeClass2.class),
        @Type(SomeClassInSubPackage1.class),
        @Type(SomeClassInSubPackage2.class),
        @Type(SomeClassInUpperPackage1.class),
        @Type(SomeClassInUpperPackage2.class),
        @Type(de.qaware.openapigeneratorforspring.test.app44.package2.SomeClass1.class),
        @Type(de.qaware.openapigeneratorforspring.test.app44.package2.SomeClass2.class),
        @Type(App44Controller.InnerClass1.class),
})
public interface Base1 {

}
