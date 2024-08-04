package de.dddns.kirbylink.eliquidcalculator;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.stream.Stream;
import org.apache.commons.cli.ParseException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.BeansException;
import jakarta.validation.ConstraintViolationException;

@ExtendWith(MockitoExtension.class)
class ELiquidCalculatorApplicationIntegrationTest {

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

  @Nested
  @DisplayName("Test for console application")
  class TestsForConsoleApplication {

    @ParameterizedTest
    @MethodSource ("provideArgsWithHelpParameterForMainMethod")
    void testMain_WhenParameterForHelpIsSet_ThenHelpIsPrintedOut(String args) throws BeansException, ParseException, IOException {

      // Given
      var byteArrayOutputStream  = new ByteArrayOutputStream();
      var printStream = new PrintStream(byteArrayOutputStream);
      var originalOut = System.out;
      System.setOut(printStream);
      var expectedOutput = """
          usage: java -jar e-liquid-calculator-<version>-jar-with-dependencies.jar
           -a,--amount <arg>           Die Gesamtmenge des fertigen Liquids
           -bn,--base-nicotine <arg>   Die Nikotinmenge in der Basis
           -bp,--base-pg <arg>         Prozentualer PG-Anteil in der Basis
           -bv,--base-vg <arg>         Prozentualer VG-Anteil in der Basis
           -bw,--base-water <arg>      Prozentualer Wasseranteil in der Basis
           -g,--gui                    Startet die grafische Oberfläche
           -h,--help                   Zeigt diese Hilfe an
           -n,--nicotine <arg>         Die Nikotinmenge im fertigen Liquid
           -p,--pg <arg>               Prozentualer PG-Anteil im fertigen Liquid
           -v,--vg <arg>               Prozentualer VG-Anteil im fertigen Liquid
           -w,--water <arg>            Prozentualer Wasseranteil im fertigen Liquid""";

      // When
      ELiquidCalculatorApplication.main(args.split("\\s+"));

      try {
        // When
        ELiquidCalculatorApplication.main(args.split("\\s+"));

        // Then
        var consoleOuptut = byteArrayOutputStream.toString(StandardCharsets.UTF_8);
        assertThat(consoleOuptut).contains(expectedOutput);
      } finally {
        System.setOut(originalOut);
      }
    }

    private static Stream<Arguments> provideArgsWithHelpParameterForMainMethod() {
      return Stream.of(
              Arguments.of("-a 800 -p 1 -n 6 -bn 48 -bp 1 -h"),
              Arguments.of("-p 1 -n 6 -bn 48 -bp 1 -h"),
              Arguments.of("-h")
      );
    }

    @Test
    void testMain_WithFinishedLiquidWithFiftyFiftyMixAndPgBase() throws BeansException, ParseException, IOException {

      // Given
      String[] args = {"-a", "800", "-p", "0.5", "-v", "0.5", "-n", "6", "-bn", "48", "-bp", "1"};
      var byteArrayOutputStream  = new ByteArrayOutputStream();
      var printStream = new PrintStream(byteArrayOutputStream);
      var originalOut = System.out;
      System.setOut(printStream);
      var expectedOutput = """
          Benötigte Mengen
          -----------------------------------------------------------
          \t\tVolumen\t\tGewicht\t\tProzent
          Basisliquid:\t100,00 ml\t104,00 g\t12,50%
          PG:\t\t300,00 ml\t311,00 g\t37,50%
          VG:\t\t400,00 ml\t504,00 g\t50,00%
          Wasser:\t\t000,00 ml\t000,00 g\t00,00%
          -----------------------------------------------------------""";

      try {
        // When
        ELiquidCalculatorApplication.main(args);

        // Then
        var consoleOuptut = byteArrayOutputStream.toString(StandardCharsets.UTF_8);
        assertThat(consoleOuptut).contains(expectedOutput);
      } finally {
        System.setOut(originalOut);
      }
    }

    @Test
    void testMain_WithFinishedLiquidWithFiftyFiftyMixAndPgBaseAndEnglishLocale() throws BeansException, ParseException, IOException {

      // Given
      Locale.setDefault(Locale.ENGLISH);
      String[] args = {"-a", "800", "-p", "0.5", "-v", "0.5", "-n", "6", "-bn", "48", "-bp", "1"};
      var byteArrayOutputStream  = new ByteArrayOutputStream();
      var printStream = new PrintStream(byteArrayOutputStream);
      var originalOut = System.out;
      System.setOut(printStream);
      var expectedOutput = """
          Required amounts
          -----------------------------------------------------------
          \t\tVolume\t\tWeight\t\tPercentage
          Base liquid:\t100.00 ml\t104.00 g\t12.50%
          PG:\t\t300.00 ml\t311.00 g\t37.50%
          VG:\t\t400.00 ml\t504.00 g\t50.00%
          Water:\t\t000.00 ml\t000.00 g\t00.00%
          -----------------------------------------------------------""";

      try {
        // When
        ELiquidCalculatorApplication.main(args);

        // Then
        var consoleOuptut = byteArrayOutputStream.toString(StandardCharsets.UTF_8);
        assertThat(consoleOuptut).contains(expectedOutput);
      } finally {
        System.setOut(originalOut);
      }
    }

