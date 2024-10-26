package de.dddns.kirbylink.eliquidcalculator.config;

import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.ParseException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import de.dddns.kirbylink.eliquidcalculator.service.InternationalizationService;

@ExtendWith(MockitoExtension.class)
class CommandLineConfigurationTest {

  @Mock
  InternationalizationService internationalizationService;

  CommandLineConfiguration commandLineConfiguration;

  @BeforeEach
  void setUp() {
    commandLineConfiguration = new CommandLineConfiguration(internationalizationService);
    when(internationalizationService.getMessage("cli.amount.description")).thenReturn("The amount of finished liquid");
    when(internationalizationService.getMessage("cli.nicotine.description")).thenReturn("The amount of nicotine in the finished liquid");
    when(internationalizationService.getMessage("cli.pg.description")).thenReturn("The amount of pg in the finished liquid");
    when(internationalizationService.getMessage("cli.vg.description")).thenReturn("The amount of vg in the finished liquid");
    when(internationalizationService.getMessage("cli.water.description")).thenReturn("The amount of water in the finished liquid");
    when(internationalizationService.getMessage("cli.base.nicotine.description")).thenReturn("The amount of nicotine in the base");
    when(internationalizationService.getMessage("cli.base.pg.description")).thenReturn("The amount of pg in the base");
    when(internationalizationService.getMessage("cli.base.vg.description")).thenReturn("The amount of vg in the base");
    when(internationalizationService.getMessage("cli.base.water.description")).thenReturn("The amount of water in the base");
    when(internationalizationService.getMessage("cli.help.description")).thenReturn("Print this message");
    when(internationalizationService.getMessage("cli.version.description")).thenReturn("Version information");
    when(internationalizationService.getMessage("cli.gui.description")).thenReturn("Starts the gui interface");
  }

  @Test
  void testGetCommandLine_WhenAllRequiredArgsAvailable_ThenCommandLineWithOptionsIsReturned() throws ParseException {

    // Given
    var options = commandLineConfiguration.getConsoleOptions();
    String[] args = {"-a", "800", "-p", "0.5", "-v", "0.5", "-n", "6", "-bn", "48", "-bp", "1"};


    // When
    var commandLine = commandLineConfiguration.getCommandLine(options, args);

    // Then
    assertThat(commandLine.hasOption("a")).isTrue();
    assertThat(commandLine.hasOption("n")).isTrue();
    assertThat(commandLine.hasOption("p")).isTrue();
    assertThat(commandLine.hasOption("v")).isTrue();
    assertThat(commandLine.hasOption("w")).isFalse();
    assertThat(commandLine.hasOption("bn")).isTrue();
    assertThat(commandLine.hasOption("bp")).isTrue();
    assertThat(commandLine.hasOption("bv")).isFalse();
    assertThat(commandLine.hasOption("bw")).isFalse();
  }

  @Test
  void testGetCommandLine_WhenRequiredArgsAreMissing_ThenMissingOptionExceptionIsThrown() {

    // Given
    var options = commandLineConfiguration.getConsoleOptions();
    String[] args = {};

    // When
    var throwable = catchThrowable(() -> {
      commandLineConfiguration.getCommandLine(options, args);
    });

    // Then
    assertThat(throwable).isInstanceOf(MissingOptionException.class);
  }

  @Test
  void testGetConsoleOptions() {
    // Given

    // When
    var options = commandLineConfiguration.getConsoleOptions();

    // Then
    assertThat(options.hasShortOption("a")).isTrue();
    assertThat(options.getOption("a").isRequired()).isTrue();
    assertThat(options.hasShortOption("n")).isTrue();
    assertThat(options.getOption("n").isRequired()).isFalse();
    assertThat(options.hasShortOption("p")).isTrue();
    assertThat(options.getOption("p").isRequired()).isTrue();
    assertThat(options.hasShortOption("v")).isTrue();
    assertThat(options.getOption("v").isRequired()).isFalse();
    assertThat(options.hasShortOption("w")).isTrue();
    assertThat(options.getOption("w").isRequired()).isFalse();
    assertThat(options.hasShortOption("bn")).isTrue();
    assertThat(options.getOption("bn").isRequired()).isFalse();
    assertThat(options.hasShortOption("bp")).isTrue();
    assertThat(options.getOption("bp").isRequired()).isFalse();
    assertThat(options.hasShortOption("bv")).isTrue();
    assertThat(options.getOption("bv").isRequired()).isFalse();
    assertThat(options.hasShortOption("bw")).isTrue();
    assertThat(options.getOption("bw").isRequired()).isFalse();
    assertThat(options.hasShortOption("h")).isTrue();
    assertThat(options.getOption("h").isRequired()).isFalse();
    assertThat(options.hasShortOption("g")).isTrue();
    assertThat(options.getOption("g").isRequired()).isFalse();
    assertThat(options.hasLongOption("version")).isTrue();
    assertThat(options.getOption("version").isRequired()).isFalse();
  }

  @Test
  void testGetGuiOptions() {
    // Given

    // When
    var options = commandLineConfiguration.getGuiOptions();

    // Then
    assertThat(options.hasShortOption("a")).isTrue();
    assertThat(options.getOption("a").isRequired()).isFalse();
    assertThat(options.hasShortOption("n")).isTrue();
    assertThat(options.getOption("n").isRequired()).isFalse();
    assertThat(options.hasShortOption("p")).isTrue();
    assertThat(options.getOption("p").isRequired()).isFalse();
    assertThat(options.hasShortOption("v")).isTrue();
    assertThat(options.getOption("v").isRequired()).isFalse();
    assertThat(options.hasShortOption("w")).isTrue();
    assertThat(options.getOption("w").isRequired()).isFalse();
    assertThat(options.hasShortOption("bn")).isTrue();
    assertThat(options.getOption("bn").isRequired()).isFalse();
    assertThat(options.hasShortOption("bp")).isTrue();
    assertThat(options.getOption("bp").isRequired()).isFalse();
    assertThat(options.hasShortOption("bv")).isTrue();
    assertThat(options.getOption("bv").isRequired()).isFalse();
    assertThat(options.hasShortOption("bw")).isTrue();
    assertThat(options.getOption("bw").isRequired()).isFalse();
    assertThat(options.hasShortOption("h")).isTrue();
    assertThat(options.getOption("h").isRequired()).isFalse();
    assertThat(options.hasShortOption("g")).isTrue();
    assertThat(options.getOption("g").isRequired()).isFalse();
    assertThat(options.hasLongOption("version")).isTrue();
    assertThat(options.getOption("version").isRequired()).isFalse();
  }

}
