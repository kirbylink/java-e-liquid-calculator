package de.dddns.kirbylink.eliquidcalculator.service;

import static java.lang.String.format;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import de.dddns.kirbylink.eliquidcalculator.config.InternationalizationConfiguration;
import de.dddns.kirbylink.eliquidcalculator.model.ResultVolumeWeightPercentage;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InternationalizationService {

  private final MessageSource messageSource;
  private final InternationalizationConfiguration internationalizationConfiguration;

  public String getMessage(String property) {
    return messageSource.getMessage(property, null, internationalizationConfiguration.getLocale());
  }

  public String requiredQuantityOutput (ResultVolumeWeightPercentage resultVolumeWeightPercentage) {
    var requiredQuantityOutput = messageSource.getMessage("eliquid.result.console.output", null, internationalizationConfiguration.getLocale());
    return format(internationalizationConfiguration.getLocale(), requiredQuantityOutput, resultVolumeWeightPercentage.getBaseLiquidVolume(), resultVolumeWeightPercentage.getBaseLiquidWeight(), resultVolumeWeightPercentage.getBaseLiquidPercentage(),
        resultVolumeWeightPercentage.getPgVolume(), resultVolumeWeightPercentage.getPgWeight(), resultVolumeWeightPercentage.getPgPercentage(),
        resultVolumeWeightPercentage.getVgVolume(), resultVolumeWeightPercentage.getVgWeight(), resultVolumeWeightPercentage.getVgPercentage(),
        resultVolumeWeightPercentage.getWaterVolume(), resultVolumeWeightPercentage.getWaterWeight(), resultVolumeWeightPercentage.getWaterPercentage()
        );
  }

  public String fiveDigitNumberOutput(double value) {
    var output = messageSource.getMessage("eliquid.result.gui.output.five.digits", null, internationalizationConfiguration.getLocale());
    return format(internationalizationConfiguration.getLocale(), output, value);
  }
}
