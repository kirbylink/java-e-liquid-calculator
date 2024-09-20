package de.dddns.kirbylink.eliquidcalculator.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.awt.Color;
import java.awt.event.FocusListener;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Locale;
import java.util.stream.Stream;
import javax.swing.JFrame;
import javax.swing.border.LineBorder;
import org.assertj.swing.core.BasicRobot;
import org.assertj.swing.core.Robot;
import org.assertj.swing.fixture.FrameFixture;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import de.dddns.kirbylink.eliquidcalculator.ELiquidCalculatorApplication;
import de.dddns.kirbylink.eliquidcalculator.calculator.Calculator;
import de.dddns.kirbylink.eliquidcalculator.config.CommandLineConfiguration;
import de.dddns.kirbylink.eliquidcalculator.config.GuiConfiguration;
import de.dddns.kirbylink.eliquidcalculator.config.InternationalizationConfiguration;
import de.dddns.kirbylink.eliquidcalculator.converter.ResultVolumeWeightPercentageMapper;
import de.dddns.kirbylink.eliquidcalculator.service.PersistentService.PersistentValues;

@ExtendWith({SpringExtension.class, MockitoExtension.class})
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
@ContextConfiguration(classes = ELiquidCalculatorApplication.class)
class GuiServiceIntegrationTest {

  GuiService guiService;
  GuiService guiServiceSpy;

  @Autowired
  JFrame jFrame;
  @Autowired
  GuiConfiguration guiConfiguration;
  @Autowired
  InternationalizationConfiguration internationalizationConfiguration;
  @Autowired
  InternationalizationService internationalizationService;
  @Autowired
  Calculator calculator;
  @Autowired
  ResultVolumeWeightPercentageMapper resultVolumeWeightPercentageMapper;
  @Autowired
  CommandLineConfiguration commandLineConfiguration;
  @Autowired
  CommandLineService commandLineService;
  @Autowired
  FocusListener focusListener;
  @Mock
  PersistentService persistentService;
  @Mock
  File file;

  private Robot robot;
  private FrameFixture window;

  @BeforeEach
  void setUp() {
    guiService = new GuiService(jFrame, guiConfiguration, internationalizationConfiguration, internationalizationService, calculator, resultVolumeWeightPercentageMapper, commandLineService, commandLineConfiguration, focusListener, persistentService);
    guiServiceSpy = spy(guiService);
    robot = BasicRobot.robotWithCurrentAwtHierarchy();
    window = new FrameFixture(robot, jFrame);
  }

  @AfterEach
  void tearDown() {
    window.cleanUp();
  }

  @ParameterizedTest
  @MethodSource("provideArgumentsForComboBoxTestMethod")
  void testMain_WhenGuiStartedAndComboBoxLanguageSelectsAnItem_ThenItemListenerIsCalledAndLanguageIsChanged(Locale locale, String expectedButtonCalculateLocalizedText, String expectedButtonCalculateChangedLocalizedText,
      String expectedButtonSaveLocalizedText, String expectedButtonSaveChangedLocalizedText,
      boolean isButtonClick, String expectedLabelRequiredQuantityVgPercentResultLocalizedText, String expectedLabelRequiredQuantityVgPercentResultChangedText) throws BeansException, IOException {

    // Given
    internationalizationConfiguration.setLocale(locale);

    // When
    var args = new String[] {"-bn", "48", "-bp", "1", "-n", "6", "-p", "0.50", "-v", "0.50", "-a", "800", "-g"};
    guiServiceSpy.openWindow(args);
    var comboBox = window.comboBox().target();
    var buttonCalculate = window.button("buttonCalculate");
    var buttonSave = window.button("buttonSave");
    if (isButtonClick) {
      buttonCalculate.click();
    }
    var labelRequiredQuantityVgPercentResult = window.label("labelRequiredQuantityVgPercentResult");
    var buttonCalculateInitialText = buttonCalculate.text();
    var buttonSaveInitialText = buttonSave.text();
    var labelRequiredQuantityVgPercentResultInitialText = expectedLabelRequiredQuantityVgPercentResultLocalizedText;
    comboBox.setSelectedIndex(1);

    // Then
    verify(guiServiceSpy).updateLocalizedTextsOnComponents();
    assertThat(buttonCalculateInitialText).isEqualTo(expectedButtonCalculateLocalizedText);
    assertThat(buttonCalculate.text()).isEqualTo(expectedButtonCalculateChangedLocalizedText);
    assertThat(buttonSaveInitialText).isEqualTo(expectedButtonSaveLocalizedText);
    assertThat(buttonSave.text()).isEqualTo(expectedButtonSaveChangedLocalizedText);
    assertThat(labelRequiredQuantityVgPercentResultInitialText).isEqualTo(expectedLabelRequiredQuantityVgPercentResultLocalizedText);
    assertThat(labelRequiredQuantityVgPercentResult.text()).isEqualTo(expectedLabelRequiredQuantityVgPercentResultChangedText);
  }

