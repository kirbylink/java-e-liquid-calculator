package de.dddns.kirbylink.eliquidcalculator.utility;

import java.util.Properties;
import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class AboutInformation {

  private static final String KEY = "version";
  private static final String DEFAULT_VALUE = "n/a";
  private final Properties properties = new Properties();
  private final ClassLoader classLoader;
  private String javaInformation;

  public String getApplicationVersion() {
    if (properties.containsKey(KEY)) {
      return properties.getProperty(KEY);
    }
    try (var inputStream = classLoader.getResourceAsStream("version.properties")) {
      properties.load(inputStream);
      return properties.getProperty(KEY, DEFAULT_VALUE);
    } catch (Exception e) {
      log.error("Exception occured while loading version information", e);
      return DEFAULT_VALUE;
    }
  }

  public String getJavaInformation() {
    if (null == javaInformation) {
      javaInformation = String.format("""
          Java Version: %s
          Java Runtime Version: %s
          Java Vendor: %s
          Java Vendor Url: %s
          Java VM Name: %s
          Java VM Version: %s
          Java VM Vendor: %s
          """,
          System.getProperty("java.version"),
          System.getProperty("java.runtime.version"),
          System.getProperty("java.vendor"),
          System.getProperty("java.vendor.url"),
          System.getProperty("java.vm.name"),
          System.getProperty("java.vm.version"),
          System.getProperty("java.vm.vendor"));
    }
    return javaInformation;
  }
}

