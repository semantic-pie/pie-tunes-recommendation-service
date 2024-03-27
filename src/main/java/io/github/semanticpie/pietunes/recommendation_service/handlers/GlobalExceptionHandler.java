package io.github.semanticpie.pietunes.recommendation_service.handlers;


import io.github.semanticpie.pietunes.recommendation_service.models.errors.ApiPieTunesErrorInfo;
import io.github.semanticpie.pietunes.recommendation_service.services.exceptions.NodeAlreadyExists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Slf4j
@Order(-2)
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    @ExceptionHandler(NodeAlreadyExists.class)
    public Mono<ApiPieTunesErrorInfo> handleTrackNotFound(ServerWebExchange exchange, Exception ex) {
        log.warn(ex.getMessage());
        return Mono.just(new ApiPieTunesErrorInfo(HttpStatus.NOT_FOUND.value(),
                exchange.getRequest().getPath().toString(), ex.getMessage()));
    }
}