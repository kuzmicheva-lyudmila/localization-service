package ru.otus.localization;

import ru.otus.model.Lexeme;
import ru.otus.model.Version;

import java.util.Map;

public class Constants {

    public static final String LEXEME_EXAMPLE = "test";

    public static final String LEXEME_TRANSLATION_EXAMPLE = "test";

    public static final String LOCALE_RU = "ru";

    public static final String LOCALE_EN = "en";

    public static final String LOCALE_FR = "fr";

    public static final String LANGUAGE_DEFAULT = "en";

    public static final String LEXEME_TRANSLATION_RU = "тест";

    public static final String LEXEME_TRANSLATION_EN = "test";

    public static final Long VERSION_EXAMPLE = 1L;

    public static final Long ACTUAL_VERSION_EXAMPLE = 2L;

    public static final Version VERSION_INFO_EXAMPLE = new Version("", ACTUAL_VERSION_EXAMPLE);

    public static final Lexeme LEXEME_DETAILS_EXAMPLE = new Lexeme(
            VERSION_EXAMPLE,
            LEXEME_EXAMPLE,
            Map.of(LOCALE_EN, LEXEME_TRANSLATION_EN, LOCALE_RU, LEXEME_TRANSLATION_RU)
    );

    public static final Lexeme LEXEME_DETAILS_FOR_ACTUAL_VERSION_EXAMPLE = new Lexeme(
            VERSION_EXAMPLE,
            LEXEME_EXAMPLE,
            Map.of(LOCALE_EN, LEXEME_TRANSLATION_EN, LOCALE_RU, LEXEME_TRANSLATION_RU)
    );
}
