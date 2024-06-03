package de.dddns.kirbylink.eliquidcalculator.converter;

import static de.dddns.kirbylink.eliquidcalculator.utility.Utility.round;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import de.dddns.kirbylink.eliquidcalculator.config.CalculatorConfiguration;
import de.dddns.kirbylink.eliquidcalculator.model.ELiquidBase;
import de.dddns.kirbylink.eliquidcalculator.model.Result;
import de.dddns.kirbylink.eliquidcalculator.model.ResultVolumeWeightPercentage;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

@Mapper(componentModel = "spring")
public abstract class ResultVolumeWeightPercentageMapper {
  @Mapping(target = "withBaseLiquidVolume", source = "eliquidBase.amount")
  @Mapping(target = "withBaseLiquidWeight", source = "eliquidBase", qualifiedByName = "convertToBaseLiquidWeightFromVolume")
  @Mapping(target = "withBaseLiquidPercentage", source = ".", qualifiedByName = "convertToBaseLiquidPercentage")
  @Mapping(target = "withPgVolume", source = "pg")
  @Mapping(target = "withPgWeight", source = "pg" ,qualifiedByName = "convertToPgWeightFromVolume")
  @Mapping(target = "withPgPercentage", source = ".", qualifiedByName = "convertToPgPercentage")
  @Mapping(target = "withVgVolume", source = "vg")
  @Mapping(target = "withVgWeight", source = "vg", qualifiedByName = "convertToVgWeightFromVolume")
  @Mapping(target = "withVgPercentage", source = ".", qualifiedByName = "convertToVgPercentage")
  @Mapping(target = "withWaterVolume", source = "water")
  @Mapping(target = "withWaterWeight", source = "water", qualifiedByName = "convertToWaterWeightFromVolume")
  @Mapping(target = "withWaterPercentage", source = ".", qualifiedByName = "convertToWaterPercentage")
  public abstract ResultVolumeWeightPercentage mapToResultVolumeWeightPercentage (Result result);

  @Named("convertToBaseLiquidPercentage")
  protected double convertToBaseLiquidPercentage(Result result) {
    return (result.getEliquidBase().getAmount() / result.getAmount()) * 100;
  }

  @Named("convertToPgPercentage")
  protected double convertToPgPercentage(Result result) {
    return (result.getPg() / result.getAmount()) * 100;
  }

  @Named("convertToVgPercentage")
  protected double convertToVgPercentage(Result result) {
    return (result.getVg() / result.getAmount()) * 100;
  }

  @Named("convertToWaterPercentage")
  protected double convertToWaterPercentage(Result result) {
    return (result.getWater() / result.getAmount()) * 100;
  }

  @Named("convertToBaseLiquidWeightFromVolume")
  protected int convertToBaseLiquidWeightFromVolume(ELiquidBase eLiquidBase) {
    return convertToPgWeightFromVolume(eLiquidBase.getAmount() * eLiquidBase.getPgPercentage()) +
        convertToVgWeightFromVolume(eLiquidBase.getAmount() * eLiquidBase.getVgPercentage()) +
        convertToWaterWeightFromVolume(eLiquidBase.getAmount() * eLiquidBase.getWaterPercentage());
  }

  @Named("convertToPgWeightFromVolume")
  protected int convertToPgWeightFromVolume(double volumePg) {
    return convertWeightInGrammFromVolumeInMilliliter(volumePg, CalculatorConfiguration.Density.PROPYLENE_GLYCOL);
  }

  @Named("convertToVgWeightFromVolume")
  protected int convertToVgWeightFromVolume(double volumeVg) {
    return convertWeightInGrammFromVolumeInMilliliter(volumeVg, CalculatorConfiguration.Density.GLYCEROL);
  }

  @Named("convertToWaterWeightFromVolume")
  protected int convertToWaterWeightFromVolume(double volumeWater) {
    return convertWeightInGrammFromVolumeInMilliliter(volumeWater, CalculatorConfiguration.Density.WATER);
  }

  private int convertWeightInGrammFromVolumeInMilliliter(@PositiveOrZero double volume, @Positive double density) {
    return (int) round(volume * density, 0);
  }
}
