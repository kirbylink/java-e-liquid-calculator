package de.dddns.kirbylink.eliquidcalculator.calculator;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import de.dddns.kirbylink.eliquidcalculator.model.ELiquidBase;

class CalculatorTest {

  private Calculator calculator;

  @Test
  void testCalculateELiquidResult_WhenWithTraditionalMix_ThenResultIsReturned() {

    // Given
    calculator = new Calculator();
    var amount = 800;
    var eLiquidBase = ELiquidBase.builder().withNicotine(48).withPgPercentage(1).build();
    var targetNicotine = 6;
    var pgPercentage = 0.55;
    var vgPercentage = 0.35;
    var waterPercentage = 0.1;

    // When
    var result = calculator.calculateResult(amount, eLiquidBase, targetNicotine, pgPercentage, vgPercentage, waterPercentage);

    // Then
    assertThat(result.getEliquidBase().getAmount()).isEqualTo(100);
    assertThat(result.getPg()).isEqualTo(340);
    assertThat(result.getVg()).isEqualTo(280);
    assertThat(result.getWater()).isEqualTo(80);
  }

  @Test
  void testCalculateELiquidResult_WhenWithFiftyFiftyMix_ThenResultIsReturned() {

    // Given
    calculator = new Calculator();
    var amount = 800;
    var eLiquidBase = ELiquidBase.builder().withNicotine(48).withPgPercentage(1).build();
    var targetNicotine = 6;
    var pgPercentage = 0.5;
    var vgPercentage = 0.5;
    var waterPercentage = 0.0;

    // When
    var eLiquidResult = calculator.calculateResult(amount, eLiquidBase, targetNicotine, pgPercentage, vgPercentage, waterPercentage);

    // Then
    assertThat(eLiquidResult.getEliquidBase().getAmount()).isEqualTo(100);
    assertThat(eLiquidResult.getPg()).isEqualTo(300);
    assertThat(eLiquidResult.getVg()).isEqualTo(400);
    assertThat(eLiquidResult.getWater()).isZero();
  }

  @Test
  void testCalculateELiquidResult_WhenWithUnusualMix_ThenResultIsReturned() {

    // Given
    calculator = new Calculator();
    var amount = 800;
    var eLiquidBase = ELiquidBase.builder().withNicotine(45).withPgPercentage(0.5).withVgPercentage(0.5).build();
    var targetNicotine = 6;
    var pgPercentage = 0.55;
    var vgPercentage = 0.35;
    var waterPercentage = 0.10;

    // When
    var eLiquidResult = calculator.calculateResult(amount, eLiquidBase, targetNicotine, pgPercentage, vgPercentage, waterPercentage);

    // Then
    assertThat(eLiquidResult.getEliquidBase().getAmount()).isEqualTo(106.67);
    assertThat(eLiquidResult.getPg()).isEqualTo(386.67);
    assertThat(eLiquidResult.getVg()).isEqualTo(226.67);
    assertThat(eLiquidResult.getWater()).isEqualTo(80);
  }

  @Test
  void testCalculateELiquidResult_WhenWithFiftyFiftyMixAndNoNicotine_ThenResultIsReturned() {

    // Given
    calculator = new Calculator();
    var amount = 800;
    var eLiquidBase = ELiquidBase.builder().build();
    var targetNicotine = 0;
    var pgPercentage = 0.5;
    var vgPercentage = 0.5;
    var waterPercentage = 0.0;

    // When
    var eLiquidResult = calculator.calculateResult(amount, eLiquidBase, targetNicotine, pgPercentage, vgPercentage, waterPercentage);

    // Then
    assertThat(eLiquidResult.getEliquidBase().getAmount()).isZero();
    assertThat(eLiquidResult.getPg()).isEqualTo(400);
    assertThat(eLiquidResult.getVg()).isEqualTo(400);
    assertThat(eLiquidResult.getWater()).isZero();
  }

  @Test
  void testCalculateELiquidResult_WhenWithFiftyFiftyMixAndNoNicotineInFinishedMix_ThenResultIsReturned() {

    // Given
    calculator = new Calculator();
    var amount = 800;
    var eLiquidBase = ELiquidBase.builder().withNicotine(48).withPgPercentage(1).build();
    var targetNicotine = 0;
    var pgPercentage = 0.5;
    var vgPercentage = 0.5;
    var waterPercentage = 0.0;

    // When
    var eLiquidResult = calculator.calculateResult(amount, eLiquidBase, targetNicotine, pgPercentage, vgPercentage, waterPercentage);

    // Then
    assertThat(eLiquidResult.getEliquidBase().getAmount()).isZero();
    assertThat(eLiquidResult.getPg()).isEqualTo(400);
    assertThat(eLiquidResult.getVg()).isEqualTo(400);
    assertThat(eLiquidResult.getWater()).isZero();
  }

  @ParameterizedTest
  @CsvSource({"0,6", "6,48"})
  void testCalculateELiquidResult_WhenFiftyFiftyMixAndLessNicotineInBaseThanInTargetNicotine_ThenIllegalArgumentExceptionIsThrown(int baseLiquidNicotine, int targetNicotine) {

    // Given
    calculator = new Calculator();
    var amount = 800;
    var eLiquidBase = ELiquidBase.builder().withNicotine(baseLiquidNicotine).withPgPercentage(1).build();
    var pgPercentage = 0.5;
    var vgPercentage = 0.5;
    var waterPercentage = 0.0;
    var expectedExceptionMessage = "calculator.exception.illegal.argument.errormessage";

    // When
    var throwAbleMethod = catchThrowable(() -> {
      calculator.calculateResult(amount, eLiquidBase, targetNicotine, pgPercentage, vgPercentage, waterPercentage);
    });

    // Then
    assertThat(throwAbleMethod).isInstanceOf(IllegalArgumentException.class).hasMessageContaining(expectedExceptionMessage);
  }
}
