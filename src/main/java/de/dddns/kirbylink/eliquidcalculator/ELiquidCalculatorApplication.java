package de.dddns.kirbylink.eliquidcalculator;

import static de.dddns.kirbylink.eliquidcalculator.config.CommandLineConfiguration.CLI_GUI_LONG_OPTION;
import static de.dddns.kirbylink.eliquidcalculator.config.CommandLineConfiguration.CLI_GUI_SHORT_OPTION;
import static de.dddns.kirbylink.eliquidcalculator.utility.Utility.hasArgument;
import java.io.IOException;
import org.apache.commons.cli.ParseException;
import org.springframework.beans.BeansException;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import de.dddns.kirbylink.eliquidcalculator.service.ConsoleService;
import de.dddns.kirbylink.eliquidcalculator.service.GuiService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@ComponentScan("de.dddns.kirbylink.eliquidcalculator")
public class ELiquidCalculatorApplication {

  public static void main(String[] args) throws BeansException, ParseException, IOException {
    var isGui = args.length == 0 || hasArgument(args, CLI_GUI_SHORT_OPTION, CLI_GUI_LONG_OPTION);
    try (var annotationConfigApplicationContext = new AnnotationConfigApplicationContext(ELiquidCalculatorApplication.class)) {
      if (isGui) {
        var eliquidCalculatorGuiService = annotationConfigApplicationContext.getBean(GuiService.class);
        eliquidCalculatorGuiService.openWindow(args);
      } else {
        var eliquidCalculatorConsoleService = annotationConfigApplicationContext.getBean(ConsoleService.class);
        eliquidCalculatorConsoleService.printVolumeWeightAndPercentageOfRequiredQuantity(args);
      }
    }
  }
}
