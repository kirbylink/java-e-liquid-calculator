package de.dddns.kirbylink.eliquidcalculator.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import de.dddns.kirbylink.eliquidcalculator.calculator.Calculator;
import de.dddns.kirbylink.eliquidcalculator.config.CommandLineConfiguration;
import de.dddns.kirbylink.eliquidcalculator.converter.ResultVolumeWeightPercentageMapper;
import de.dddns.kirbylink.eliquidcalculator.exceptions.ValidationException;
import de.dddns.kirbylink.eliquidcalculator.model.ELiquidBase;
import de.dddns.kirbylink.eliquidcalculator.model.Result;
import de.dddns.kirbylink.eliquidcalculator.model.ResultVolumeWeightPercentage;

@ExtendWith(MockitoExtension.class)
class ConsoleServiceTest {

  @Mock
  private CommandLineConfiguration commandLineConfiguration;
  @Mock
  private InternationalizationService internationalizationService;
  @Mock
  private CommandLineService commandLineService;
  @Mock
  private Calculator calculator;
  @Mock
  private ResultVolumeWeightPercentageMapper resultVolumeWeightPercentageMapper;
  @Mock
  private Options options;
  @Mock
  private CommandLine commandLine;
  @Mock
  private ValidationException validationException;
  private String[] args;

  @InjectMocks
  ConsoleService consoleService;

  @BeforeEach
  void setUp() {
    when(commandLineConfiguration.getConsoleOptions()).thenReturn(options);
  }

  @Test
  void testPrintCalculateLiquid_WhenWithFiftyFiftyMix_ThenResultIsPrinted() throws ParseException {

    // Given
    var eliquidBase = ELiquidBase.builder()
        .withPgPercentage(1)
        .withAmount(100)
        .build();
    var result = Result.builder()
        .withAmount(800)
        .withEliquidBase(eliquidBase)
        .withPg(300)
        .withVg(400)
        .build();
    var resultVolumeWeightPercentage = ResultVolumeWeightPercentage.builder()
        .withBaseLiquidVolume(100)
        .withBaseLiquidWeight(104)
        .withBaseLiquidPercentage(12.5)
        .withPgVolume(300)
        .withPgWeight(311)
        .withPgPercentage(37.5)
        .withVgVolume(400)
        .withVgWeight(504)
        .withVgPercentage(50)
        .build();
    when(commandLineService.getCommandLineFromArgumentsAndOptions(any(String[].class), eq(options))).thenReturn(commandLine);
    when(commandLine.getOptionValue("a")).thenReturn("800");
    when(commandLine.hasOption("n")).thenReturn(true);
    when(commandLine.getOptionValue("n")).thenReturn(String.valueOf("6"));
    when(commandLine.hasOption("bn")).thenReturn(true);
    when(commandLine.getOptionValue("bn")).thenReturn("48");
    when(commandLine.hasOption("bp")).thenReturn(true);
    when(commandLine.getOptionValue("bp")).thenReturn("1");
    when(commandLine.hasOption("bv")).thenReturn(false);
    when(commandLine.hasOption("bw")).thenReturn(false);
    when(commandLine.getOptionValue("p")).thenReturn("0.5");
    when(commandLine.hasOption("v")).thenReturn(true);
    when(commandLine.getOptionValue("v")).thenReturn("0.5");
    when(commandLine.hasOption("w")).thenReturn(false);
    when(calculator.calculateResult(eq(800), any(ELiquidBase.class), eq(6), eq(0.5), eq(0.5), eq(0.0))).thenReturn(result);
    when(resultVolumeWeightPercentageMapper.mapToResultVolumeWeightPercentage(result)).thenReturn(resultVolumeWeightPercentage);

    var expectedOutput = """
        Benötigte Menge
        -----------------------------------------------------------
        \t\tVolumen\t\tGewicht\t\tProzent
        Basisliquid:\t100,00 ml\t104,00 g\t12,50%
        PG:\t\t300,00 ml\t311,00 g\t37,50%
        VG:\t\t400,00 ml\t504,00 g\t50,00%
        Wasser:\t\t000,00 ml\t000,00 g\t00,00%
        -----------------------------------------------------------""";
    when(internationalizationService.requiredQuantityOutput(resultVolumeWeightPercentage)).thenReturn(expectedOutput);
    var byteArrayOutputStream  = new ByteArrayOutputStream();
    var printStream = new PrintStream(byteArrayOutputStream);
    var originalOut = System.out;
    System.setOut(printStream);

    try {
      // When
      args = new String[]{"-a", "800", "-p", "0.5", "-v", "0.5", "-n", "6", "-bn", "48", "-bp", "1"};
      consoleService.printVolumeWeightAndPercentageOfRequiredQuantity(args);

      // Then
      var consoleOuptut = byteArrayOutputStream.toString(StandardCharsets.UTF_8);
      assertThat(consoleOuptut).contains(expectedOutput);
    } finally {
      System.setOut(originalOut);
    }
  }

