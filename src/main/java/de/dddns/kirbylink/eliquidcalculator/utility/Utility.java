package de.dddns.kirbylink.eliquidcalculator.utility;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import de.dddns.kirbylink.eliquidcalculator.exceptions.ValidationException;
import lombok.experimental.UtilityClass;

@UtilityClass
public class Utility {

  public static double round(double value, int places) {
    if (places < 0) {
      throw new ValidationException("utility.round.errormessage");
    }

    var bigDecimal = BigDecimal.valueOf(value);
    bigDecimal = bigDecimal.setScale(places, RoundingMode.HALF_UP);
    return bigDecimal.doubleValue();
  }

  public static boolean hasArgument(String[] args, String shortOption, String longOption) {
    return args != null && args.length != 0 && Arrays.stream(args).anyMatch(argument -> argument.contains(shortOption) || argument.contains(longOption));
  }
}
