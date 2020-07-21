package ru.otus.loader.repository;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;
import ru.otus.model.Version;

public interface VersionRepository extends CassandraRepository<Version, Long> {

    @Query("select value from version where id = 'localization'")
    Version findActualVersion();
}
