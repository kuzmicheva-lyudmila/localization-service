package ru.otus.loader.job;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;
import ru.otus.loader.configuration.TranslationProperties;
import ru.otus.loader.model.LexemeTranslation;
import ru.otus.loader.model.Translation;
import ru.otus.loader.repository.LexemeDescriptionRepository;
import ru.otus.loader.repository.LexemeRepository;
import ru.otus.loader.repository.VersionRepository;
import ru.otus.model.Lexeme;
import ru.otus.model.LexemeDescription;
import ru.otus.model.Version;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class LoaderTasklet implements Tasklet, StepExecutionListener {

    private static final String SERVICE_ID = "localization";

    private final TranslationProperties translationProperties;

    private final VersionRepository versionRepository;

    private final LexemeDescriptionRepository lexemeDescriptionRepository;

    private final LexemeRepository lexemeRepository;

    @Override
    public void beforeStep(StepExecution stepExecution) {
        log.info("LoaderTasklet initialized.");
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        log.info("LoaderTasklet ended.");
        return ExitStatus.COMPLETED;
    }

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        Long actualVersion = versionRepository.findActualVersion().getValue();
        Long newVersion = translationProperties.getVersion();

        if (newVersion != null && newVersion > actualVersion || actualVersion == null) {
            saveLexemeDescriptions();
            saveLexemeTranslations(newVersion);
            updateVersion(newVersion);
        }

        return RepeatStatus.FINISHED;
    }

    private void saveLexemeDescriptions() {
        List<LexemeDescription> lexemeDescriptions = translationProperties.getLexemes();

        if (lexemeDescriptions != null && lexemeDescriptions.size() > 0) {
            lexemeDescriptionRepository.saveAll(lexemeDescriptions);
        }
    }

    private void saveLexemeTranslations(Long version) {
        List<LexemeTranslation> translations = translationProperties.getLexemesTranslations();

        if (translations != null && translations.size() > 0) {
            List<Lexeme> lexemes = translations.stream()
                    .map(lexemeTranslation -> getLexeme(version, lexemeTranslation))
                    .collect(Collectors.toList());

            lexemeRepository.saveAll(lexemes);
        }
    }

    private Lexeme getLexeme(Long version, LexemeTranslation lexemeTranslation) {
        Lexeme lexeme = new Lexeme();

        lexeme.setVersion(version);
        lexeme.setId(lexemeTranslation.getLexeme());
        lexeme.setTranslations(getTranslationMap(lexemeTranslation));

        return lexeme;
    }

    private Map<String, String> getTranslationMap(LexemeTranslation lexemeTranslation) {
        return lexemeTranslation.getTranslations()
                    .stream()
                    .collect(Collectors.toMap(Translation::getLang, Translation::getDescribe));
    }

    private void updateVersion(Long version) {
        versionRepository.save(new Version(SERVICE_ID, version));
    }
}
