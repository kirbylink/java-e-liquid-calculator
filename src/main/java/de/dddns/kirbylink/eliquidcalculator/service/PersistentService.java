package de.dddns.kirbylink.eliquidcalculator.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Properties;
import org.springframework.stereotype.Service;
import de.dddns.kirbylink.eliquidcalculator.config.PersistentConfiguration;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class PersistentService {

  private final PersistentConfiguration persistentConfiguration;

  public void saveValues(PersistentValues persistentValues) throws Exception {
    var properties = new Properties();
    properties.setProperty("base-nicotine", persistentValues.getBaseNicotine());
    properties.setProperty("base-pg", persistentValues.getBasePg());
    properties.setProperty("base-vg", persistentValues.getBaseVg());
    properties.setProperty("base-water", persistentValues.getBaseWater());
    properties.setProperty("target-nicotine", persistentValues.getTargetNicotine());
    properties.setProperty("target-pg", persistentValues.getTargetPg());
    properties.setProperty("target-vg", persistentValues.getTargetVg());
    properties.setProperty("target-water", persistentValues.getTargetWater());
    properties.setProperty("amount", persistentValues.getAmount());

    var directory = getApplicationDataDirectoryFile();
    if (!directory.exists()) {
      log.debug("Create directory {}", directory.getAbsolutePath());
      directory.mkdirs();
    }

    var file = new File(directory.getAbsolutePath() + File.separator + "default_values.properties");
    try (var output = getFileOutputStream(file)) {
      properties.store(output, "Default E-Liquid Values");
      log.debug("Settings saved at {}", file);
    } catch (Exception e) {
      log.error("Could not save default values", e);
      throw e;
    }
  }

  public PersistentValues loadValues() {
    var properties = new Properties();
    var directoryPath = persistentConfiguration.getApplicationDataDirectory();
    var file = new File(directoryPath + File.separator + "default_values.properties");

    if (file.exists()) {
      try (var input = getFileInputStream(file)) {
        properties.load(input);

        var persistendValues = PersistentValues.builder()
            .withBaseNicotine(properties.getProperty("base-nicotine", "0"))
            .withBasePg(properties.getProperty("base-pg", "0"))
            .withBaseVg(properties.getProperty("base-vg", "0"))
            .withBaseWater(properties.getProperty("base-water", "0"))
            .withTargetNicotine(properties.getProperty("target-nicotine", "0"))
            .withTargetPg(properties.getProperty("target-pg", "0"))
            .withTargetVg(properties.getProperty("target-vg", "0"))
            .withTargetWater(properties.getProperty("target-water", "0"))
            .withAmount(properties.getProperty("amount", "0"))
            .build();
        log.debug("Settings loaded from {} with {}", file.getAbsolutePath(), persistendValues.toString());
        return persistendValues;
      } catch (Exception e) {
        log.warn("Error occured during loading of stored values", e);
      }
    }
    return PersistentValues.builder().build();
  }

  protected File getApplicationDataDirectoryFile() {
    var directoryPath = persistentConfiguration.getApplicationDataDirectory();
    return new File(directoryPath);
  }

  protected FileInputStream getFileInputStream(File file) throws FileNotFoundException {
    return new FileInputStream(file);
  }

  protected FileOutputStream getFileOutputStream(File file) throws FileNotFoundException {
    return new FileOutputStream(file);
  }

  @Getter
  @ToString
  @Builder(setterPrefix = "with")
  static class PersistentValues {
    @Default
    private final String baseNicotine = "0";
    @Default
    private final String basePg = "0";
    @Default
    private final String baseVg = "0";
    @Default
    private final String baseWater = "0";
    @Default
    private final String targetNicotine = "0";
    @Default
    private final String targetPg = "0";
    @Default
    private final String targetVg = "0";
    @Default
    private final String targetWater = "0";
    @Default
    private final String amount = "0";
  }
}
