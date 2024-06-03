package de.dddns.kirbylink.eliquidcalculator.converter;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import de.dddns.kirbylink.eliquidcalculator.model.ELiquidBase;
import de.dddns.kirbylink.eliquidcalculator.model.Result;

class ResultVolumeWeightPercentageMapperTest {

  ResultVolumeWeightPercentageMapper resultVolumeWeightPercentageMapper;

  @BeforeEach
  void setUp() {
    resultVolumeWeightPercentageMapper = new ResultVolumeWeightPercentageMapperImpl();
  }

  @Test
  void testMapToELiquidResultConsoleOutputWithPgBase() {

    // Given
    var eliquidBase = ELiquidBase.builder()
        .withPgPercentage(1)
        .withAmount(100)
        .build();
    var eLiquidResult = Result.builder()
        .withAmount(800)
        .withEliquidBase(eliquidBase)
        .withPg(300)
        .withVg(400)
        .build();

    // When
    var mapToELiquidResultConsoleOutput = resultVolumeWeightPercentageMapper.mapToResultVolumeWeightPercentage(eLiquidResult);

    // Then
    assertThat(mapToELiquidResultConsoleOutput.getBaseLiquidVolume()).isEqualTo(100);
    assertThat(mapToELiquidResultConsoleOutput.getBaseLiquidWeight()).isEqualTo(104);
    assertThat(mapToELiquidResultConsoleOutput.getBaseLiquidPercentage()).isEqualTo(12.5);
    assertThat(mapToELiquidResultConsoleOutput.getPgVolume()).isEqualTo(300);
    assertThat(mapToELiquidResultConsoleOutput.getPgWeight()).isEqualTo(311);
    assertThat(mapToELiquidResultConsoleOutput.getPgPercentage()).isEqualTo(37.5);
    assertThat(mapToELiquidResultConsoleOutput.getVgVolume()).isEqualTo(400);
    assertThat(mapToELiquidResultConsoleOutput.getVgWeight()).isEqualTo(504);
    assertThat(mapToELiquidResultConsoleOutput.getVgPercentage()).isEqualTo(50);
    assertThat(mapToELiquidResultConsoleOutput.getWaterVolume()).isZero();
    assertThat(mapToELiquidResultConsoleOutput.getWaterWeight()).isZero();
    assertThat(mapToELiquidResultConsoleOutput.getWaterPercentage()).isZero();
  }

  @ParameterizedTest
  @CsvSource({"100,104,300,311,400,504,100,100", "0,0,0,0,0,0,0,0"})
  void testConvertWeightInGrammFromVolumeInMilliliterWithPgBase(double volumeBase, int expectedBaseWeight, double volumePg, int expectedPgWeight, double volumeVg, int expectedVgWeight, double volumeWater, int expectedWaterWeight) {

    // Given
    var eliquidBase = ELiquidBase.builder()
        .withPgPercentage(1)
        .withAmount(volumeBase)
        .build();

    // When
    var actualBaseWeight = resultVolumeWeightPercentageMapper.convertToBaseLiquidWeightFromVolume(eliquidBase);
    var actualPgWeight = resultVolumeWeightPercentageMapper.convertToPgWeightFromVolume(volumePg);
    var actualVgWeight = resultVolumeWeightPercentageMapper.convertToVgWeightFromVolume(volumeVg);
    var actualWaterWeight = resultVolumeWeightPercentageMapper.convertToWaterWeightFromVolume(volumeWater);

    // Then
    assertThat(actualBaseWeight).isEqualTo(expectedBaseWeight);
    assertThat(actualPgWeight).isEqualTo(expectedPgWeight);
    assertThat(actualVgWeight).isEqualTo(expectedVgWeight);
    assertThat(actualWaterWeight).isEqualTo(expectedWaterWeight);
  }

