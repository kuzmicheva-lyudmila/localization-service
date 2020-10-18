package ru.otus.localization.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import ru.otus.localization.configuration.LocalizationProperties;
import ru.otus.localization.model.TranslatedLexeme;
import ru.otus.localization.repository.LexemeRepository;
import ru.otus.localization.repository.VersionRepository;
import ru.otus.localization.service.implementation.LexemeServiceImpl;
import ru.otus.model.Lexeme;

import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.when;
import static ru.otus.localization.Constants.ACTUAL_VERSION_EXAMPLE;
import static ru.otus.localization.Constants.LANGUAGE_DEFAULT;
import static ru.otus.localization.Constants.LEXEME_DETAILS_EXAMPLE;
import static ru.otus.localization.Constants.LEXEME_EXAMPLE;
import static ru.otus.localization.Constants.LEXEME_TRANSLATION_EN;
import static ru.otus.localization.Constants.LEXEME_TRANSLATION_RU;
import static ru.otus.localization.Constants.LOCALE_FR;
import static ru.otus.localization.Constants.LOCALE_RU;
import static ru.otus.localization.Constants.VERSION_EXAMPLE;
import static ru.otus.localization.Constants.VERSION_INFO_EXAMPLE;

@SpringBootTest
public class LexemeServiceTest {

    @Configuration
    static class LexemeServiceConfiguration {

        @Bean
        public LexemeService lexemeService(
                LexemeRepository lexemeRepository,
                VersionRepository versionRepository,
                LocalizationProperties localizationProperties
        ) {
            return new LexemeServiceImpl(
                    lexemeRepository,
                    versionRepository,
                    localizationProperties
            );
        }
    }

    @MockBean
    private LexemeRepository lexemeRepository;

    @MockBean
    private VersionRepository versionRepository;

    @MockBean
    private LocalizationProperties localizationProperties;

    @Autowired
    private LexemeService lexemeService;

    @BeforeEach
    public void doBeforeTest() {
        when(versionRepository.findActualVersion()).thenReturn(Mono.just(VERSION_INFO_EXAMPLE));
    }

    @Test
    public void getLexemeByIdTest() {
        when(lexemeRepository.findTranslationsByLexeme(VERSION_EXAMPLE, LEXEME_EXAMPLE))
                .thenReturn(Mono.just(LEXEME_DETAILS_EXAMPLE));

        Mono<String> expectedLexeme = lexemeService.getLexemeById(LEXEME_EXAMPLE, LOCALE_RU, VERSION_EXAMPLE);
        StepVerifier.create(expectedLexeme)
                .expectNext(LEXEME_TRANSLATION_RU)
                .expectComplete()
                .verify();
    }

    @Test
    public void getDefaultLexemeByIdTest() {
        when(lexemeRepository.findTranslationsByLexeme(VERSION_EXAMPLE, LEXEME_EXAMPLE))
                .thenReturn(Mono.just(LEXEME_DETAILS_EXAMPLE));

        when(localizationProperties.getMasterLang()).thenReturn(LANGUAGE_DEFAULT);

        Mono<String> expectedLexeme = lexemeService.getLexemeById(LEXEME_EXAMPLE, LOCALE_FR, VERSION_EXAMPLE);
        StepVerifier.create(expectedLexeme)
                .expectNext(LEXEME_TRANSLATION_EN)
                .expectComplete()
                .verify();
    }

    @Test
    public void getLexemeByIdForActualVersionTest() {
        when(lexemeRepository.findTranslationsByLexeme(ACTUAL_VERSION_EXAMPLE, LEXEME_EXAMPLE))
                .thenReturn(Mono.just(LEXEME_DETAILS_EXAMPLE));

        Mono<String> expectedLexeme = lexemeService.getLexemeById(LEXEME_EXAMPLE, LOCALE_RU, null);
        StepVerifier.create(expectedLexeme)
                .expectNext(LEXEME_TRANSLATION_RU)
                .verifyComplete();
    }

    @Test
    public void getAllLexemes() {
        List<Lexeme> lexemes = Collections.singletonList(LEXEME_DETAILS_EXAMPLE);
        when(lexemeRepository.findLexemesByVersion(VERSION_EXAMPLE)).thenReturn(Flux.fromIterable(lexemes));

        Flux<TranslatedLexeme> translatedLexemes = lexemeService.getLexemes(LOCALE_RU, VERSION_EXAMPLE);
        StepVerifier.create(translatedLexemes)
                .expectNextMatches(translatedLexeme -> translatedLexeme.getTranslation().equals(LEXEME_TRANSLATION_RU))
                .verifyComplete();
    }
}
