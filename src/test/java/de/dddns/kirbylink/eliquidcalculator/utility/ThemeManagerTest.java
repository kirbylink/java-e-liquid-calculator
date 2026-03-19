package de.dddns.kirbylink.eliquidcalculator.utility;

import static org.assertj.core.api.Assertions.assertThatNoException;
import org.junit.jupiter.api.Test;

class ThemeManagerTest {

  @Test
  void applyTheme_shouldApplyDarkMode() {
    var themeManager = new ThemeManager();
    assertThatNoException().isThrownBy(() -> themeManager.applyTheme(true));
  }

  @Test
  void applyTheme_shouldApplyLightMode() {
    var themeManager = new ThemeManager();
    assertThatNoException().isThrownBy(() -> themeManager.applyTheme(false));
  }

  @Test
  void applyTheme_darkModeCanBeSwitchedToLight() {
    var themeManager = new ThemeManager();
    assertThatNoException().isThrownBy(() -> {
      themeManager.applyTheme(true);
      themeManager.applyTheme(false);
    });
  }

  @Test
  void applyTheme_lightModeCanBeSwitchedToDark() {
    var themeManager = new ThemeManager();
    assertThatNoException().isThrownBy(() -> {
      themeManager.applyTheme(false);
      themeManager.applyTheme(true);
    });
  }

  @Test
  void applyTheme_shouldHandleMultipleSwitches() {
    var themeManager = new ThemeManager();
    assertThatNoException().isThrownBy(() -> {
      for (var i = 0; i < 5; i++) {
        themeManager.applyTheme(i % 2 == 0);
      }
    });
  }
}
