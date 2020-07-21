package ru.otus.loader.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class LexemeTranslation {

    private String lexeme;

    private Long version;

    private List<Translation> translations;
}
