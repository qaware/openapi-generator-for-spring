package de.qaware.openapigeneratorforspring.test.app2;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "simple")
public class App2SimpleController {

    @GetMapping("get-mapping")
    public String getMappingReturnString() {
        return null;
    }

    @PostMapping("post-mapping")
    @ResponseStatus(HttpStatus.CREATED)
    public void postMapping() {
    }

    @PutMapping("put-mapping")
    public void putMapping() {
    }

    @DeleteMapping("delete-mapping")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteMapping() {
    }
}

