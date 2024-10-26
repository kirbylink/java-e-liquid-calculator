package de.dddns.kirbylink.eliquidcalculator.utility;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AboutInformationTest {

  @Mock
  private ClassLoader mockClassLoader;
  @InjectMocks
  private AboutInformation aboutInformation;

  @Test
  void testGetApplicationVersion_WhenPropertiesAlreadyLoaded_ThenVersionIsReturnedAndResourceIsNotLoadedAgain() {

    // Given
    var expectedVersion = "1.1.0";
    var inputStream = AboutInformationTest.class.getClassLoader().getResourceAsStream("version.properties");
    when(mockClassLoader.getResourceAsStream("version.properties")).thenReturn(inputStream);

    // When
    aboutInformation.getApplicationVersion();
    var actualVersion = aboutInformation.getApplicationVersion();

    // Then
    assertThat(actualVersion).isEqualTo(expectedVersion);
    verify(mockClassLoader, times(1)).getResourceAsStream("version.properties");
  }

  @Test
  void testGetApplicationVersion_WhenPropertiesExistsAndNoExceptionIsThrown_ThenVersionIsReturned() {

    // Given
    var expectedVersion = "1.1.0";
    var inputStream = AboutInformationTest.class.getClassLoader().getResourceAsStream("version.properties");
    when(mockClassLoader.getResourceAsStream("version.properties")).thenReturn(inputStream);

    // When
    var actualVersion = aboutInformation.getApplicationVersion();

    // Then
    assertThat(actualVersion).isEqualTo(expectedVersion);
  }

  @Test
  void testGetApplicationVersion_WhenPropertiesNotExistsAndNoExceptionIsThrown_ThenNoVersionIsReturned() {

    // Given
    var expectedVersion = "n/a";
    when(mockClassLoader.getResourceAsStream("version.properties")).thenReturn(null);

    // When
    var actualVersion = aboutInformation.getApplicationVersion();

    // Then
    assertThat(actualVersion).isEqualTo(expectedVersion);
  }
  
  @Test
  void testGetJavaInformation_WhenCalledTwice_ThenValueWillNotChange() {
    
    // Given
    var value = System.getProperty("java.version");
    
    // When
    aboutInformation.getJavaInformation();
    System.setProperty("java.version", "42");
    var actualJavaInformation = aboutInformation.getJavaInformation();
    
    // Then
    assertThat(actualJavaInformation).doesNotContain("42");
    
    System.setProperty("java.version", value);
  }
}
