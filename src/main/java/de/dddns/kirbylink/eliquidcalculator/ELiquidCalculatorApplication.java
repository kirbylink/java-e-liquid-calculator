package de.dddns.kirbylink.eliquidcalculator;

import java.io.IOException;
import org.apache.commons.cli.ParseException;
import org.springframework.beans.BeansException;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import de.dddns.kirbylink.eliquidcalculator.service.ConsoleService;
import de.dddns.kirbylink.eliquidcalculator.service.GuiService;
import de.dddns.kirbylink.eliquidcalculator.utility.Utility;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@ComponentScan("de.dddns.kirbylink.eliquidcalculator")
public class ELiquidCalculatorApplication {

  public static void main(String[] args) throws BeansException, ParseException, IOException {
    var isGui = args.length == 0 || Utility.hasArgument(args, "-g", "--gui");
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
