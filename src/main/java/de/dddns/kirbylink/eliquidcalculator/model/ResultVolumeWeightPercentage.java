package de.dddns.kirbylink.eliquidcalculator.model;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(setterPrefix = "with")
public class ResultVolumeWeightPercentage {

  private final double baseLiquidVolume;
  private final double baseLiquidWeight;
  private final double baseLiquidPercentage;
  private final double pgVolume;
  private final double pgWeight;
  private final double pgPercentage;
  private final double vgVolume;
  private final double vgWeight;
  private final double vgPercentage;
  private final double waterVolume;
  private final double waterWeight;
  private final double waterPercentage;
}
