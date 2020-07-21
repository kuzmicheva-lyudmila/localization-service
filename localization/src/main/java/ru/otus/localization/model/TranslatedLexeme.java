package ru.otus.localization.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class TranslatedLexeme {

    private final String name;

    private final String translation;
}
