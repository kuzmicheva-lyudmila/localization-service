package ru.otus.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

@Getter
@Setter
@ToString
@Table(value = "lexeme_description")
@AllArgsConstructor
@NoArgsConstructor
public class LexemeDescription {

    @PrimaryKey
    private String id;

    private String description;
}
