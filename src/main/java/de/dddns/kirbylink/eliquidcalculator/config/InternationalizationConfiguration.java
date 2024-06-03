package de.dddns.kirbylink.eliquidcalculator.config;

import java.util.Locale;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import lombok.Getter;
import lombok.Setter;

@Configuration
public class InternationalizationConfiguration {

  @Getter
  @Setter
  private Locale locale = Locale.getDefault();

  @Bean
  MessageSource messageSource() {
    var messageSource = new ReloadableResourceBundleMessageSource();
    messageSource.setBasenames("classpath:messages/messages");
    messageSource.setDefaultEncoding("UTF-8");
    messageSource.setDefaultLocale(Locale.ENGLISH);
    return messageSource;
  }

  @Bean
  LocalValidatorFactoryBean validator(MessageSource messageSource) {
    var localValidatorFactoryBean = new LocalValidatorFactoryBean();
    localValidatorFactoryBean.setValidationMessageSource(messageSource);
    return localValidatorFactoryBean;
  }
}
