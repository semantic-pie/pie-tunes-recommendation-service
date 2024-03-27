package io.github.semanticpie.pietunes.recommendation_service.services.exceptions;

public class NodeAlreadyExists extends RuntimeException {
    public NodeAlreadyExists(String message) {
        super(message);
    }
}
