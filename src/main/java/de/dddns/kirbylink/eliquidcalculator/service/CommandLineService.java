package de.dddns.kirbylink.eliquidcalculator.service;

import static de.dddns.kirbylink.eliquidcalculator.config.CommandLineConfiguration.CLI_HELP_LONG_OPTION;
import static de.dddns.kirbylink.eliquidcalculator.config.CommandLineConfiguration.CLI_HELP_SHORT_OPTION;
import static de.dddns.kirbylink.eliquidcalculator.config.CommandLineConfiguration.CLI_VERSION_LONG_OPTION;
import static de.dddns.kirbylink.eliquidcalculator.utility.Utility.hasArgument;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.springframework.stereotype.Service;
import de.dddns.kirbylink.eliquidcalculator.config.CommandLineConfiguration;
import de.dddns.kirbylink.eliquidcalculator.utility.AboutInformation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommandLineService {

  private final CommandLineConfiguration commandLineConfiguration;
  private final InternationalizationService internationalizationService;
  private final AboutInformation aboutInformation;

  public CommandLine getCommandLineFromArgumentsAndOptions(String[] args, Options options) {
    var printHelp = hasArgument(args, CLI_HELP_SHORT_OPTION, CLI_HELP_LONG_OPTION);
    if (printHelp) {
      commandLineConfiguration.printHelp(internationalizationService.getMessage("cli.helper.usage.message"), options);
      return null;
    }

    var printAbout = hasArgument(args, CLI_VERSION_LONG_OPTION, CLI_VERSION_LONG_OPTION);
    if (printAbout) {
      log.info("Version: {}\n\n{}", aboutInformation.getApplicationVersion(), aboutInformation.getJavaInformation());
      return null;
    }

    try {
      return commandLineConfiguration.getCommandLine(options, args);
    } catch (Exception e) {
      commandLineConfiguration.printHelp(internationalizationService.getMessage("cli.helper.usage.message") + System.lineSeparator() + e.getLocalizedMessage(), options);
      return null;
    }
  }

}
