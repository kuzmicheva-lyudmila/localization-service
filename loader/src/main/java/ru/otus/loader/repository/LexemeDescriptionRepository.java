package ru.otus.loader.repository;

import org.springframework.data.cassandra.repository.CassandraRepository;
import ru.otus.model.LexemeDescription;

public interface LexemeDescriptionRepository extends CassandraRepository<LexemeDescription, String> {
}
