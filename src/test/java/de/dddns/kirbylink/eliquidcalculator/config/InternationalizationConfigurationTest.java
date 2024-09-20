package de.dddns.kirbylink.eliquidcalculator.config;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import java.util.Locale;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class InternationalizationConfigurationTest {

  private Locale originalDefaultLocale;
  private InternationalizationConfiguration internationalizationConfiguration;

  @BeforeEach
  void setUp() {
    originalDefaultLocale = Locale.getDefault();
  }

  @AfterEach
  void tearDown() {
    Locale.setDefault(originalDefaultLocale);
  }

  @Test
  void testMessageSource() {

    // Given
    Locale.setDefault(Locale.ENGLISH);
    internationalizationConfiguration = new InternationalizationConfiguration();
    var expectedMessage = "Die Anzahl der Dezimalstellen muss größer oder gleich 0 sein";

    // When
    var messageSource = internationalizationConfiguration.messageSource();
    var actualMessage = messageSource.getMessage("utility.round.errormessage", null, Locale.GERMAN);

    // Then
    assertThat(actualMessage).isEqualTo(expectedMessage);
  }
}
