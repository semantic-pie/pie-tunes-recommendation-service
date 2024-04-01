package io.github.semanticpie.pietunes.recommendation_service.config;

import org.neo4j.driver.Driver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.neo4j.core.ReactiveDatabaseSelectionProvider;
import org.springframework.data.neo4j.core.transaction.ReactiveNeo4jTransactionManager;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

@Configuration
public class Neo4jDatabaseConfiguration {

    @Bean
    public ReactiveNeo4jTransactionManager reactiveTransactionManager(Driver driver,
                                                                      ReactiveDatabaseSelectionProvider databaseNameProvider) {
        return new ReactiveNeo4jTransactionManager(driver, databaseNameProvider);
    }

}