  private static Stream<Arguments> provideArgumentsForComboBoxTestMethod() {
    return Stream.of(
        Arguments.of(Locale.GERMAN, "Berechnen", "Calculate", "Speichern", "Save", true, "50,00", "50.00"),
        Arguments.of(Locale.GERMAN, "Berechnen", "Calculate", "Speichern", "Save", false, "0", "0"),
        Arguments.of(Locale.ENGLISH, "Calculate", "Berechnen", "Save", "Speichern", true, "50,00", "50,00"),
        Arguments.of(Locale.FRENCH, "Calculate", "Berechnen", "Save", "Speichern", true, "50.00", "50,00")
    );
}

  @ParameterizedTest
  @MethodSource("provideArgumentsForButtonTestMethod")
  void testMain_WhenGuiStartedWithTradidionalRequestedFinishedLiquidAndButtonIsClicked_CalculationIsStartedAndLabelsAreUpdated(Locale locale, String expectedBaseLiquidWeight, String expectedPgWeight, String expectedVgWeight, String waterWeight) throws BeansException, IOException {

    // Given
      internationalizationConfiguration.setLocale(locale);
      when(persistentService.loadValues()).thenReturn(PersistentValues.builder().build());

      // When
      guiServiceSpy.openWindow(new String[0]);
      fillTextFields();
      window.button("buttonCalculate").click();

      // Then
      verify(guiServiceSpy).calculateRequiredQuantity();
      assertThat(window.label("labelRequiredQuantityBaseLiquidWeightResult").text()).isEqualTo(expectedBaseLiquidWeight);
      assertThat(window.label("labelRequiredQuantityPgWeightResult").text()).isEqualTo(expectedPgWeight);
      assertThat(window.label("labelRequiredQuantityVgWeightResult").text()).isEqualTo(expectedVgWeight);
      assertThat(window.label("labelRequiredQuantityWaterWeightResult").text()).isEqualTo(waterWeight);
  }

  private static Stream<Arguments> provideArgumentsForButtonTestMethod() {
      return Stream.of(
          Arguments.of(Locale.GERMAN, "104,00", "352,00", "353,00", "80,00"),
          Arguments.of(Locale.ENGLISH, "104.00", "352.00", "353.00", "80.00")
      );
  }

  @ParameterizedTest
  @MethodSource("provideArgumentsForArgumentTestMethod")
  void testMain_WhenGuiStartedWithArguments_ThenFieldsAreSetWithGivenArguments(String[] args, String expectedBaseLiquidNicotine, String expectedBaseLiquidPgPercent, String expectedBaseLiquidVgPercent, String expectedBaseLiquidWaterPercent, String expectedTargetNicotine, String expectedTargetPg, String expectedTargetVg, String expectedTargetWater, String expectedAmount) throws BeansException, IOException {

    // Given

    // When
    guiServiceSpy.openWindow(args);

    // Then
    verifyTextFields(expectedBaseLiquidNicotine, expectedBaseLiquidPgPercent, expectedBaseLiquidVgPercent, expectedBaseLiquidWaterPercent, expectedTargetNicotine, expectedTargetPg, expectedTargetVg, expectedTargetWater, expectedAmount);
  }

  private static Stream<Arguments> provideArgumentsForArgumentTestMethod() {
    return Stream.of(
        Arguments.of(new String[]{"-bn", "48", "-bp", "1","-bv", "0", "-bw", "0", "-n", "6", "-p", "0.55", "-v", "0.35", "-w", "0.10", "-a", "800"}, "48", "100", "0", "0", "6", "55", "35", "10", "800"),
        Arguments.of(new String[]{"-bn", "0", "-bp", "0", "-bv", "0", "-bw", "0", "-n", "0", "-p", "0", "-v", "0", "-w", "0", "-a", "0"}, "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0"),
        Arguments.of(new String[]{"-bn", "48", "-bp", "0", "-bv", "0", "-bw", "0", "-n", "6", "-p", "0.55", "-v", "0", "-w", "0", "-a", "800"}, "48", "0", "0", "0", "6", "55", "0", "0", "800")
    );
  }

