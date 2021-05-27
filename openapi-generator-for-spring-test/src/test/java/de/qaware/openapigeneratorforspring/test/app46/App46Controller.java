package de.qaware.openapigeneratorforspring.test.app46;

import org.apache.commons.io.IOUtils;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

@RestController
class App46Controller {

    private byte[] audio = new byte[0];

    @PostMapping(consumes = "audio/wav")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void upload(InputStream requestBody) throws IOException {
        audio = IOUtils.toByteArray(requestBody);
    }

    @GetMapping(produces = "audio/wav")
    public ResponseEntity<InputStreamResource> download() {
        return ResponseEntity.ok()
                .contentType(new MediaType("audio", "wav"))
                .body(new InputStreamResource(new ByteArrayInputStream(audio)));
    }

}
