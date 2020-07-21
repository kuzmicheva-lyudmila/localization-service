package ru.otus.localization.repository;

import org.springframework.data.cassandra.repository.Query;
import org.springframework.data.cassandra.repository.ReactiveCassandraRepository;
import org.springframework.data.repository.query.Param;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.model.Lexeme;

public interface LexemeRepository extends ReactiveCassandraRepository<Lexeme, Long> {

    @Query("select * from lexeme where version = :version and id = :id")
    Mono<Lexeme> findTranslationsByLexeme(@Param("version") Long version, @Param("id") String id);

    @Query("select * from lexeme where version = :version")
    Flux<Lexeme> findLexemesByVersion(@Param("version") Long version);
}
