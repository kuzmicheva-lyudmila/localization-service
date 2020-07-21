package ru.otus.loader.configuration;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import ru.otus.loader.model.LexemeTranslation;
import ru.otus.model.LexemeDescription;

import java.util.List;

@Component
@ConfigurationProperties("localization")
@Getter
@Setter
@ToString
public class TranslationProperties {

    private Long version;

    private List<LexemeDescription> lexemes;

    private List<LexemeTranslation> lexemesTranslations;
}
