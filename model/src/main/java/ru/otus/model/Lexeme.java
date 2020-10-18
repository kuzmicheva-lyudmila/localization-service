package ru.otus.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

import java.util.Map;

@Getter
@Setter
@Table
@AllArgsConstructor
@NoArgsConstructor
public class Lexeme {

    @PrimaryKeyColumn (
            value = "version",
            type = PrimaryKeyType.PARTITIONED,
            ordinal = 0
    )
    private Long version;

    @PrimaryKeyColumn (
            value = "id",
            ordinal = 1
    )
    private String id;

    private Map<String, String> translations;
}
