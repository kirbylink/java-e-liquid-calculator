package de.dddns.kirbylink.eliquidcalculator.calculator;

import static de.dddns.kirbylink.eliquidcalculator.utility.Utility.round;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;
import de.dddns.kirbylink.eliquidcalculator.model.ELiquidBase;
import de.dddns.kirbylink.eliquidcalculator.model.Result;
import de.dddns.kirbylink.eliquidcalculator.utility.ConsistentDataParametersSumOfPercentage;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

@Component
@Validated
public class Calculator {

  private static final String ILLEGAL_ARGUMENT_EXCEPTION_MESSAGE = "calculator.exception.illegal.argument.errormessage";

  @ConsistentDataParametersSumOfPercentage
  public Result calculateResult(@Positive int amount,
      @NotNull ELiquidBase eLiquidBase,
      @PositiveOrZero int targetNicotine,
      @Min(value = 0) @Max(value = 1) double pgPercentage,
      @Min(value = 0) @Max(value = 1) double vgPercentage,
      @Min(value = 0) @Max(value = 1) double waterPercentage) {

    Assert.isTrue(eLiquidBase.getNicotine() >= targetNicotine, ILLEGAL_ARGUMENT_EXCEPTION_MESSAGE);

    var dilutionFactor = eLiquidBase.getNicotine() > 0 && targetNicotine > 0 ? (double) eLiquidBase.getNicotine() / targetNicotine : 0;
    var baseAmount = dilutionFactor > 0 ? amount / dilutionFactor : 0;

    var eliquidBase = ELiquidBase.builder()
        .withAmount(dilutionFactor > 0 ? round(amount / dilutionFactor, 2) : 0)
        .withPgPercentage(eLiquidBase.getPgPercentage())
        .withVgPercentage(eLiquidBase.getVgPercentage())
        .withWaterPercentage(eLiquidBase.getWaterPercentage())
        .build();

    return Result.builder()
        .withAmount(amount)
        .withEliquidBase(eliquidBase)
        .withPg(round(amount * pgPercentage - baseAmount * eLiquidBase.getPgPercentage(), 2))
        .withVg(round(amount * vgPercentage - baseAmount * eLiquidBase.getVgPercentage(), 2))
        .withWater(round(amount * waterPercentage - baseAmount * eLiquidBase.getWaterPercentage(), 2))
        .build();
  }
}
