package de.qaware.openapigeneratorforspring.test.app50;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping
class App50Controller {

    @GetMapping("/users/{id}")
    public List<String> getUsers(
            @PathVariable("id") UserId userId,
            @RequestHeader(name = "X-Custom-Header") HeaderDto header
    ) {
        return Collections.emptyList();
    }

    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    @Getter
    static class UserId {
        private final String id;

        public static UserId of(String id) {
            return new UserId(id);
        }
    }

    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    @Getter
    static class HeaderDto {
        private final String param1;
        private final String param2;

        public static HeaderDto fromParams(String param1, String param2) {
            return new HeaderDto(param1, param2);
        }
    }
}