    @Test
    void testMain_WithFinishedLiquidWithTraditionalMixPgBase() throws BeansException, ParseException, IOException {

      // Given
      String[] args = {"-a", "800", "-p", "0.55", "-v", "0.35", "-w", "0.10", "-n", "6", "-bn", "48", "-bp", "1"};
      var byteArrayOutputStream  = new ByteArrayOutputStream();
      var printStream = new PrintStream(byteArrayOutputStream);
      var originalOut = System.out;
      System.setOut(printStream);
      var expectedOutput = """
          Benötigte Mengen
          -----------------------------------------------------------
          \t\tVolumen\t\tGewicht\t\tProzent
          Basisliquid:\t100,00 ml\t104,00 g\t12,50%
          PG:\t\t340,00 ml\t352,00 g\t42,50%
          VG:\t\t280,00 ml\t353,00 g\t35,00%
          Wasser:\t\t080,00 ml\t080,00 g\t10,00%
          -----------------------------------------------------------""";

      try {
        // When
        ELiquidCalculatorApplication.main(args);

        // Then
        var consoleOuptut = byteArrayOutputStream.toString(StandardCharsets.UTF_8);
        assertThat(consoleOuptut).contains(expectedOutput);
      } finally {
        System.setOut(originalOut);
      }
    }

    @Test
    void testMain_WithFinishedLiquidWithFiftyFiftyMixAndFiftyFiftyBase() throws BeansException, ParseException, IOException {

      // Given
      String[] args = {"-a", "800", "-p", "0.5", "-v", "0.5", "-n", "6", "-bn", "48", "-bp", "0.5", "-bv", "0.5"};
      var byteArrayOutputStream  = new ByteArrayOutputStream();
      var printStream = new PrintStream(byteArrayOutputStream);
      var originalOut = System.out;
      System.setOut(printStream);
      var expectedOutput = """
          Benötigte Mengen
          -----------------------------------------------------------
          \t\tVolumen\t\tGewicht\t\tProzent
          Basisliquid:\t100,00 ml\t115,00 g\t12,50%
          PG:\t\t350,00 ml\t363,00 g\t43,75%
          VG:\t\t350,00 ml\t441,00 g\t43,75%
          Wasser:\t\t000,00 ml\t000,00 g\t00,00%
          -----------------------------------------------------------""";

      try {
        // When
        ELiquidCalculatorApplication.main(args);

        // Then
        var consoleOuptut = byteArrayOutputStream.toString(StandardCharsets.UTF_8);
        assertThat(consoleOuptut).contains(expectedOutput);
      } finally {
        System.setOut(originalOut);
      }
    }

    @Test
    void testMain_WithFinishedLiquidWithTraditionalMixFiftyFiftyBase() throws BeansException, ParseException, IOException {

      // Given
      String[] args = {"-a", "800", "-p", "0.55", "-v", "0.35", "-w", "0.10", "-n", "6", "-bn", "48", "-bp", "0.5", "-bv", "0.5"};
      var byteArrayOutputStream  = new ByteArrayOutputStream();
      var printStream = new PrintStream(byteArrayOutputStream);
      var originalOut = System.out;
      System.setOut(printStream);
      var expectedOutput = """
          Benötigte Mengen
          -----------------------------------------------------------
          \t\tVolumen\t\tGewicht\t\tProzent
          Basisliquid:\t100,00 ml\t115,00 g\t12,50%
          PG:\t\t390,00 ml\t404,00 g\t48,75%
          VG:\t\t230,00 ml\t290,00 g\t28,75%
          Wasser:\t\t080,00 ml\t080,00 g\t10,00%
          -----------------------------------------------------------""";

      try {
        // When
        ELiquidCalculatorApplication.main(args);

        // Then
        var consoleOuptut = byteArrayOutputStream.toString(StandardCharsets.UTF_8);
        assertThat(consoleOuptut).contains(expectedOutput);
      } finally {
        System.setOut(originalOut);
      }
    }

