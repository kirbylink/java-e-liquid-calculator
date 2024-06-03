package de.dddns.kirbylink.eliquidcalculator.config;

import lombok.Getter;
import lombok.experimental.UtilityClass;

@Getter
@UtilityClass
public class CalculatorConfiguration {

  @UtilityClass
  public static class Density {
    public static final double PROPYLENE_GLYCOL = 1.036;
    public static final double GLYCEROL = 1.261;
    public static final double WATER = 1.0;
  }
}
