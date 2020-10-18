package ru.otus.localization.service.implementation;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.localization.configuration.LocalizationProperties;
import ru.otus.localization.model.TranslatedLexeme;
import ru.otus.localization.repository.LexemeRepository;
import ru.otus.localization.repository.VersionRepository;
import ru.otus.localization.service.LexemeService;
import ru.otus.model.Lexeme;
import ru.otus.model.Version;

import java.util.Map;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class LexemeServiceImpl implements LexemeService {

    private static final String TRANSLATIONS_DONT_FOUND_MSG =
            "Lexeme translations don't found in repository";
    private static final String TRANSLATION_BY_LANG_DONT_FOUND_MSG =
            "Translation don't found in repository for lang = %s and master lang = %s";

    private final LexemeRepository lexemeRepository;

    private final VersionRepository versionRepository;

    private final LocalizationProperties localizationProperties;

    @Override
    public Mono<String> getLexemeById(String id, String lang, Long version) {
        return Mono.justOrEmpty(version)
                .switchIfEmpty(getActualVersion())
                .flatMap(findTranslation(id, lang));
    }

    @Override
    public Flux<TranslatedLexeme> getLexemes(String lang, Long version) {
        return Mono.justOrEmpty(version)
                .switchIfEmpty(getActualVersion())
                .flatMapMany(findLexemesWithTranslation(lang));
    }

    private Mono<Long> getActualVersion() {
        return versionRepository.findActualVersion()
                .map(Version::getValue);
    }

    private Function<Long, Mono<String>> findTranslation(String id, String lang) {
        return version -> lexemeRepository.findTranslationsByLexeme(version, id)
                .switchIfEmpty(
                        Mono.error(new IllegalArgumentException(TRANSLATIONS_DONT_FOUND_MSG))
                )
                .map(lexeme -> getTranslationWithDefault(lexeme, lang))
                .switchIfEmpty(
                        Mono.error(
                                new IllegalArgumentException(
                                        String.format(TRANSLATION_BY_LANG_DONT_FOUND_MSG, lang, localizationProperties.getMasterLang())
                                )
                        )
                );
    }

    private Function<Long, Flux<TranslatedLexeme>> findLexemesWithTranslation(String lang) {
        return version -> lexemeRepository.findLexemesByVersion(version)
                .map(lexeme -> new TranslatedLexeme(lexeme.getId(), getTranslationWithDefault(lexeme, lang)))
                .filter(translatedLexeme -> !translatedLexeme.getTranslation().isEmpty())
                .switchIfEmpty(
                        Flux.error(new IllegalArgumentException(
                                String.format(TRANSLATION_BY_LANG_DONT_FOUND_MSG, lang, localizationProperties.getMasterLang()))
                        )
                );
    }

    private String getTranslationWithDefault(Lexeme lexeme, String lang) {
        Map<String, String> translations = lexeme.getTranslations();
        return translations.get(lang) == null
                ? translations.get(localizationProperties.getMasterLang())
                : translations.get(lang);
    }
}