  private void verifyTextFields(String expectedBaseLiquidNicotine, String expectedBaseLiquidPgPercent, String expectedBaseLiquidVgPercent, String expectedBaseLiquidWaterPercent, String expectedTargetNicotine, String expectedTargetPg, String expectedTargetVg, String expectedTargetWater, String expectedAmount) {
    assertThat(window.textBox("textBasicMaterialsBaseLiquidNicotine").text()).isEqualTo(expectedBaseLiquidNicotine);
    assertThat(window.textBox("textBasicMaterialsBaseLiquidPg").text()).isEqualTo(expectedBaseLiquidPgPercent);
    assertThat(window.textBox("textBasicMaterialsBaseLiquidVg").text()).isEqualTo(expectedBaseLiquidVgPercent);
    assertThat(window.textBox("textBasicMaterialsBaseLiquidWater").text()).isEqualTo(expectedBaseLiquidWaterPercent);
    assertThat(window.textBox("textFinishedLiquidNicotine").text()).isEqualTo(expectedTargetNicotine);
    assertThat(window.textBox("textFinishedLiquidPg").text()).isEqualTo(expectedTargetPg);
    assertThat(window.textBox("textFinishedLiquidVg").text()).isEqualTo(expectedTargetVg);
    assertThat(window.textBox("textFinishedLiquidWater").text()).isEqualTo(expectedTargetWater);
    assertThat(window.textBox("textFinishedLiquidAmount").text()).isEqualTo(expectedAmount);
  }

  @Test
  void testMain_WhenGuiStartedAndEnabledTextFieldGainsFocus_ThenAllTextsIsSelected() throws BeansException, IOException {

    // Given
    when(persistentService.loadValues()).thenReturn(PersistentValues.builder().build());

    // When
    guiServiceSpy.openWindow(new String[0]);

    // Then
    testTextFieldSelection("textBasicMaterialsBaseLiquidNicotine");
    testTextFieldSelection("textBasicMaterialsBaseLiquidPg");
    testTextFieldSelection("textBasicMaterialsBaseLiquidVg");
    testTextFieldSelection("textBasicMaterialsBaseLiquidWater");
    testTextFieldSelection("textFinishedLiquidNicotine");
    testTextFieldSelection("textFinishedLiquidPg");
    testTextFieldSelection("textFinishedLiquidVg");
    testTextFieldSelection("textFinishedLiquidWater");
    testTextFieldSelection("textFinishedLiquidAmount");

    testNoSelection("textBasicMaterialsVgVg");
    testNoSelection("textBasicMaterialsVgWater");
  }

  private void testTextFieldSelection(String textBoxName) {
    var textBox = window.textBox(textBoxName).target();
    textBox.requestFocus();
    textBox.selectAll();
    robot.waitForIdle();
    textBox.getSelectedText().equals(textBox.getText());
  }

  private void testNoSelection(String textBoxName) {
    var textBox = window.textBox(textBoxName).target();
    textBox.requestFocus();
    assertThat(textBox.getSelectedText()).isNull();
  }

  @Test
  void testMain_WhenGuiStartedAndTextHasFocusAndEnterIsPressend_ThenCalculationIsStarted() throws BeansException, IOException {

    // Given
    when(persistentService.loadValues()).thenReturn(PersistentValues.builder().build());

    // When
    guiServiceSpy.openWindow(new String[0]);
    fillTextFields();

    window.textBox("textBasicMaterialsBaseLiquidNicotine").focus().pressAndReleaseKeys(java.awt.event.KeyEvent.VK_ENTER);
    window.textBox("textBasicMaterialsBaseLiquidPg").focus().pressAndReleaseKeys(java.awt.event.KeyEvent.VK_ENTER);
    window.textBox("textBasicMaterialsBaseLiquidVg").focus().pressAndReleaseKeys(java.awt.event.KeyEvent.VK_ENTER);
    window.textBox("textBasicMaterialsBaseLiquidWater").focus().pressAndReleaseKeys(java.awt.event.KeyEvent.VK_ENTER);
    window.textBox("textFinishedLiquidNicotine").focus().pressAndReleaseKeys(java.awt.event.KeyEvent.VK_ENTER);
    window.textBox("textFinishedLiquidPg").focus().pressAndReleaseKeys(java.awt.event.KeyEvent.VK_ENTER);
    window.textBox("textFinishedLiquidVg").focus().pressAndReleaseKeys(java.awt.event.KeyEvent.VK_ENTER);
    window.textBox("textFinishedLiquidWater").focus().pressAndReleaseKeys(java.awt.event.KeyEvent.VK_ENTER);
    window.textBox("textFinishedLiquidAmount").focus().pressAndReleaseKeys(java.awt.event.KeyEvent.VK_ENTER);

    window.textBox("textBasicMaterialsBaseLiquidNicotine").focus().pressAndReleaseKeys(java.awt.event.KeyEvent.VK_NUMPAD1);

    // Then
    verify(guiServiceSpy, times(9)).calculateRequiredQuantity();
  }

