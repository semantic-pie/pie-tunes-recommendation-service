package io.github.semanticpie.pietunes.recommendation_service.services.impl;

import io.github.semanticpie.pietunes.recommendation_service.models.neo4jDomain.UserNeo4j;
import io.github.semanticpie.pietunes.recommendation_service.repositories.UserNeo4jRepository;
import io.github.semanticpie.pietunes.recommendation_service.services.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Slf4j
@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserNeo4jRepository userRepository;
    @Override
    public Mono<UserNeo4j> findUserById(UUID id) {
        return userRepository.findById(id);
    }
}