  @Test
  void testPrintCalculateLiquid_WhenWithPgAndWaterMixAndNoNicotineWithPgBase_ThenResultIsPrinted() throws ParseException {

    // Given
    var eliquidBase = ELiquidBase.builder()
        .withPgPercentage(0.5)
        .withWaterPercentage(0.5)
        .withAmount(100)
        .build();
    var result = Result.builder()
        .withAmount(800)
        .withEliquidBase(eliquidBase)
        .withPg(340)
        .withVg(280)
        .withWater(80)
        .build();
    var resultVolumeWeightPercentage = ResultVolumeWeightPercentage.builder()
        .withPgVolume(400)
        .withPgWeight(414)
        .withPgPercentage(50)
        .withWaterVolume(400)
        .withWaterWeight(400)
        .withWaterPercentage(50)
        .build();
    when(commandLineService.getCommandLineFromArgumentsAndOptions(any(String[].class), eq(options))).thenReturn(commandLine);
    when(commandLine.getOptionValue("a")).thenReturn("800");
    when(commandLine.hasOption("n")).thenReturn(false);
    when(commandLine.hasOption("bn")).thenReturn(true);
    when(commandLine.getOptionValue("bn")).thenReturn("48");
    when(commandLine.hasOption("bp")).thenReturn(true);
    when(commandLine.getOptionValue("bp")).thenReturn("0.5");
    when(commandLine.hasOption("bv")).thenReturn(false);
    when(commandLine.hasOption("bw")).thenReturn(true);
    when(commandLine.getOptionValue("bw")).thenReturn("0.50");
    when(commandLine.getOptionValue("p")).thenReturn("0.50");
    when(commandLine.hasOption("v")).thenReturn(false);
    when(commandLine.hasOption("w")).thenReturn(true);
    when(commandLine.getOptionValue("w")).thenReturn("0.50");
    when(calculator.calculateResult(eq(800), any(ELiquidBase.class), eq(0), eq(0.50), eq(0.0), eq(0.50))).thenReturn(result);
    when(resultVolumeWeightPercentageMapper.mapToResultVolumeWeightPercentage(result)).thenReturn(resultVolumeWeightPercentage);

    var expectedOutput = """
        Benötigte Menge
        -----------------------------------------------------------
        \t\tVolumen\t\tGewicht\t\tProzent
        Basisliquid:\t000,00 ml\t000,00 g\t00,00%
        PG:\t\t400,00 ml\t414,00 g\t50,00%
        VG:\t\t000,00 ml\t000,00 g\t00,00%
        Wasser:\t\t400,00 ml\t400,00 g\t50,00%
        -----------------------------------------------------------""";
    when(internationalizationService.requiredQuantityOutput(resultVolumeWeightPercentage)).thenReturn(expectedOutput);
    var byteArrayOutputStream  = new ByteArrayOutputStream();
    var printStream = new PrintStream(byteArrayOutputStream);
    var originalOut = System.out;
    System.setOut(printStream);

    try {
      // When
      args = new String[]{"-a", "800", "-p", "0.50", "-w", "0.50", "-bn", "48", "-bp", "1"};
      consoleService.printVolumeWeightAndPercentageOfRequiredQuantity(args);

      // Then
      var consoleOuptut = byteArrayOutputStream.toString(StandardCharsets.UTF_8);
      assertThat(consoleOuptut).contains(expectedOutput);
    } finally {
      System.setOut(originalOut);
    }

  }