  private void fillTextFields() {
    window.textBox("textBasicMaterialsBaseLiquidNicotine").setText("48");
    window.textBox("textBasicMaterialsBaseLiquidPg").setText("100");
    window.textBox("textBasicMaterialsBaseLiquidVg").setText("0");
    window.textBox("textBasicMaterialsBaseLiquidWater").setText("0");
    window.textBox("textFinishedLiquidNicotine").setText("6");
    window.textBox("textFinishedLiquidPg").setText("55");
    window.textBox("textFinishedLiquidVg").setText("35");
    window.textBox("textFinishedLiquidWater").setText("10");
    window.textBox("textFinishedLiquidAmount").setText("800");
  }

  @Test
  void testMain_WhenGuiStartedWithTradidionalRequestedFinishedLiquidAndButtonSaveIsClicked_ThenValuesGetPersistet() throws BeansException, IOException {

    // Given
    internationalizationConfiguration.setLocale(Locale.GERMAN);
    when(persistentService.loadValues()).thenReturn(PersistentValues.builder().build());
    when(persistentService.getApplicationDataDirectoryFile()).thenReturn(file);
    when(file.getAbsolutePath()).thenReturn("target/");

    // When
    guiServiceSpy.openWindow(new String[0]);
    fillTextFields();
    window.button("buttonSave").click();

    // Then
    var title = "Werte gespeichert";
    verify(guiServiceSpy).saveValues();
    verify(guiServiceSpy).openDialog(anyString(), eq(title));
  }

  @Test
  void testMain_WhenGuiStartedWithTradidionalRequestedFinishedLiquidAndButtonSaveIsClickedWithException_ThenValuesAreNotPersistet() throws Exception {

    // Given
    internationalizationConfiguration.setLocale(Locale.GERMAN);
    doThrow(new IOException("IOException occured")).when(persistentService).saveValues(any(PersistentValues.class));
    when(persistentService.loadValues()).thenReturn(PersistentValues.builder().build());
    when(persistentService.getApplicationDataDirectoryFile()).thenReturn(file);
    when(file.getAbsolutePath()).thenReturn("target/");

    // When
    guiServiceSpy.openWindow(new String[0]);
    fillTextFields();
    window.button("buttonSave").click();

    // Then
    var title = "Speichern der Werte fehlgeschlagen";
    verify(guiServiceSpy).saveValues();
    verify(guiServiceSpy).openDialog(anyString(), eq(title));
  }

  @Nested
  @DisplayName("Test for failure handling")
  class TestsForFailureHandling {

    @ParameterizedTest(name = "{index}: Test with baseNicotine={0}, basePg={1}, baseVg={2}, baseWater={3}, targetNicotine={4}, targetPg={5}, targetVg={6}, targetWater={7}, amount={8}, expectedFields={9}")
    @MethodSource("provideTestData")
    void testMain_WhenGuiStartedAndButtonIsClicked_ThenFieldsAreMarkedAsWrong( String baseNicotine, String basePg, String baseVg, String baseWater, String targetNicotine, String targetPg, String targetVg, String targetWater, String amount, String componentType, Color expectedColor, String[] expectedFields) throws BeansException, IOException {

      // Given
      when(persistentService.loadValues()).thenReturn(PersistentValues.builder().build());

      // When
      guiServiceSpy.openWindow(new String[0]);
      fillTextFields(baseNicotine, basePg, baseVg, baseWater, targetNicotine, targetPg, targetVg, targetWater, amount);
      window.button("buttonCalculate").click();

      // Then
      verify(guiServiceSpy).calculateRequiredQuantity();
      verifyBorders(componentType, expectedColor, expectedFields);
    }

