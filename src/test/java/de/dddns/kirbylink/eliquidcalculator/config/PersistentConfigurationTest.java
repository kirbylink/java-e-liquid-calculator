package de.dddns.kirbylink.eliquidcalculator.config;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Mockito.when;
import java.io.File;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PersistentConfigurationTest {

  @Mock
  private PersistentConfiguration persistentConfiguration;

  @Test
  void testGetApplicationDataDirectory_Windows() {
    // Given
    when(persistentConfiguration.getOsName()).thenReturn("windows 10");
    when(persistentConfiguration.getUserHome()).thenReturn("C:\\Users\\User");
    when(persistentConfiguration.getAppData()).thenReturn("C:\\Users\\User\\AppData\\Roaming");
    when(persistentConfiguration.getApplicationDataDirectory()).thenCallRealMethod();

    // When
    var actualPath = persistentConfiguration.getApplicationDataDirectory();

    // Then
    var expectedPath = "C:\\Users\\User\\AppData\\Roaming" + File.separator + "e-liquid-calculator";
    assertThat(actualPath).isEqualTo(expectedPath);
  }

  @ParameterizedTest(name = "{index}: Test with os.name={0}, user.home={1} and expected path={2}")
  @MethodSource("provideOsSpecificPaths")
  void testGetApplicationDataDirectory(String osName, String userHome, String expectedPath) {
    // Given
    when(persistentConfiguration.getOsName()).thenReturn(osName);
    when(persistentConfiguration.getUserHome()).thenReturn(userHome);
    when(persistentConfiguration.getApplicationDataDirectory()).thenCallRealMethod();

    // When
    var actualPath = persistentConfiguration.getApplicationDataDirectory();

    // Then
    assertThat(actualPath).isEqualTo(expectedPath);
  }

  private static Stream<Arguments> provideOsSpecificPaths() {
    return Stream.of(
        Arguments.of("mac os x", "/Users/User", "/Users/User/Library/Application Support/e-liquid-calculator"),
        Arguments.of("linux", "/home/user", "/home/user/.config/e-liquid-calculator"),
        Arguments.of("unix", "/home/user", "/home/user/.config/e-liquid-calculator"),
        Arguments.of("unknown os", "/home/user", "/home/user/e-liquid-calculator")
        );
  }

  @Test
  void testGetAppDataCall_WhenCalled_ThenNothingOrAValueIsReturned() {
    // Given
    when(persistentConfiguration.getAppData()).thenCallRealMethod();

    // When
    var throwAbleMethod = catchThrowable(() -> {
      persistentConfiguration.getAppData();
    });

    // Then
    assertThat(throwAbleMethod).isNull();
  }
}
