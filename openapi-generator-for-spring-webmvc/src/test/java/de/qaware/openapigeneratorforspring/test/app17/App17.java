package de.qaware.openapigeneratorforspring.test.app17;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class App17 {
    public static void main(String[] args) {
        SpringApplication.run(App17.class, args);
    }
//
//    @Bean
//    public ReferenceDeciderForSchema referenceDeciderForSchema() {
//        return new ReferenceDeciderForSchema() {
//            @Override
//            public boolean turnIntoReference(Schema item, long numberOfUsages) {
//                return false;
//            }
//        };
//    }
}
