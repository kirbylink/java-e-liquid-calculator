package de.dddns.kirbylink.eliquidcalculator.config;

import java.io.File;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PersistentConfiguration {
  public String getApplicationDataDirectory() {
    var oS = getOsName();
    var userHome = getUserHome();
    var appName = "e-liquid-calculator";

    if (oS.contains("win")) {
      return getAppData() + File.separator + appName;
    } else if (oS.contains("mac")) {
      return userHome + File.separator + "Library" + File.separator + "Application Support" + File.separator + appName;
    } else if (oS.contains("nix") || oS.contains("nux")) {
      return userHome + File.separator + ".config" + File.separator + appName;
    } else {
      return userHome + File.separator + appName;
    }
  }

  protected String getOsName() {
    return System.getProperty("os.name").toLowerCase();
  }

  protected String getUserHome() {
    return System.getProperty("user.home");
  }

  protected String getAppData() {
    return System.getenv("APPDATA");
  }
}
