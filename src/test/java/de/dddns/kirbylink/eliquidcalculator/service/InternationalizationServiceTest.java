package de.dddns.kirbylink.eliquidcalculator.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;
import java.util.Locale;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;
import de.dddns.kirbylink.eliquidcalculator.config.InternationalizationConfiguration;
import de.dddns.kirbylink.eliquidcalculator.model.ResultVolumeWeightPercentage;

@ExtendWith(MockitoExtension.class)
class InternationalizationServiceTest {

  @Mock
  MessageSource messageSource;
  @Mock
  InternationalizationConfiguration internationalizationConfiguration;
  @InjectMocks
  InternationalizationService internationalizationService;

  private Locale originalDefaultLocale;

  @BeforeEach
  void setUp() {
    originalDefaultLocale = Locale.getDefault();
    Locale.setDefault(Locale.GERMAN);
  }

  @AfterEach
  void tearDown() {
    Locale.setDefault(originalDefaultLocale);
  }

  @Test
  void testGetMessage() {

    // Given
    var expectedMessage = "Decimal place must greater or equal than 0";
    when(internationalizationConfiguration.getLocale()).thenReturn(Locale.GERMAN);
    when(messageSource.getMessage("utility.round.errormessage", null, Locale.GERMAN)).thenReturn(expectedMessage);

    // When
    var actualMessage = internationalizationService.getMessage("utility.round.errormessage");

    // Then
    assertThat(actualMessage).isEqualTo(expectedMessage);
  }

  @Test
  void testELiquidResultConsoleOutput() {

    // Given
    var expectedMessage = """
        Benötigte Menge
        -----------------------------------------------------------
        \t\tVolumen\t\tGewicht\t\tProzent
        Basisliquid:\t100,00 ml\t104,00 g\t12,50%
        PG:\t\t300,00 ml\t311,00 g\t37,50%
        VG:\t\t400,00 ml\t504,00 g\t50,00%
        Wasser:\t000,00 ml\t000,00 g\t00,00%
        -----------------------------------------------------------""";
    var messageFromProperties = "Benötigte Menge\n-----------------------------------------------------------\n\t\tVolumen\t\tGewicht\t\tProzent\nBasisliquid:\t%06.2f ml\t%06.2f g\t%05.2f%%\nPG:\t\t%06.2f ml\t%06.2f g\t%05.2f%%\nVG:\t\t%06.2f ml\t%06.2f g\t%05.2f%%\nWasser:\t%06.2f ml\t%06.2f g\t%05.2f%%\n-----------------------------------------------------------";
    var eLiquidResultConsoleOutput = ResultVolumeWeightPercentage.builder()
        .withBaseLiquidVolume(100)
        .withBaseLiquidWeight(104)
        .withBaseLiquidPercentage(12.5)
        .withPgVolume(300)
        .withPgWeight(311)
        .withPgPercentage(37.5)
        .withVgVolume(400)
        .withVgWeight(504)
        .withVgPercentage(50)
        .withWaterVolume(0)
        .withWaterWeight(0)
        .withWaterPercentage(0)
        .build();
    when(internationalizationConfiguration.getLocale()).thenReturn(Locale.GERMAN);
    when(messageSource.getMessage("eliquid.result.console.output", null, Locale.GERMAN)).thenReturn(messageFromProperties);

    // When
    var actualMessage = internationalizationService.requiredQuantityOutput(eLiquidResultConsoleOutput);

    // Then
    assertThat(actualMessage).isEqualTo(expectedMessage);
  }

  @Test
  void testFiveDigitNumberOutput() {

    // Given
    var expectedMessage = "42,50";
    var messageFromProperties = "%05.2f";
    var pgPercent = 42.5;
    when(internationalizationConfiguration.getLocale()).thenReturn(Locale.GERMAN);
    when(messageSource.getMessage("eliquid.result.gui.output.five.digits", null, Locale.GERMAN)).thenReturn(messageFromProperties);

    // When
    var actualMessage = internationalizationService.fiveDigitNumberOutput(pgPercent);

    // Then
    assertThat(actualMessage).isEqualTo(expectedMessage);
  }
}
