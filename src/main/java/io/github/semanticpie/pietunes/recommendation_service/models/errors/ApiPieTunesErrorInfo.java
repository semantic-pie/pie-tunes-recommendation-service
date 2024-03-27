package io.github.semanticpie.pietunes.recommendation_service.models.errors;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ApiPieTunesErrorInfo {
    private LocalDateTime timestamp;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private String url;
    private int status;
    private String message;

    private ApiPieTunesErrorInfo() {
        timestamp = LocalDateTime.now();
    }

    public ApiPieTunesErrorInfo(int status, String url, String message) {
        this();
        this.status = status;
        this.url = url;
        this.message = message;

    }
    // Egor: в дальнейшем можно будет добавить список subErrors
}