package de.dddns.kirbylink.eliquidcalculator.config;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.springframework.context.annotation.Configuration;
import de.dddns.kirbylink.eliquidcalculator.service.InternationalizationService;
import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class CommandLineConfiguration {

  public static final String CLI_AMOUNT_SHORT_OPTION = "a";
  public static final String CLI_AMOUNT_LONG_OPTION = "amount";
  public static final String CLI_NICOTINE_SHORT_OPTION = "n";
  public static final String CLI_NICOTINE_LONG_OPTION = "nicotine";
  public static final String CLI_PG_SHORT_OPTION = "p";
  public static final String CLI_PG_LONG_OPTION = "pg";
  public static final String CLI_VG_SHORT_OPTION = "v";
  public static final String CLI_VG_LONG_OPTION = "vg";
  public static final String CLI_WATER_SHORT_OPTION = "w";
  public static final String CLI_WATER_LONG_OPTION = "water";
  public static final String CLI_BASE_NICOTINE_SHORT_OPTION = "bn";
  public static final String CLI_BASE_NICOTINE_LONG_OPTION = "base-nicotine";
  public static final String CLI_BASE_PG_SHORT_OPTION = "bp";
  public static final String CLI_BASE_PG_LONG_OPTION = "base-pg";
  public static final String CLI_BASE_VG_SHORT_OPTION = "bv";
  public static final String CLI_BASE_VG_LONG_OPTION = "base-vg";
  public static final String CLI_BASE_WATER_SHORT_OPTION = "bw";
  public static final String CLI_BASE_WATER_LONG_OPTION = "base-water";
  public static final String CLI_HELP_SHORT_OPTION = "h";
  public static final String CLI_HELP_LONG_OPTION = "help";
  public static final String CLI_GUI_SHORT_OPTION = "g";
  public static final String CLI_GUI_LONG_OPTION = "gui";

  private final InternationalizationService internationalizationService;

  public Options getConsoleOptions() {

    var options = getNonRequiredDefaultOptions();

    options.addOption(createOption(CLI_AMOUNT_SHORT_OPTION, CLI_AMOUNT_LONG_OPTION, true, internationalizationService.getMessage("cli.amount.description"), int.class, true));
    options.addOption(createOption(CLI_PG_SHORT_OPTION, CLI_PG_LONG_OPTION, true, internationalizationService.getMessage("cli.pg.description"), double.class, true));

    return options;
  }

  public Options getGuiOptions() {

    var options = getNonRequiredDefaultOptions();

    options.addOption(createOption(CLI_AMOUNT_SHORT_OPTION, CLI_AMOUNT_LONG_OPTION, true, internationalizationService.getMessage("cli.amount.description"), int.class, false));
    options.addOption(createOption(CLI_PG_SHORT_OPTION, CLI_PG_LONG_OPTION, true, internationalizationService.getMessage("cli.pg.description"), double.class, false));

    return options;
  }

  private Options getNonRequiredDefaultOptions() {

    var options = new Options();

    options.addOption(createOption(CLI_NICOTINE_SHORT_OPTION, CLI_NICOTINE_LONG_OPTION, true, internationalizationService.getMessage("cli.nicotine.description"), int.class, false));
    options.addOption(createOption(CLI_VG_SHORT_OPTION, CLI_VG_LONG_OPTION, true, internationalizationService.getMessage("cli.vg.description"), double.class, false));
    options.addOption(createOption(CLI_WATER_SHORT_OPTION, CLI_WATER_LONG_OPTION, true, internationalizationService.getMessage("cli.water.description"), double.class, false));
    options.addOption(createOption(CLI_BASE_NICOTINE_SHORT_OPTION, CLI_BASE_NICOTINE_LONG_OPTION, true, internationalizationService.getMessage("cli.base.nicotine.description"), int.class, false));
    options.addOption(createOption(CLI_BASE_PG_SHORT_OPTION, CLI_BASE_PG_LONG_OPTION, true, internationalizationService.getMessage("cli.base.pg.description"), double.class, false));
    options.addOption(createOption(CLI_BASE_VG_SHORT_OPTION, CLI_BASE_VG_LONG_OPTION, true, internationalizationService.getMessage("cli.base.vg.description"), double.class, false));
    options.addOption(createOption(CLI_BASE_WATER_SHORT_OPTION, CLI_BASE_WATER_LONG_OPTION, true, internationalizationService.getMessage("cli.base.water.description"), double.class, false));
    options.addOption(createOption(CLI_HELP_SHORT_OPTION, CLI_HELP_LONG_OPTION, false, internationalizationService.getMessage("cli.help.description"), double.class, false));
    options.addOption(createOption(CLI_GUI_SHORT_OPTION, CLI_GUI_LONG_OPTION, false, internationalizationService.getMessage("cli.gui.description"), double.class, false));

    return options;
  }

  public CommandLine getCommandLine(Options options, String[] args) throws ParseException {
    return new DefaultParser().parse(options, args);
  }

  public void printHelp(String cmdLineSyntax, Options options) {
    new HelpFormatter().printHelp(cmdLineSyntax, options);
  }

  private Option createOption(String shortOption, String longOption, boolean hasArg, String description, Class<?> type, boolean required) {
    return Option.builder(shortOption)
    .longOpt(longOption)
    .hasArg(hasArg)
    .desc(description)
    .type(type)
    .required(required)
    .build();
  }
}
