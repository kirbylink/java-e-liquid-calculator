package de.dddns.kirbylink.eliquidcalculator.service;

import static de.dddns.kirbylink.eliquidcalculator.config.CommandLineConfiguration.CLI_HELP_LONG_OPTION;
import static de.dddns.kirbylink.eliquidcalculator.config.CommandLineConfiguration.CLI_HELP_SHORT_OPTION;
import static de.dddns.kirbylink.eliquidcalculator.utility.Utility.hasArgument;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.springframework.stereotype.Service;
import de.dddns.kirbylink.eliquidcalculator.config.CommandLineConfiguration;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CommandLineService {

  private final CommandLineConfiguration commandLineConfiguration;
  private final InternationalizationService internationalizationService;

  public CommandLine getCommandLineFromArgumentsAndOptions(String[] args, Options options) {
    var printHelp = hasArgument(args, CLI_HELP_SHORT_OPTION, CLI_HELP_LONG_OPTION);
    if(printHelp) {
      commandLineConfiguration.printHelp(internationalizationService.getMessage("cli.helper.usage.message"), options);
      return null;
    }

    CommandLine commandLine = null;
    try {
      commandLine = commandLineConfiguration.getCommandLine(options, args);
    } catch (Exception e) {
      commandLineConfiguration.printHelp(internationalizationService.getMessage("cli.helper.usage.message") + System.lineSeparator() + e.getLocalizedMessage(), options);
      return null;
    }
    return commandLine;
  }

}
