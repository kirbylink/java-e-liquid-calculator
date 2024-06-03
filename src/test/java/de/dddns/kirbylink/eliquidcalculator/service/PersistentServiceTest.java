package de.dddns.kirbylink.eliquidcalculator.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import de.dddns.kirbylink.eliquidcalculator.config.PersistentConfiguration;

@ExtendWith(MockitoExtension.class)
class PersistentServiceTest {

  @TempDir
  File tempDir;

  @Mock
  private PersistentConfiguration persistentConfiguration;

  @Spy
  @InjectMocks
  private PersistentService persistentService;

  @Nested
  @DisplayName("Test for saveValues method")
  class TestsSaveValuesMethod {

    @Test
    void testSaveValues_WhenDirectoryExistsAndFileCanBeWritten_ThenPropertyFileIsCreated() throws Exception {
      // Given
      var persistentValues = PersistentService.PersistentValues.builder()
          .withBaseNicotine("48")
          .withBasePg("50")
          .withBaseVg("50")
          .withBaseWater("0")
          .withTargetNicotine("6")
          .withTargetPg("50")
          .withTargetVg("50")
          .withTargetWater("0")
          .withAmount("800")
          .build();

      var file = new File(tempDir.getAbsolutePath() + File.separator + "default_values.properties");

      when(persistentConfiguration.getApplicationDataDirectory()).thenReturn(tempDir.getAbsolutePath());

      // When
      persistentService.saveValues(persistentValues);

      // Then
      var properties = new Properties();
      try (var inputStream = new FileInputStream(file)) {
        properties.load(inputStream);
      }

      assertThat(properties.getProperty("base-nicotine")).isEqualTo("48");
      assertThat(properties.getProperty("base-pg")).isEqualTo("50");
      assertThat(properties.getProperty("base-vg")).isEqualTo("50");
      assertThat(properties.getProperty("base-water")).isEqualTo("0");
      assertThat(properties.getProperty("target-nicotine")).isEqualTo("6");
      assertThat(properties.getProperty("target-pg")).isEqualTo("50");
      assertThat(properties.getProperty("target-vg")).isEqualTo("50");
      assertThat(properties.getProperty("target-water")).isEqualTo("0");
      assertThat(properties.getProperty("amount")).isEqualTo("800");
    }

    @Test
    void testSaveValues_WhenDirectoryDoesNotExistsAndFileCanBeWritten_ThenPropertyFileIsCreated() throws Exception {
      // Given
      var persistentValues = PersistentService.PersistentValues.builder()
          .withBaseNicotine("48")
          .withBasePg("50")
          .withBaseVg("50")
          .withBaseWater("0")
          .withTargetNicotine("6")
          .withTargetPg("50")
          .withTargetVg("50")
          .withTargetWater("0")
          .withAmount("800")
          .build();

      var directoryPath = "src/test/resources/not-existing";
      var notExistingDirectory = new File(directoryPath);
      notExistingDirectory.deleteOnExit();
      var file = new File(directoryPath + File.separator + "default_values.properties");
      file.deleteOnExit();

      if (notExistingDirectory.exists()) {
        notExistingDirectory.delete();
      }
      if (file.exists()) {
        file.delete();
      }

      // Mocking methods
      when(persistentConfiguration.getApplicationDataDirectory()).thenReturn(directoryPath);

      // When
      persistentService.saveValues(persistentValues);

      // Then
      var properties = new Properties();
      try (var inputStream = new FileInputStream(file)) {
        properties.load(inputStream);
      }

      assertThat(properties.getProperty("base-nicotine")).isEqualTo("48");
      assertThat(properties.getProperty("base-pg")).isEqualTo("50");
      assertThat(properties.getProperty("base-vg")).isEqualTo("50");
      assertThat(properties.getProperty("base-water")).isEqualTo("0");
      assertThat(properties.getProperty("target-nicotine")).isEqualTo("6");
      assertThat(properties.getProperty("target-pg")).isEqualTo("50");
      assertThat(properties.getProperty("target-vg")).isEqualTo("50");
      assertThat(properties.getProperty("target-water")).isEqualTo("0");
      assertThat(properties.getProperty("amount")).isEqualTo("800");
    }

