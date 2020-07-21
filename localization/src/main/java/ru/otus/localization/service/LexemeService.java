package ru.otus.localization.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.localization.model.TranslatedLexeme;

public interface LexemeService {

    Mono<String> getLexemeById(String id, String lang, Long version);
    Flux<TranslatedLexeme> getLexemes(String lang, Long version);
}
