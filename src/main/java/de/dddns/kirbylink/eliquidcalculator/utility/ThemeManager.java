package de.dddns.kirbylink.eliquidcalculator.utility;

import org.springframework.context.annotation.Configuration;
import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLightLaf;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class ThemeManager {

  public void applyTheme(boolean isDarkMode) {
    if (isDarkMode) {
      FlatDarkLaf.setup();
      log.debug("Dark mode applied");
    } else {
      FlatLightLaf.setup();
      log.debug("Light mode applied");
    }
  }
}
