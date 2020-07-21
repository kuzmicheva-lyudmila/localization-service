package ru.otus.loader.repository;

import org.springframework.data.cassandra.repository.CassandraRepository;
import ru.otus.model.Lexeme;

public interface LexemeRepository extends CassandraRepository<Lexeme, Long> {
}
