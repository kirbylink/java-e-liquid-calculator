package de.dddns.kirbylink.eliquidcalculator.utility;

import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import java.util.Locale;
import java.util.stream.Stream;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import de.dddns.kirbylink.eliquidcalculator.exceptions.ValidationException;

class UtilityTest {

  private Locale originalDefaultLocale;

  @BeforeEach
  void setUp() {
    originalDefaultLocale = Locale.getDefault();
  }

  @AfterEach
  void tearDown() {
    Locale.setDefault(originalDefaultLocale);
  }

  @ParameterizedTest
  @CsvSource({"200.3456,2,200.35", "200.3456,3,200.346", "106.6666666667,2,106.67", "28786.079999999998,3,28786.08", "123.45,0,123", "123.51,0,124"})
  void testRound(double value, int places, double expectedDouble) {

    // Given

    // When
    var actualDouble = Utility.round(value, places);

    // Then
    assertThat(actualDouble).isEqualTo(expectedDouble);
  }

  @Test
  void testRound_WhenNumberOfDecimalPlaceIsLessThanOne_ThenValidationExceptionIsThrown() {

    // Given
    Locale.setDefault(Locale.ENGLISH);

    // When
    var throwAbleMethod = catchThrowable(() -> {
      Utility.round(1.2345, -2);
    });

    // Then
    assertThat(throwAbleMethod).isInstanceOf(ValidationException.class).hasMessage("Decimal places must be greater than or equal to 0");
  }

  @ParameterizedTest
  @MethodSource("provideArgumentsForHasArgumentMethod")
  void testHasArgument(String[] args, String shortOption, String longOption, boolean expectedHasArgument) {

    // Given

    // When
    var actualHasArgument = Utility.hasArgument(args, shortOption, longOption);

    // Then
    assertThat(actualHasArgument).isEqualTo(expectedHasArgument);
  }

  private static Stream<Arguments> provideArgumentsForHasArgumentMethod() {
    return Stream.of(
      Arguments.of(new String[]{"-a", "800", "-p", "1", "-n", "6", "-bn", "48", "-bp", "1", "-h"}, "-p", "--pg", true),
      Arguments.of(new String[]{"-a", "800", "-p", "1", "-n", "6", "-bn", "48", "-bp", "1", "-h"}, "-p", "--print", true),
      Arguments.of(new String[]{"-a", "800", "--pg", "1", "-n", "6", "-bn", "48", "-bp", "1", "-h"}, "-c", "--pg", true),
      Arguments.of(new String[]{"-a", "800", "-p", "1", "-n", "6", "-bn", "48", "-bp", "1", "-h"}, "-c", "--clear", false),
      Arguments.of(new String[0], "-p", "--pg", false)
    );
  }
}