    @Test
    void testMain_WithFinishedLiquidWithoutVgAndWater() throws BeansException, ParseException, IOException {

      // Given
      String[] args = {"-a", "800", "-p", "1", "-n", "6", "-bn", "48", "-bp", "1"};
      var byteArrayOutputStream  = new ByteArrayOutputStream();
      var printStream = new PrintStream(byteArrayOutputStream);
      var originalOut = System.out;
      System.setOut(printStream);
      var expectedOutput = """
          Benötigte Mengen
          -----------------------------------------------------------
          \t\tVolumen\t\tGewicht\t\tProzent
          Basisliquid:\t100,00 ml\t104,00 g\t12,50%
          PG:\t\t700,00 ml\t725,00 g\t87,50%
          VG:\t\t000,00 ml\t000,00 g\t00,00%
          Wasser:\t\t000,00 ml\t000,00 g\t00,00%
          -----------------------------------------------------------""";

      try {
        // When
        ELiquidCalculatorApplication.main(args);

        // Then
        var consoleOuptut = byteArrayOutputStream.toString(StandardCharsets.UTF_8);
        assertThat(consoleOuptut).contains(expectedOutput);
      } finally {
        System.setOut(originalOut);
      }
    }

    @Test
    void testMain_WithFinishedLiquidWithFiftyFiftyMixAndZeroBase() throws BeansException, ParseException, IOException {

      // Given
      String[] args = {"-a", "800", "-p", "0.5", "-v", "0.5"};
      var byteArrayOutputStream  = new ByteArrayOutputStream();
      var printStream = new PrintStream(byteArrayOutputStream);
      var originalOut = System.out;
      System.setOut(printStream);
      var expectedOutput = """
          Benötigte Mengen
          -----------------------------------------------------------
          \t\tVolumen\t\tGewicht\t\tProzent
          Basisliquid:\t000,00 ml\t000,00 g\t00,00%
          PG:\t\t400,00 ml\t414,00 g\t50,00%
          VG:\t\t400,00 ml\t504,00 g\t50,00%
          Wasser:\t\t000,00 ml\t000,00 g\t00,00%
          -----------------------------------------------------------""";

      try {
        // When
        ELiquidCalculatorApplication.main(args);

        // Then
        var consoleOuptut = byteArrayOutputStream.toString(StandardCharsets.UTF_8);
        assertThat(consoleOuptut).contains(expectedOutput);
      } finally {
        System.setOut(originalOut);
      }
    }

    @Nested
    @DisplayName("Test methods for validation checks")
    class TestsForValidationChecks {

      @ParameterizedTest
      @ValueSource(strings = {"0", "-800"})
      void testMain__WhenUsingZeroOrNegativeAmount_ThenValidationExceptionIsThrown(String amount) throws BeansException {

        // Given
        String[] args = {"-a", amount, "-p", "1", "-n", "6", "-bn", "48", "-bp", "1"};
        var expectedOutput = "muss größer als 0 sein";

        // When
        var throwAbleMethod = catchThrowable(() -> {
          ELiquidCalculatorApplication.main(args);
        });

        // Then
        assertThat(throwAbleMethod).isInstanceOf(ConstraintViolationException.class).hasMessageContaining(expectedOutput);
      }

      @Test
      void testMain__WhenSumOfPercentageIsNotEqualOne_ThenEnglishValidationExceptionIsThrown() throws BeansException {

        // Given
        Locale.setDefault(Locale.ENGLISH);
        String[] args = {"-a", "800", "-p", "0.5", "-v", "0.5", "-w", "0.5", "-n", "6", "-bn", "48", "-bp", "1"};
        var expectedOutput = "The sum of all percents must be exactly 1.0";

        // When
        var throwAbleMethod = catchThrowable(() -> {
          ELiquidCalculatorApplication.main(args);
        });

        // Then
        assertThat(throwAbleMethod).isInstanceOf(ConstraintViolationException.class).hasMessageContaining(expectedOutput);
      }
    }
  }


  @Nested
  @DisplayName("Test for gui application")
  class TestsForGuiApplication {
    @ParameterizedTest
    @MethodSource ("provideArrayListWithGuiParameterForMainMethod")
    void testMain_WhenParameterForGuiIsSetOrNoArgumentsProvided_ThenGuiIsPrintedOut(List<String> args) throws BeansException, ParseException, IOException {

      // Given
      var byteArrayOutputStream  = new ByteArrayOutputStream();
      var printStream = new PrintStream(byteArrayOutputStream);
      var originalOut = System.out;
      System.setOut(printStream);
      var expectedOutput = "Gui successfully started.";

      try {
        // When
        ELiquidCalculatorApplication.main(args.toArray(new String[0]));

        // Then
        var consoleOuptut = byteArrayOutputStream.toString(StandardCharsets.UTF_8);
        assertThat(consoleOuptut).contains(expectedOutput);
      } finally {
        System.setOut(originalOut);
      }
    }

    private static Stream<Arguments> provideArrayListWithGuiParameterForMainMethod() {
      return Stream.of(
        Arguments.of(Arrays.asList("-a", "800", "-p", "1", "-n", "6", "-bn", "48", "-bp", "1", "-g")),
        Arguments.of(Arrays.asList("-p", "1", "-n", "6", "-bn", "48", "-bp", "1", "-g")),
        Arguments.of(Arrays.asList("-g")),
        Arguments.of(Collections.emptyList())
      );
    }
  }
}
