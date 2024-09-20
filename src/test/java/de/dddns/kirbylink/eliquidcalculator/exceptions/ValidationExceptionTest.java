package de.dddns.kirbylink.eliquidcalculator.exceptions;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import java.util.Locale;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ValidationExceptionTest {

  private Locale originalDefaultLocale;

  @BeforeEach
  void setUp() {
    originalDefaultLocale = Locale.getDefault();
  }

  @AfterEach
  void tearDown() {
    Locale.setDefault(originalDefaultLocale);
  }

  @Test
  void testValidationException_WhenMessageKey_ThenMessageGermanWillBeUsed() {

    // Given
    Locale.setDefault(Locale.GERMAN);
    var expectedMessage = "Die Anzahl der Dezimalstellen muss größer oder gleich 0 sein";

    // When
    var validationException = new ValidationException("utility.round.errormessage");

    // Then
    assertThat(validationException).hasMessage(expectedMessage);
  }

  @Test
  void testValidationException_WhenMessageKeyAndPercentages_ThenMessageWillBeFormatted() {

    // Given
    Locale.setDefault(Locale.GERMAN);
    var expectedMessage = "Die Summe aller Prozente muss genau 1,0 ergeben, wenn die Basis gesetzt ist.\nAktuelle Werte: PG = 0,50, VG = 0,50, Wasser = 0,50";

    // When
    var validationException = new ValidationException("eliquid.base.build.errormessage", 0.5, 0.5, 0.5);

    // Then
    assertThat(validationException).hasMessage(expectedMessage);
  }

  @Test
  void testGetMessage() {
    // Given
    Locale.setDefault(Locale.ENGLISH);
    var expectedMessage = "Decimal places must be greater than or equal to 0";

    // When
    var validationException = new ValidationException("utility.round.errormessage");
    var actualMessage = validationException.getMessage();

    // Then
    assertThat(actualMessage).isEqualTo(expectedMessage);
  }

  @Test
  void testValidationException_WhenMessageKeyAndLocal_ThenEnglishMessageWillBeUsed() {
    Locale.setDefault(Locale.ENGLISH);
    // Given
    var expectedMessage = "Decimal places must be greater than or equal to 0";

    // When
    var validationException = new ValidationException("utility.round.errormessage", null, null, null);

    // Then
    assertThat(validationException).hasMessage(expectedMessage);
  }

}