    @Test
    void testSaveValues_FileNotFoundExceptionOccurs_ThenNoPropertiesAreStored() throws Exception {
      // Given
      var persistentValues = PersistentService.PersistentValues.builder()
              .withBaseNicotine("48")
              .withBasePg("50")
              .withBaseVg("50")
              .withBaseWater("0")
              .withTargetNicotine("6")
              .withTargetPg("50")
              .withTargetVg("50")
              .withTargetWater("0")
              .withAmount("800")
              .build();

      var directoryPath = "src/test/resources/test";
      var directory = new File(directoryPath);
      directory.deleteOnExit();
      when(persistentConfiguration.getApplicationDataDirectory()).thenReturn(directoryPath);

      // Mock getFileOutputStream to throw IOException
      doThrow(FileNotFoundException.class).when(persistentService).getFileOutputStream(any(File.class));

      // When
      var throwAbleMethod = catchThrowable(() -> {
        persistentService.saveValues(persistentValues);
      });

      // Then
      assertThat(throwAbleMethod).isInstanceOf(FileNotFoundException.class);
      var file = new File(directoryPath + File.separator + "default_values.properties");
      assertThat(file).doesNotExist();
    }
  }

  @Nested
  @DisplayName("Test for loadValues method")
  class TestsLoadValuesMethod {

    @Test
    void testLoadValues_WhenFileExistsAndPropertiesLoaded_ThenPersistentValuesContainsValidValues() {
      // Given
      var directoryPath = "src/test/resources";
      when(persistentConfiguration.getApplicationDataDirectory()).thenReturn(directoryPath);

      // When
      var persistentValues = persistentService.loadValues();

      // Then
      assertThat(persistentValues.getBaseNicotine()).isEqualTo("48");
      assertThat(persistentValues.getBasePg()).isEqualTo("50");
      assertThat(persistentValues.getBaseVg()).isEqualTo("50");
      assertThat(persistentValues.getBaseWater()).isEqualTo("0");
      assertThat(persistentValues.getTargetNicotine()).isEqualTo("6");
      assertThat(persistentValues.getTargetPg()).isEqualTo("50");
      assertThat(persistentValues.getTargetVg()).isEqualTo("50");
      assertThat(persistentValues.getTargetWater()).isEqualTo("0");
      assertThat(persistentValues.getAmount()).isEqualTo("800");
    }

    @Test
    void testLoadValues_WhenFileDoesNotExist_ThenPersistentValuesContainsOnlyZeroValues() throws IOException {
      // Given
      var directoryPath = "invalid/path";

      when(persistentConfiguration.getApplicationDataDirectory()).thenReturn(directoryPath);

      // When
      var persistentValues = persistentService.loadValues();

      // Then
      assertThat(persistentValues.getBaseNicotine()).isEqualTo("0");
      assertThat(persistentValues.getBasePg()).isEqualTo("0");
      assertThat(persistentValues.getBaseVg()).isEqualTo("0");
      assertThat(persistentValues.getBaseWater()).isEqualTo("0");
      assertThat(persistentValues.getTargetNicotine()).isEqualTo("0");
      assertThat(persistentValues.getTargetPg()).isEqualTo("0");
      assertThat(persistentValues.getTargetVg()).isEqualTo("0");
      assertThat(persistentValues.getTargetWater()).isEqualTo("0");
      assertThat(persistentValues.getAmount()).isEqualTo("0");
    }

    @Test
    void testLoadValues_WhenSecurityExceptionOccurs_ThenPersistentValuesContainsOnlyZeroValues() throws IOException {
      // Given
      var directoryPath = "src/test/resources";

      when(persistentConfiguration.getApplicationDataDirectory()).thenReturn(directoryPath);

      doThrow(SecurityException .class).when(persistentService).getFileInputStream(any(File.class));

      // When
      var persistentValues = persistentService.loadValues();

      // Then
      assertThat(persistentValues.getBaseNicotine()).isEqualTo("0");
      assertThat(persistentValues.getBasePg()).isEqualTo("0");
      assertThat(persistentValues.getBaseVg()).isEqualTo("0");
      assertThat(persistentValues.getBaseWater()).isEqualTo("0");
      assertThat(persistentValues.getTargetNicotine()).isEqualTo("0");
      assertThat(persistentValues.getTargetPg()).isEqualTo("0");
      assertThat(persistentValues.getTargetVg()).isEqualTo("0");
      assertThat(persistentValues.getTargetWater()).isEqualTo("0");
      assertThat(persistentValues.getAmount()).isEqualTo("0");
    }
  }
}