  @ParameterizedTest
  @CsvSource({"100,115,390,404,230,290,80,80", "0,0,0,0,0,0,0,0"})
  void testConvertWeightInGrammFromVolumeInMilliliterWithFiftyFiftyBase(double volumeBase, int expectedBaseWeight, double volumePg, int expectedPgWeight, double volumeVg, int expectedVgWeight, double volumeWater, int expectedWaterWeight) {

    // Given
    var eliquidBase = ELiquidBase.builder()
        .withPgPercentage(0.5)
        .withVgPercentage(0.5)
        .withAmount(volumeBase)
        .build();

    // When
    var actualBaseWeight = resultVolumeWeightPercentageMapper.convertToBaseLiquidWeightFromVolume(eliquidBase);
    var actualPgWeight = resultVolumeWeightPercentageMapper.convertToPgWeightFromVolume(volumePg);
    var actualVgWeight = resultVolumeWeightPercentageMapper.convertToVgWeightFromVolume(volumeVg);
    var actualWaterWeight = resultVolumeWeightPercentageMapper.convertToWaterWeightFromVolume(volumeWater);

    // Then
    assertThat(actualBaseWeight).isEqualTo(expectedBaseWeight);
    assertThat(actualPgWeight).isEqualTo(expectedPgWeight);
    assertThat(actualVgWeight).isEqualTo(expectedVgWeight);
    assertThat(actualWaterWeight).isEqualTo(expectedWaterWeight);
  }

  @ParameterizedTest
  @CsvSource({"100,111,385,399,245,309,70,70", "0,0,0,0,0,0,0,0"})
  void testConvertWeightInGrammFromVolumeInMilliliterWithTraditionalBase(double volumeBase, int expectedBaseWeight, double volumePg, int expectedPgWeight, double volumeVg, int expectedVgWeight, double volumeWater, int expectedWaterWeight) {

    // Given
    var eliquidBase = ELiquidBase.builder()
        .withPgPercentage(0.55)
        .withVgPercentage(0.35)
        .withWaterPercentage(0.10)
        .withAmount(volumeBase)
        .build();

    // When
    var actualBaseWeight = resultVolumeWeightPercentageMapper.convertToBaseLiquidWeightFromVolume(eliquidBase);
    var actualPgWeight = resultVolumeWeightPercentageMapper.convertToPgWeightFromVolume(volumePg);
    var actualVgWeight = resultVolumeWeightPercentageMapper.convertToVgWeightFromVolume(volumeVg);
    var actualWaterWeight = resultVolumeWeightPercentageMapper.convertToWaterWeightFromVolume(volumeWater);

    // Then
    assertThat(actualBaseWeight).isEqualTo(expectedBaseWeight);
    assertThat(actualPgWeight).isEqualTo(expectedPgWeight);
    assertThat(actualVgWeight).isEqualTo(expectedVgWeight);
    assertThat(actualWaterWeight).isEqualTo(expectedWaterWeight);
  }

  @ParameterizedTest
  @CsvSource({"800,100,12.5,340,42.5,280,35.0,80,10", "800,100,12.5,300,37.5,400,50,0,0"})
  void testConvertToPercentagePgBase(int amount, double base, double expectedBasePercentage, double pg, double expectedPgPercentage,  double vg, double expectedVgPercentage, double water, double expectedWaterPercentage) {

    // Given
    var eliquidBase = ELiquidBase.builder()
        .withPgPercentage(1)
        .withAmount(100)
        .build();
    var eLiquidResult = Result.builder()
        .withAmount(amount)
        .withEliquidBase(eliquidBase)
        .withPg(pg)
        .withVg(vg)
        .withWater(water)
        .build();

    // When
    var actualBasePercentage = resultVolumeWeightPercentageMapper.convertToBaseLiquidPercentage(eLiquidResult);
    var actualPgPercentage = resultVolumeWeightPercentageMapper.convertToPgPercentage(eLiquidResult);
    var actualVgPercentage = resultVolumeWeightPercentageMapper.convertToVgPercentage(eLiquidResult);
    var actualWaterPercentage = resultVolumeWeightPercentageMapper.convertToWaterPercentage(eLiquidResult);

    // Then
    assertThat(actualBasePercentage).isEqualTo(expectedBasePercentage);
    assertThat(actualPgPercentage).isEqualTo(expectedPgPercentage);
    assertThat(actualVgPercentage).isEqualTo(expectedVgPercentage);
    assertThat(actualWaterPercentage).isEqualTo(expectedWaterPercentage);
  }
}