    static Stream<Arguments> provideTestData() {
      return Stream.of(
        Arguments.of("48", "50", "0", "0", "6", "50", "50", "0", "800", "JTextField", Color.RED, new String[]{"textBasicMaterialsBaseLiquidPg", "textBasicMaterialsBaseLiquidVg", "textBasicMaterialsBaseLiquidWater"}),
        Arguments.of("5", "100", "0", "0", "6", "50", "50", "0", "800", "JTextField", Color.RED, new String[]{"textBasicMaterialsBaseLiquidNicotine", "textFinishedLiquidNicotine"}),
        Arguments.of("48", "100", "0", "0", "6", "50", "0", "0", "800", "JTextField", Color.RED, new String[]{"textFinishedLiquidPg", "textFinishedLiquidVg", "textFinishedLiquidWater"}),
        Arguments.of("48", "100", "0", "0", "6", "50", "50", "0", "0", "JTextField", Color.RED, new String[]{"textFinishedLiquidAmount"}),
        Arguments.of("48", "100", "0", "0", "6", "0", "0", "0", "0", "JTextField", Color.RED, new String[]{"textFinishedLiquidPg", "textFinishedLiquidVg", "textFinishedLiquidWater", "textFinishedLiquidAmount"}),
        Arguments.of("48", "50", "50", "0", "6", "0", "100", "0", "800", "JLabel", Color.RED, new String[]{"labelRequiredQuantityPgVolumeResult", "labelRequiredQuantityPgWeightResult", "labelRequiredQuantityPgPercentResult"}),
        Arguments.of("48", "50", "50", "0", "6", "100", "0", "0", "800", "JLabel", Color.RED, new String[]{"labelRequiredQuantityVgVolumeResult", "labelRequiredQuantityVgWeightResult", "labelRequiredQuantityVgPercentResult"}),
        Arguments.of("48", "0", "50", "50", "6", "50", "50", "0", "800", "JLabel", Color.RED, new String[]{"labelRequiredQuantityWaterVolumeResult", "labelRequiredQuantityWaterWeightResult", "labelRequiredQuantityWaterPercentResult"})
      );
    }

    @Test
    void testMain_WhenGuiStartedAndTextFieldWithNonNumericValueIsFilledAndButtonIsClicked_ThenDialogWithNumberFormatExceptionIsCalled() throws BeansException, IOException {

      // Given
      when(persistentService.loadValues()).thenReturn(PersistentValues.builder().build());

      // When
      guiServiceSpy.openWindow(new String[0]);
      fillTextFields("48", "0", "50", "50", "6", "50", "50", "0", "n");
      window.button("buttonCalculate").click();

      // Then
      verify(guiServiceSpy).calculateRequiredQuantity();

      // Then
      verify(guiServiceSpy).calculateRequiredQuantity();
      verify(guiServiceSpy).openDialog(anyString(), eq("NumberFormatException"));
    }

    private void fillTextFields(String baseNicotine, String basePg, String baseVg, String baseWater, String targetNicotine, String targetPg, String targetVg, String targetWater, String amount) {
      window.textBox("textBasicMaterialsBaseLiquidNicotine").setText(baseNicotine);
      window.textBox("textBasicMaterialsBaseLiquidPg").setText(basePg);
      window.textBox("textBasicMaterialsBaseLiquidVg").setText(baseVg);
      window.textBox("textBasicMaterialsBaseLiquidWater").setText(baseWater);
      window.textBox("textFinishedLiquidNicotine").setText(targetNicotine);
      window.textBox("textFinishedLiquidPg").setText(targetPg);
      window.textBox("textFinishedLiquidVg").setText(targetVg);
      window.textBox("textFinishedLiquidWater").setText(targetWater);
      window.textBox("textFinishedLiquidAmount").setText(amount);
    }

    private void verifyBorders(String component, Color expectedColor, String... componentNames) {
      if ("JTextField".equals(component)) {
        Arrays.stream(componentNames).forEach(componentName -> assertThat(((LineBorder) window.textBox(componentName).target().getBorder()).getLineColor()).isEqualTo(expectedColor));
      } else if ("JLabel".equals(component)) {
        Arrays.stream(componentNames).forEach(componentName -> assertThat(((LineBorder) window.label(componentName).target().getBorder()).getLineColor()).isEqualTo(expectedColor));
      }
    }
  }
}
