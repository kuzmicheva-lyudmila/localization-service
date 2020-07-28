package ru.otus.localization.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.localization.Constants;
import ru.otus.localization.model.TranslatedLexeme;
import ru.otus.localization.service.LexemeService;

import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@WebFluxTest
public class ControllerTest {

    @Autowired
    private WebTestClient webClient;

    @MockBean
    LexemeService lexemeService;

    @Test
    public void getAllLexemesWithTranslationTest() {
        TranslatedLexeme translatedLexeme = new TranslatedLexeme(Constants.LEXEME_EXAMPLE, Constants.LEXEME_EXAMPLE);
        List<TranslatedLexeme> translatedLexemes = Collections.singletonList(translatedLexeme);
        Flux<TranslatedLexeme> lexemeFlux = Flux.fromIterable(translatedLexemes);

        when(lexemeService.getLexemes(Constants.LOCALE_RU, Constants.VERSION_EXAMPLE)).thenReturn(lexemeFlux);

        webClient.get()
                .uri(
                    uriBuilder ->
                            uriBuilder.path("/lexeme/all")
                                    .queryParam("locale", Constants.LOCALE_RU)
                                    .queryParam("version", Constants.VERSION_EXAMPLE)
                                    .build()
                )
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$[0]['name']").isEqualTo(Constants.LEXEME_EXAMPLE)
                .jsonPath("$[0]['translation']").isEqualTo(Constants.LEXEME_TRANSLATION_EXAMPLE);

        verify(lexemeService).getLexemes(Constants.LOCALE_RU, Constants.VERSION_EXAMPLE);
    }

    @Test
    public void getLexemeTranslationByIdTest() {
        when(lexemeService.getLexemeById(Constants.LEXEME_EXAMPLE, Constants.LOCALE_RU, Constants.VERSION_EXAMPLE))
                .thenReturn(Mono.just(Constants.LEXEME_TRANSLATION_EXAMPLE));

        webClient.get()
                .uri(
                        uriBuilder ->
                                uriBuilder.path("/lexeme/{id}")
                                        .queryParam("locale", Constants.LOCALE_RU)
                                        .queryParam("version", Constants.VERSION_EXAMPLE)
                                        .build(Constants.LEXEME_EXAMPLE)
                )
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class).isEqualTo(Constants.LEXEME_TRANSLATION_EXAMPLE);

        verify(lexemeService).getLexemeById(Constants.LEXEME_EXAMPLE, Constants.LOCALE_RU, Constants.VERSION_EXAMPLE);
    }
}
