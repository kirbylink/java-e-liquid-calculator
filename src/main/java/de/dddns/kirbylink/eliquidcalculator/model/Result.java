package de.dddns.kirbylink.eliquidcalculator.model;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(setterPrefix = "with")
public class Result {
  private final int amount;
  private final ELiquidBase eliquidBase;
  private final int targetNicotine;
  private final double pg;
  private final double vg;
  private final double water;
}
