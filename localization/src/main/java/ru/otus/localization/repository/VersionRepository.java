package ru.otus.localization.repository;

import org.springframework.data.cassandra.repository.Query;
import org.springframework.data.cassandra.repository.ReactiveCassandraRepository;
import reactor.core.publisher.Mono;
import ru.otus.model.Version;

public interface VersionRepository extends ReactiveCassandraRepository<Version, Long> {

    @Query("select value from version where id = 'localization'")
    Mono<Version> findActualVersion();
}
