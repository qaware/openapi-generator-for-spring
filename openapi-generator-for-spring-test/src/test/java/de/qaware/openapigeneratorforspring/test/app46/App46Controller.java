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

    private static final String AUDIO_WAV = "audio/wav";
    private static final String IMAGE_PNG = "image/png";

    private byte[] audio = new byte[0];
    private byte[] image = new byte[0];

    @PostMapping(consumes = AUDIO_WAV)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void uploadAudio(InputStream requestBody) throws IOException {
        audio = IOUtils.toByteArray(requestBody);
    }

    @PostMapping(consumes = IMAGE_PNG)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void uploadImage(InputStream requestBody) throws IOException {
        image = IOUtils.toByteArray(requestBody);
    }


    @GetMapping(produces = AUDIO_WAV)
    public ResponseEntity<InputStreamResource> downloadAudio() {
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(AUDIO_WAV))
                .body(new InputStreamResource(new ByteArrayInputStream(audio)));
    }

    @GetMapping(produces = IMAGE_PNG)
    public ResponseEntity<byte[]> downloadImage() {
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(IMAGE_PNG))
                .body(image);
    }

}
