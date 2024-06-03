package de.dddns.kirbylink.eliquidcalculator.model;

import de.dddns.kirbylink.eliquidcalculator.exceptions.ValidationException;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@Builder(setterPrefix = "with")
public class ELiquidBase {
  private final int nicotine;
  private final double pgPercentage;
  private final double vgPercentage;
  private final double waterPercentage;
  private final double amount;

  public static ELiquidBaseBuilder builder() {
    return new CustomELiquidBaseBuilder();
  }

  private static class CustomELiquidBaseBuilder extends ELiquidBaseBuilder {
    @Override
    public ELiquidBase build() {

      if (super.pgPercentage + super.vgPercentage + super.waterPercentage != 1.0 && super.nicotine != 0) {
        throw new ValidationException("eliquid.base.build.errormessage", super.pgPercentage, super.vgPercentage, super.waterPercentage);
      }

      return super.build();
    }
  }
}
