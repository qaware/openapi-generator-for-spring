package de.qaware.openapigeneratorforspring.test.app50;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;
import java.util.Locale;

@RestController
@RequestMapping
class App50Controller {

    @GetMapping("/users/{id}")
    public List<UserId> getUsers(
            @PathVariable("id") UserId userId,
            @RequestHeader(name = "X-Custom-Header") HeaderDto header
    ) {
        return Collections.singletonList(userId);
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserId>> getWithAcceptHeader(
            @Parameter(in = ParameterIn.HEADER, name = "Accept-Language") Locale locale,
            @RequestParam(value = "user", defaultValue = "") List<UserId> userIds
    ) {
        return ResponseEntity.ok(userIds);
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
