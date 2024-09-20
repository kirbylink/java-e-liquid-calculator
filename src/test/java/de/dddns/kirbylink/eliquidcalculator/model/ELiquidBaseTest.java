package de.dddns.kirbylink.eliquidcalculator.model;

import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import java.text.DecimalFormat;
import java.util.Locale;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ELiquidBaseTest {

  private Locale originalDefaultLocale;

  @BeforeEach
  void setUp() {
    originalDefaultLocale = Locale.getDefault();
  }

  @AfterEach
  void tearDown() {
    Locale.setDefault(originalDefaultLocale);
  }

  @Test
  void testBuilder() {

    // Given
    var nicotine = 48;
    var pgPercent = 0.55;
    var vgPercent = 0.35;
    var waterPercent = 0.10;

    // When
    var eLiquidBase = ELiquidBase.builder().withNicotine(nicotine).withPgPercentage(pgPercent).withVgPercentage(vgPercent).withWaterPercentage(waterPercent).build();

    // Then
    assertThat(eLiquidBase.getNicotine()).isEqualTo(48);
    assertThat(eLiquidBase.getPgPercentage()).isEqualTo(0.55);
    assertThat(eLiquidBase.getVgPercentage()).isEqualTo(0.35);
    assertThat(eLiquidBase.getWaterPercentage()).isEqualTo(0.10);
  }

  @Test
  void testBuilder_WhenSumOfPercentIsNotEqualOne_ThenValidationExceptionIsThrown() {

    // Given
    Locale.setDefault(Locale.ENGLISH);
    var nicotine = 48;
    var pgPercent = 0.5;
    var vgPercent = 0.5;
    var waterPercent = 0.5;
    var decimalFormatVolume = new DecimalFormat("0.00");
    var errorMessage = "The sum of all percentages must be exactly 1.0 when setting the base liquid.\nCurrent values: PG = " + decimalFormatVolume.format(0.5) + ", VG = " + decimalFormatVolume.format(0.5) + ", Water = " + decimalFormatVolume.format(0.5);

    // When
    var throwAbleMethod = catchThrowable(() -> {
      ELiquidBase.builder().withNicotine(nicotine).withPgPercentage(pgPercent).withVgPercentage(vgPercent).withWaterPercentage(waterPercent).build();
    });

    // Then
    assertThat(throwAbleMethod).isInstanceOf(IllegalArgumentException.class).hasMessage(errorMessage);
  }

  @Test
  void testBuilder_WhenSumOfPercentAndNicotineIsZero_ThenValidationExceptionIsNotThrown() {

    Locale.setDefault(Locale.ENGLISH);
    var nicotine = 0;
    var pgPercent = 0;
    var vgPercent = 0;
    var waterPercent = 0;

    // When
    var eLiquidBase = ELiquidBase.builder().withNicotine(nicotine).withPgPercentage(pgPercent).withVgPercentage(vgPercent).withWaterPercentage(waterPercent).build();

    // Then
    assertThat(eLiquidBase.getNicotine()).isZero();
    assertThat(eLiquidBase.getPgPercentage()).isZero();
    assertThat(eLiquidBase.getVgPercentage()).isZero();
    assertThat(eLiquidBase.getWaterPercentage()).isZero();
  }
}
