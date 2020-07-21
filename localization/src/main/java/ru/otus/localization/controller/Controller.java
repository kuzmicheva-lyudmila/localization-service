package ru.otus.localization.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.localization.model.TranslatedLexeme;
import ru.otus.localization.service.LexemeService;

import java.util.Locale;

@RestController
@RequestMapping("/lexeme")
@RequiredArgsConstructor
public class Controller {

    private final LexemeService lexemeService;

    @GetMapping("/byId/{id}")
    private Mono<String> getLexemeById(
            @PathVariable String id,
            @RequestParam Locale locale,
            @RequestParam(required = false) Long version
    ) {
        return lexemeService.getLexemeById(id, locale.getLanguage(), version);
    }

    @GetMapping("/all")
    private Flux<TranslatedLexeme> getLexemes (
            @RequestParam Locale locale,
            @RequestParam(required = false) Long version
    ) {
        return lexemeService.getLexemes(locale.getLanguage(), version);
    }
}