  @Test
  void testPrintCalculateLiquid_WhenWithFiftyFiftyMixWithZeroBaseNicotineButWithTargetNicotine_ThenIllegalArgumentErrorMessageIsPrinted() throws ParseException {

    // Given
    when(commandLineService.getCommandLineFromArgumentsAndOptions(any(String[].class), eq(options))).thenReturn(commandLine);
    when(commandLine.getOptionValue("a")).thenReturn("800");
    when(commandLine.hasOption("n")).thenReturn(true);
    when(commandLine.getOptionValue("n")).thenReturn(String.valueOf("6"));
    when(commandLine.hasOption("bn")).thenReturn(false);
    when(commandLine.hasOption("bp")).thenReturn(true);
    when(commandLine.getOptionValue("bp")).thenReturn("1");
    when(commandLine.hasOption("bv")).thenReturn(false);
    when(commandLine.hasOption("bw")).thenReturn(false);
    when(commandLine.getOptionValue("p")).thenReturn("0.5");
    when(commandLine.hasOption("v")).thenReturn(true);
    when(commandLine.getOptionValue("v")).thenReturn("0.5");
    when(commandLine.hasOption("w")).thenReturn(false);
    doThrow(new IllegalArgumentException("calculator.exception.illegal.argument.errormessage")).when(calculator).calculateResult(eq(800), any(ELiquidBase.class), eq(6), eq(0.5), eq(0.5), eq(0.0));

    var expectedOutput = "Das Nikotin der Base muss größer oder gleich dem Nikotin im fertigen Liquid sein";
    when(internationalizationService.getMessage("calculator.exception.illegal.argument.errormessage")).thenReturn("Das Nikotin der Base muss größer oder gleich dem Nikotin im fertigen Liquid sein");
    var byteArrayOutputStream  = new ByteArrayOutputStream();
    var printStream = new PrintStream(byteArrayOutputStream);
    var originalOut = System.out;
    System.setOut(printStream);

    try {
      // When
      args = new String[]{"-a", "800", "-p", "0.5", "-v", "0.5", "-n", "6","-bp", "1"};
      consoleService.printVolumeWeightAndPercentageOfRequiredQuantity(args);

      // Then
      var consoleOuptut = byteArrayOutputStream.toString(StandardCharsets.UTF_8);
      assertThat(consoleOuptut).contains(expectedOutput);
    } finally {
      System.setOut(originalOut);
    }

  }

  @Test
  void testPrintCalculateLiquid_WhenWithFiftyFiftyMixWithZeroBasePercentageButWithBaseNicotine_ThenValidationErrorMessageIsPrinted() throws ParseException {

    // Given
    when(commandLineService.getCommandLineFromArgumentsAndOptions(any(String[].class), eq(options))).thenReturn(commandLine);
    when(commandLine.getOptionValue("a")).thenReturn("800");
    when(commandLine.hasOption("n")).thenReturn(true);
    when(commandLine.getOptionValue("n")).thenReturn(String.valueOf("6"));
    when(commandLine.hasOption("bn")).thenReturn(true);
    when(commandLine.getOptionValue("bn")).thenReturn(String.valueOf("0"));
    when(commandLine.hasOption("bp")).thenReturn(true);
    when(commandLine.getOptionValue("bp")).thenReturn(String.valueOf("1"));
    when(commandLine.hasOption("bv")).thenReturn(false);
    when(commandLine.hasOption("bw")).thenReturn(false);
    when(commandLine.getOptionValue("p")).thenReturn("0.5");
    when(commandLine.hasOption("v")).thenReturn(true);
    when(commandLine.getOptionValue("v")).thenReturn("0.5");
    when(commandLine.hasOption("w")).thenReturn(false);
    var errorMessage = "Die Summme aller Prozente muss genau 1.0 ergeben wenn Base Nikotin gesetzt ist.";
    doThrow(validationException).when(calculator).calculateResult(eq(800), any(ELiquidBase.class), eq(6), eq(0.5), eq(0.5), eq(0.0));
    when(validationException.getMessage()).thenReturn(errorMessage);

    var byteArrayOutputStream  = new ByteArrayOutputStream();
    var printStream = new PrintStream(byteArrayOutputStream);
    var originalOut = System.out;
    System.setOut(printStream);

    try {
      // When
      args = new String[]{"-a", "800", "-p", "0.5", "-v", "0.5", "-n", "6","-bn", "48"};
      consoleService.printVolumeWeightAndPercentageOfRequiredQuantity(args);

      // Then
      var consoleOuptut = byteArrayOutputStream.toString(StandardCharsets.UTF_8);
      assertThat(consoleOuptut).contains(errorMessage);
    } finally {
      System.setOut(originalOut);
    }
  }

  @Test
  void testPrintCalculateLiquid_WhenWithNonNumericValueAsAmaountIsSet_ThenValidationErrorMessageIsPrinted() throws ParseException {

    // Given
    when(commandLineService.getCommandLineFromArgumentsAndOptions(any(String[].class), eq(options))).thenReturn(commandLine);
    when(commandLine.getOptionValue("a")).thenReturn("n");
    var errorMessage = "Nur numerische Werte sind erlaubt";
    when(internationalizationService.getMessage("calculator.exception.number.format.errormessage")).thenReturn(errorMessage);

    var byteArrayOutputStream  = new ByteArrayOutputStream();
    var printStream = new PrintStream(byteArrayOutputStream);
    var originalOut = System.out;
    System.setOut(printStream);

    try {
      // When
      args = new String[]{"-a", "n", "-p", "0.5", "-v", "0.5", "-n", "6","-bn", "48"};
      consoleService.printVolumeWeightAndPercentageOfRequiredQuantity(args);

      // Then
      var consoleOuptut = byteArrayOutputStream.toString(StandardCharsets.UTF_8);
      assertThat(consoleOuptut).contains(errorMessage);
    } finally {
      System.setOut(originalOut);
    }
  }
}
