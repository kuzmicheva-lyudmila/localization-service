package ru.otus.localization.configuration;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("localization")
@Getter
@Setter
@ToString
public class LocalizationProperties {

    private String masterLang;
}
