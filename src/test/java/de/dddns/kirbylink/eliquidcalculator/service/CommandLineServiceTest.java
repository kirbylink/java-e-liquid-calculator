package de.dddns.kirbylink.eliquidcalculator.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import de.dddns.kirbylink.eliquidcalculator.config.CommandLineConfiguration;

@ExtendWith(MockitoExtension.class)
class CommandLineServiceTest {

  @Mock
  private CommandLineConfiguration commandLineConfiguration;
  @Mock
  private InternationalizationService internationalizationService;
  @Mock
  private Options options;
  @Mock
  private CommandLine commandLine;
  @InjectMocks
  CommandLineService commandLineService;

  @Test
  void testGetCommandLineFromArgumentsAndOptions() throws ParseException {

    // Given
    var args = new String[]{"-a", "800", "-p", "0.5", "-v", "0.5", "-n", "6", "-bn", "48", "-bp", "1"};
    when(commandLineConfiguration.getCommandLine(options, args)).thenReturn(commandLine);

    // When
    var actualCommandLine = commandLineService.getCommandLineFromArgumentsAndOptions(args, options);

    // Then
    assertThat(actualCommandLine).isEqualTo(commandLine);
  }

  @ParameterizedTest
  @CsvSource(value = {"-h", "--help"})
  void testGetCommandLineFromArgumentsAndOptions_WhenArgumentContainsHelpOption_ThenPrintHelpIsLogged(String argument) {

    // Given
    var cliHelperUsageMessage = """
        usage: java -jar e-liquid-calculator-<version>-jar-with-dependencies.jar
         -a,--amount <arg>           Die Gesamtmenge des fertigen Liquids
         -bn,--base-nicotine <arg>   Die Menge an Nikotin in der Base
         -bp,--base-pg <arg>         Prozentualer PG Anteil in der Base
         -bv,--base-vg <arg>         Prozentualer VG Anteil in der Base
         -bw,--base-water <arg>      Prozentualer Wasser Anteil in der Base
         -g,--gui                    Startet die grafische Oberfläche
         -h,--help                   Zeigt diese Nachricht
         -n,--nicotine <arg>         Die Gesamtmenge an Nikotin im fertigen Liquid
         -p,--pg <arg>               Prozentualer PG Anteil im fertigen Liquid
         -v,--vg <arg>               Prozentualer VG Anteil im fertigen Liquid
            """;
    when(internationalizationService.getMessage("cli.helper.usage.message")).thenReturn(cliHelperUsageMessage);

    // When
    var args = new String[] {argument};
    commandLineService.getCommandLineFromArgumentsAndOptions(args, options);

    // Then
    verify(commandLineConfiguration).printHelp(cliHelperUsageMessage, options);
  }

  @Test
  void testGetCommandLineFromArgumentsAndOptions_WhenExceptionOccursWhenGettingTheCommandLine_ThenPrintHelpIsLogged() throws ParseException {

    // Given
    var exceptionWithLocalizedMessage = new ExceptionWithLocalizedMessage();
    doThrow(exceptionWithLocalizedMessage).when(commandLineConfiguration).getCommandLine(eq(options), any(String[].class));
    var cliHelperUsageMessage = """
        usage: java -jar e-liquid-calculator-<version>-jar-with-dependencies.jar
         -a,--amount <arg>           Die Gesamtmenge des fertigen Liquids
         -bn,--base-nicotine <arg>   Die Menge an Nikotin in der Base
         -bp,--base-pg <arg>         Prozentualer PG Anteil in der Base
         -bv,--base-vg <arg>         Prozentualer VG Anteil in der Base
         -bw,--base-water <arg>      Prozentualer Wasser Anteil in der Base
         -g,--gui                    Startet die grafische Oberfläche
         -h,--help                   Zeigt diese Nachricht
         -n,--nicotine <arg>         Die Gesamtmenge an Nikotin im fertigen Liquid
         -p,--pg <arg>               Prozentualer PG Anteil im fertigen Liquid
         -v,--vg <arg>               Prozentualer VG Anteil im fertigen Liquid
            """;
    var expectedOutput = cliHelperUsageMessage + System.lineSeparator() + "The argument is illegal";
    when(internationalizationService.getMessage("cli.helper.usage.message")).thenReturn(cliHelperUsageMessage);

    // When
    var args = new String[]{"-a", "800", "-p", "0.5", "-v", "0.5", "-n", "6", "-bn", "48", "-bp", "1"};
    commandLineService.getCommandLineFromArgumentsAndOptions(args, options);

    // Then
    verify(commandLineConfiguration).printHelp(expectedOutput, options);
  }

  private static class ExceptionWithLocalizedMessage extends IllegalArgumentException {

    private static final long serialVersionUID = -620624574529771836L;

    @Override
    public String getLocalizedMessage() {
      return "The argument is illegal";
    }
  }

}
