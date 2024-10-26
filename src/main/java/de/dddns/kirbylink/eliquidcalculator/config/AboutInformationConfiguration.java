package de.dddns.kirbylink.eliquidcalculator.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import de.dddns.kirbylink.eliquidcalculator.utility.AboutInformation;

@Configuration
public class AboutInformationConfiguration {
  @Bean
  ClassLoader getClassLoader() {
    return AboutInformation.class.getClassLoader();
  }
}
