package de.dddns.kirbylink.eliquidcalculator.service;

import static de.dddns.kirbylink.eliquidcalculator.config.CommandLineConfiguration.CLI_AMOUNT_SHORT_OPTION;
import static de.dddns.kirbylink.eliquidcalculator.config.CommandLineConfiguration.CLI_BASE_NICOTINE_SHORT_OPTION;
import static de.dddns.kirbylink.eliquidcalculator.config.CommandLineConfiguration.CLI_BASE_PG_SHORT_OPTION;
import static de.dddns.kirbylink.eliquidcalculator.config.CommandLineConfiguration.CLI_BASE_VG_SHORT_OPTION;
import static de.dddns.kirbylink.eliquidcalculator.config.CommandLineConfiguration.CLI_BASE_WATER_SHORT_OPTION;
import static de.dddns.kirbylink.eliquidcalculator.config.CommandLineConfiguration.CLI_NICOTINE_SHORT_OPTION;
import static de.dddns.kirbylink.eliquidcalculator.config.CommandLineConfiguration.CLI_PG_SHORT_OPTION;
import static de.dddns.kirbylink.eliquidcalculator.config.CommandLineConfiguration.CLI_VG_SHORT_OPTION;
import static de.dddns.kirbylink.eliquidcalculator.config.CommandLineConfiguration.CLI_WATER_SHORT_OPTION;
import java.util.Optional;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.ParseException;
import org.springframework.stereotype.Service;
import de.dddns.kirbylink.eliquidcalculator.calculator.Calculator;
import de.dddns.kirbylink.eliquidcalculator.config.CommandLineConfiguration;
import de.dddns.kirbylink.eliquidcalculator.converter.ResultVolumeWeightPercentageMapper;
import de.dddns.kirbylink.eliquidcalculator.exceptions.ValidationException;
import de.dddns.kirbylink.eliquidcalculator.model.ELiquidBase;
import de.dddns.kirbylink.eliquidcalculator.model.Result;
import de.dddns.kirbylink.eliquidcalculator.model.ResultVolumeWeightPercentage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ConsoleService {

  private final CommandLineConfiguration commandLineConfiguration;
  private final CommandLineService commandLineService;
  private final InternationalizationService internationalizationService;
  private final Calculator calculator;
  private final ResultVolumeWeightPercentageMapper resultVolumeWeightPercentageMapper;

  public void printVolumeWeightAndPercentageOfRequiredQuantity(String[] args) throws ParseException {

    var options = commandLineConfiguration.getConsoleOptions();

    var optionalCommandLine = Optional.ofNullable(commandLineService.getCommandLineFromArgumentsAndOptions(args, options));

    if (optionalCommandLine.isEmpty()) {
      return;
    }

    var commandLine = optionalCommandLine.get();

    try {
      var resultVolumeWeightPercentage = calculateVolumeWeightAndPercentageForConsoleOutput(commandLine);
      var requiredQuantityOutputString = internationalizationService.requiredQuantityOutput(resultVolumeWeightPercentage);
      log.info(requiredQuantityOutputString);
    } catch (ValidationException e) {
      log.error(e.getMessage());
    } catch (NumberFormatException e) {
      log.error(internationalizationService.getMessage("calculator.exception.number.format.errormessage"));
    } catch (IllegalArgumentException e) {
      var errorMessage = internationalizationService.getMessage(e.getMessage());
      log.error(errorMessage);
    }
  }

  private ResultVolumeWeightPercentage calculateVolumeWeightAndPercentageForConsoleOutput(CommandLine commandLine) {
    var amount = Integer.valueOf(commandLine.getOptionValue(CLI_AMOUNT_SHORT_OPTION));
    var targetNicotine = commandLine.hasOption(CLI_NICOTINE_SHORT_OPTION) ? Integer.valueOf(commandLine.getOptionValue(CLI_NICOTINE_SHORT_OPTION)) : 0;
    var eLiquidBase = ELiquidBase.builder()
        .withNicotine(commandLine.hasOption(CLI_BASE_NICOTINE_SHORT_OPTION) ? Integer.valueOf(commandLine.getOptionValue(CLI_BASE_NICOTINE_SHORT_OPTION)) : 0)
        .withPgPercentage(commandLine.hasOption(CLI_BASE_PG_SHORT_OPTION) ? Double.valueOf(commandLine.getOptionValue(CLI_BASE_PG_SHORT_OPTION)) : 0)
        .withVgPercentage(commandLine.hasOption(CLI_BASE_VG_SHORT_OPTION) ? Double.valueOf(commandLine.getOptionValue(CLI_BASE_VG_SHORT_OPTION)) : 0)
        .withWaterPercentage(commandLine.hasOption(CLI_BASE_WATER_SHORT_OPTION) ? Double.valueOf(commandLine.getOptionValue(CLI_BASE_WATER_SHORT_OPTION)): 0)
        .build();
    var pgPercentage = Double.valueOf(commandLine.getOptionValue(CLI_PG_SHORT_OPTION));
    var vgPercentage = commandLine.hasOption(CLI_VG_SHORT_OPTION) ? Double.valueOf(commandLine.getOptionValue(CLI_VG_SHORT_OPTION)) : 0;
    var waterPercentage = commandLine.hasOption(CLI_WATER_SHORT_OPTION) ? Double.valueOf(commandLine.getOptionValue(CLI_WATER_SHORT_OPTION)) : 0;

    Result result;
    result = calculator.calculateResult(amount, eLiquidBase, targetNicotine, pgPercentage, vgPercentage, waterPercentage);
    return resultVolumeWeightPercentageMapper.mapToResultVolumeWeightPercentage(result);
  }
}
