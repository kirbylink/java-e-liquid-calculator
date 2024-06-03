package de.dddns.kirbylink.eliquidcalculator.service;

import static de.dddns.kirbylink.eliquidcalculator.config.CommandLineConfiguration.CLI_AMOUNT_SHORT_OPTION;
import static de.dddns.kirbylink.eliquidcalculator.config.CommandLineConfiguration.CLI_BASE_NICOTINE_SHORT_OPTION;
import static de.dddns.kirbylink.eliquidcalculator.config.CommandLineConfiguration.CLI_BASE_PG_SHORT_OPTION;
import static de.dddns.kirbylink.eliquidcalculator.config.CommandLineConfiguration.CLI_BASE_VG_SHORT_OPTION;
import static de.dddns.kirbylink.eliquidcalculator.config.CommandLineConfiguration.CLI_BASE_WATER_SHORT_OPTION;
import static de.dddns.kirbylink.eliquidcalculator.config.CommandLineConfiguration.CLI_NICOTINE_SHORT_OPTION;
import static de.dddns.kirbylink.eliquidcalculator.config.CommandLineConfiguration.CLI_PG_SHORT_OPTION;
import static de.dddns.kirbylink.eliquidcalculator.config.CommandLineConfiguration.CLI_VG_SHORT_OPTION;
import static de.dddns.kirbylink.eliquidcalculator.config.CommandLineConfiguration.CLI_WATER_SHORT_OPTION;
import java.awt.Color;
import java.awt.Dialog.ModalityType;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.KeyAdapter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Locale;
import java.util.Optional;
import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import org.apache.commons.cli.CommandLine;
import org.springframework.stereotype.Service;
import de.dddns.kirbylink.eliquidcalculator.calculator.Calculator;
import de.dddns.kirbylink.eliquidcalculator.config.CommandLineConfiguration;
import de.dddns.kirbylink.eliquidcalculator.config.GuiConfiguration;
import de.dddns.kirbylink.eliquidcalculator.config.InternationalizationConfiguration;
import de.dddns.kirbylink.eliquidcalculator.converter.ResultVolumeWeightPercentageMapper;
import de.dddns.kirbylink.eliquidcalculator.exceptions.ValidationException;
import de.dddns.kirbylink.eliquidcalculator.model.ELiquidBase;
import de.dddns.kirbylink.eliquidcalculator.model.ResultVolumeWeightPercentage;
import de.dddns.kirbylink.eliquidcalculator.service.PersistentService.PersistentValues;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class GuiService {

  private static final String GUI_COMBO_BOX_LANGUAGE_ENGLISH = "gui.combo.box.language.english";
  private static final String GUI_COMBO_BOX_LANGUAGE_GERMAN = "gui.combo.box.language.german";
  private static final String GUI_DIALOG_SAVE_FAILURE_MESSAGE= "gui.dialog.save.failure.message";
  private static final String GUI_DIALOG_SAVE_FAILURE_TITLE= "gui.dialog.save.failure.title";
  private static final String GUI_DIALOG_SAVE_SUCCESS_MESSAGE= "gui.dialog.save.success.message";
  private static final String GUI_DIALOG_SAVE_SUCCESS_TITLE= "gui.dialog.save.success.title";
  private static final String GUI_LABEL_PERCENT = "gui.label.percent";
  private static final String GUI_LABEL_WEIGHT = "gui.label.weight";
  private static final String GUI_LABEL_VOLUME = "gui.label.volume";
  private static final String GUI_LABEL_REQUIRED_QUANTITY = "gui.label.required.quantity";
  private static final String GUI_BUTTON_CALCULATE = "gui.button.calculate";
  private static final String GUI_BUTTON_SAVE = "gui.button.save";
  private static final String GUI_LABEL_FINISHED_LIQUID = "gui.label.finished.liquid";
  private static final String GUI_LABEL_AMOUNT = "gui.label.amount";
  private static final String GUI_LABEL_FINISHED_LIQUID_TARGET = "gui.label.finished.liquid.target";
  private static final String GUI_LABEL_BASE_LIQUID = "gui.label.base.liquid";
  private static final String GUI_LABEL_WATER = "gui.label.water";
  private static final String GUI_LABEL_NICOTINE = "gui.label.nicotine";
  private static final String GUI_LABEL_BASIC_SUBSTANCES = "gui.label.basic.substances";
  private static final String DEJA_VU_SANS = "DejaVu Sans";
  private static final String PG = "PG";
  private static final String VG = "VG";
  private static final String VOLUME_UNIT = "ml";
  private static final String WEIGHT_UNIT = "g";
  private static final String PERCENT_UNIT = "%";
  private static final String NICOTINE_CONCENTRATION = "mg/ml";

  private final JFrame jFrameEliquidCalculator;
  private final GuiConfiguration guiConfiguration;
  private final InternationalizationConfiguration internationalizationConfiguration;
  private final InternationalizationService internationalizationService;
  private final Calculator calculator;
  private final ResultVolumeWeightPercentageMapper resultVolumeWeightPercentageMapper;
  private final CommandLineService commandLineService;
  private final CommandLineConfiguration commandLineConfiguration;
  private final FocusListener focusListener;
  private final PersistentService persistentService;

  private KeyAdapter keyAdapter;
  private JLabel labelBasicMaterials;
  private JLabel labelNicotine;
  private JLabel labelBasicMaterialsWaterPercent;
  private JLabel labelBasicMaterialsBaseLiquid;
  private JLabel labelFinishedLiquid;
  private JLabel labelFinishedLiquidNicotineConcentration;
  private JLabel labelFinishedLiquidWaterPercent;
  private JLabel labelFinishedLiquidAmount;
  private JLabel labelFinishedLiquidFinishedLiquid;
  private JButton buttonCalculate;
  private JButton buttonSave;
  private JLabel labelRequiredQuantity;
  private JLabel labelRequiredQuantityVolumen;
  private JLabel labelRequiredQuantityWeight;
  private JLabel labelRequiredQuantityPercent;
  private JLabel labelRequiredQuantityBaseliquid;
  private JTextField textBasicMaterialsBaseLiquidNicotine;
  private JTextField textBasicMaterialsBaseLiquidPg;
  private JTextField textBasicMaterialsBaseLiquidVg;
  private JTextField textBasicMaterialsBaseLiquidWater;
  private JTextField textFinishedLiquidNicotine;
  private JTextField textFinishedLiquidPg;
  private JTextField textFinishedLiquidVg;
  private JTextField textFinishedLiquidWater;
  private JTextField textFinishedLiquidAmount;
  private JLabel labelRequiredQuantityBaseLiquidVolumeResult;
  private JLabel labelRequiredQuantityBaseLiquidWeightResult;
  private JLabel labelRequiredQuantityBaseLiquidPercentResult;
  private JLabel labelRequiredQuantityPgVolumeResult;
  private JLabel labelRequiredQuantityPgWeightResult;
  private JLabel labelRequiredQuantityPgPercentResult;
  private JLabel labelRequiredQuantityVgVolumeResult;
  private JLabel labelRequiredQuantityVgWeightResult;
  private JLabel labelRequiredQuantityVgPercentResult;
  private JLabel labelRequiredQuantityWaterVolumeResult;
  private JLabel labelRequiredQuantityWaterWeightResult;
  private JLabel labelRequiredQuantityWaterPercentResult;

  /**
   * @throws IOException
   * @wbp.parser.entryPoint
   */
  public void openWindow(String[] args) throws IOException {

    var options = commandLineConfiguration.getGuiOptions();

    var optionalCommandLine = Optional.ofNullable(commandLineService.getCommandLineFromArgumentsAndOptions(args, options));

    if (optionalCommandLine.isEmpty()) {
      return;
    }

    var commandLine = optionalCommandLine.get();

    /**
    * // Enable temporär for WindowBuilder
    * var jFrameEliquidCalculator = new JFrame();
    */

    jFrameEliquidCalculator.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    jFrameEliquidCalculator.setLocationByPlatform(true);
    jFrameEliquidCalculator.setSize(1024, 576);
    jFrameEliquidCalculator.setResizable(false);
    jFrameEliquidCalculator.getContentPane().setMinimumSize(new Dimension(2000, 0));
    jFrameEliquidCalculator.setTitle("E-Liquid-Calculator");
    var gridBagLayout = new GridBagLayout();
    gridBagLayout.columnWidths = new int[]{10, 150, 0, 50, 50, 20, 0, 50, 20, 0, 50, 20, 0, 50, 20, 0, 50, 20, 150, 10, 0};
    gridBagLayout.rowHeights = new int[]{60, 0, 0, 0, 0, 0, 15, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 31, 0};
    gridBagLayout.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 1.0, 0.0, 0.0, 1.0, 0.0, 0.0, 1.0, 0.0, 0.0, 1.0, 0.0, Double.MIN_VALUE};
    gridBagLayout.rowWeights = new double[]{1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
    jFrameEliquidCalculator.getContentPane().setLayout(gridBagLayout);
    keyAdapter = guiConfiguration.keyAdapter(this::calculateRequiredQuantity);

    var values = getValuesFromArgumentsOrProperty(args, commandLine);

    buildHeaderPartOfJFrame();

    buildInputPartOfJFrame(values);

    buildCalculatePartOfJFrame();

    buildResultPartOfJFrame();

    jFrameEliquidCalculator.setVisible(true);
    textBasicMaterialsBaseLiquidNicotine.requestFocus();

    log.debug("Gui successfully started.");
  }

  private void buildHeaderPartOfJFrame() throws IOException {
    var inputStream = this.getClass().getClassLoader().getResourceAsStream("images/e-liquid-calculator-banner.png");
    var labelBanner = new JLabel(new ImageIcon(inputStream.readAllBytes()));
    labelBanner.setName("labelBanner");
    var gridBagConstraintsLabelBanner = new GridBagConstraints();
    gridBagConstraintsLabelBanner.gridwidth = 16;
    gridBagConstraintsLabelBanner.insets = new Insets(0, 0, 5, 5);
    gridBagConstraintsLabelBanner.gridx = 2;
    gridBagConstraintsLabelBanner.gridy = 0;
    jFrameEliquidCalculator.getContentPane().add(labelBanner, gridBagConstraintsLabelBanner);

    var comboBoxLanguage = new JComboBox<String>();
    comboBoxLanguage.setName("comboBoxLanguage");
    comboBoxLanguage.addItemListener(itemEvent -> {
      log.debug("ComboBox listener {} called", itemEvent);
      if (itemEvent.getStateChange() == ItemEvent.SELECTED) {
        var itemString = itemEvent.getItem();
        if (itemString.equals("German") ^ itemString.equals("Deutsch")) {
          internationalizationConfiguration.setLocale(Locale.GERMAN);
        } else {
          internationalizationConfiguration.setLocale(Locale.ENGLISH);
        }
        updateLocalizedTextsOnComponents();
     }
    });
    String[] comboBoxLanguageItemArray;
    switch (internationalizationConfiguration.getLocale().getLanguage()) {
      case "de" -> comboBoxLanguageItemArray = new String[] {internationalizationService.getMessage(GUI_COMBO_BOX_LANGUAGE_GERMAN), internationalizationService.getMessage(GUI_COMBO_BOX_LANGUAGE_ENGLISH)};
      case "en" -> comboBoxLanguageItemArray = new String[] {internationalizationService.getMessage(GUI_COMBO_BOX_LANGUAGE_ENGLISH), internationalizationService.getMessage(GUI_COMBO_BOX_LANGUAGE_GERMAN)};
      default -> comboBoxLanguageItemArray = new String[] {internationalizationService.getMessage(GUI_COMBO_BOX_LANGUAGE_ENGLISH), internationalizationService.getMessage(GUI_COMBO_BOX_LANGUAGE_GERMAN)};
    }
    comboBoxLanguage.setModel(new DefaultComboBoxModel<>(comboBoxLanguageItemArray));
    comboBoxLanguage.addKeyListener(keyAdapter);
    var gridBagConstraintsComboBoxLanguage = new GridBagConstraints();
    gridBagConstraintsComboBoxLanguage.insets = new Insets(0, 0, 5, 5);
    gridBagConstraintsComboBoxLanguage.fill = GridBagConstraints.HORIZONTAL;
    gridBagConstraintsComboBoxLanguage.gridx = 18;
    gridBagConstraintsComboBoxLanguage.gridy = 0;
    jFrameEliquidCalculator.getContentPane().add(comboBoxLanguage, gridBagConstraintsComboBoxLanguage);
  }

  private void buildInputPartOfJFrame(PersistentValues values) {

    labelBasicMaterials = new JLabel(internationalizationService.getMessage(GUI_LABEL_BASIC_SUBSTANCES));
    labelBasicMaterials.setName("labelBasicMaterials");
    labelBasicMaterials.setFont(new Font(DEJA_VU_SANS, Font.BOLD, 14));
    labelBasicMaterials.setHorizontalAlignment(SwingConstants.LEFT);
    var gridBagConstraintsLabelBasicMaterials = new GridBagConstraints();
    gridBagConstraintsLabelBasicMaterials.anchor = GridBagConstraints.WEST;
    gridBagConstraintsLabelBasicMaterials.gridwidth = 3;
    gridBagConstraintsLabelBasicMaterials.insets = new Insets(0, 0, 5, 5);
    gridBagConstraintsLabelBasicMaterials.gridx = 2;
    gridBagConstraintsLabelBasicMaterials.gridy = 1;
    jFrameEliquidCalculator.getContentPane().add(labelBasicMaterials, gridBagConstraintsLabelBasicMaterials);

    labelNicotine = new JLabel(internationalizationService.getMessage(GUI_LABEL_NICOTINE));
    labelNicotine.setName("labelNicotine");
    var gridBagConstraintsBasicMaterialNikotinConcentration = new GridBagConstraints();
    gridBagConstraintsBasicMaterialNikotinConcentration.insets = new Insets(0, 0, 5, 5);
    gridBagConstraintsBasicMaterialNikotinConcentration.gridx = 4;
    gridBagConstraintsBasicMaterialNikotinConcentration.gridy = 2;
    jFrameEliquidCalculator.getContentPane().add(labelNicotine, gridBagConstraintsBasicMaterialNikotinConcentration);

    var labelBasicMaterialsPgPercent = new JLabel(PG);
    labelBasicMaterialsPgPercent.setName("labelBasicMaterialsPgPercent");
    var gridBagConstraintsLabelBasicMaterialsPgPercent = new GridBagConstraints();
    gridBagConstraintsLabelBasicMaterialsPgPercent.insets = new Insets(0, 0, 5, 5);
    gridBagConstraintsLabelBasicMaterialsPgPercent.gridx = 7;
    gridBagConstraintsLabelBasicMaterialsPgPercent.gridy = 2;
    jFrameEliquidCalculator.getContentPane().add(labelBasicMaterialsPgPercent, gridBagConstraintsLabelBasicMaterialsPgPercent);

    var labelBasicMaterialsVgPercent = new JLabel(VG);
    labelBasicMaterialsVgPercent.setName("labelBasicMaterialsVgPercent");
    var gridBagConstraintsLabelBasicMaterialsVgPercent = new GridBagConstraints();
    gridBagConstraintsLabelBasicMaterialsVgPercent.insets = new Insets(0, 0, 5, 5);
    gridBagConstraintsLabelBasicMaterialsVgPercent.gridx = 10;
    gridBagConstraintsLabelBasicMaterialsVgPercent.gridy = 2;
    jFrameEliquidCalculator.getContentPane().add(labelBasicMaterialsVgPercent, gridBagConstraintsLabelBasicMaterialsVgPercent);

    labelBasicMaterialsWaterPercent = new JLabel(internationalizationService.getMessage(GUI_LABEL_WATER));
    labelBasicMaterialsWaterPercent.setName("labelBasicMaterialsWaterPercent");
    var gridBagConstraintsLabelBasicMaterialsWaterPercent = new GridBagConstraints();
    gridBagConstraintsLabelBasicMaterialsWaterPercent.anchor = GridBagConstraints.WEST;
    gridBagConstraintsLabelBasicMaterialsWaterPercent.gridwidth = 2;
    gridBagConstraintsLabelBasicMaterialsWaterPercent.insets = new Insets(0, 0, 5, 5);
    gridBagConstraintsLabelBasicMaterialsWaterPercent.gridx = 13;
    gridBagConstraintsLabelBasicMaterialsWaterPercent.gridy = 2;
    jFrameEliquidCalculator.getContentPane().add(labelBasicMaterialsWaterPercent, gridBagConstraintsLabelBasicMaterialsWaterPercent);

    labelBasicMaterialsBaseLiquid = new JLabel(internationalizationService.getMessage(GUI_LABEL_BASE_LIQUID));
    labelBasicMaterialsBaseLiquid.setName("labelBasicMaterialsBaseLiquid");
    var gridBagConstraintsLabelBasicMaterialsBaseLiquid = new GridBagConstraints();
    gridBagConstraintsLabelBasicMaterialsBaseLiquid.anchor = GridBagConstraints.WEST;
    gridBagConstraintsLabelBasicMaterialsBaseLiquid.insets = new Insets(0, 0, 5, 5);
    gridBagConstraintsLabelBasicMaterialsBaseLiquid.gridx = 2;
    gridBagConstraintsLabelBasicMaterialsBaseLiquid.gridy = 3;
    jFrameEliquidCalculator.getContentPane().add(labelBasicMaterialsBaseLiquid, gridBagConstraintsLabelBasicMaterialsBaseLiquid);

    textBasicMaterialsBaseLiquidNicotine = new JTextField();
    textBasicMaterialsBaseLiquidNicotine.setName("textBasicMaterialsBaseLiquidNicotine");
    textBasicMaterialsBaseLiquidNicotine.addFocusListener(focusListener);
    textBasicMaterialsBaseLiquidNicotine.addKeyListener(keyAdapter);
    textBasicMaterialsBaseLiquidNicotine.setHorizontalAlignment(SwingConstants.RIGHT);
    textBasicMaterialsBaseLiquidNicotine.setText(String.valueOf(values.getBaseNicotine()));
    var gridBagConstraintsTextBasicMaterialsBaseLiquidNicotine = new GridBagConstraints();
    gridBagConstraintsTextBasicMaterialsBaseLiquidNicotine.insets = new Insets(0, 0, 5, 5);
    gridBagConstraintsTextBasicMaterialsBaseLiquidNicotine.fill = GridBagConstraints.HORIZONTAL;
    gridBagConstraintsTextBasicMaterialsBaseLiquidNicotine.gridx = 4;
    gridBagConstraintsTextBasicMaterialsBaseLiquidNicotine.gridy = 3;
    jFrameEliquidCalculator.getContentPane().add(textBasicMaterialsBaseLiquidNicotine, gridBagConstraintsTextBasicMaterialsBaseLiquidNicotine);
    textBasicMaterialsBaseLiquidNicotine.setColumns(10);

    var labelBasicMaterialsBaseNicotineUnit = new JLabel(NICOTINE_CONCENTRATION);
    labelBasicMaterialsBaseNicotineUnit.setName("labelBasicMaterialsBaseNicotineUnit");
    var gridBagConstraintsLabelBasicMaterialsBaseNicotineUnit = new GridBagConstraints();
    gridBagConstraintsLabelBasicMaterialsBaseNicotineUnit.anchor = GridBagConstraints.WEST;
    gridBagConstraintsLabelBasicMaterialsBaseNicotineUnit.insets = new Insets(0, 0, 5, 5);
    gridBagConstraintsLabelBasicMaterialsBaseNicotineUnit.gridx = 5;
    gridBagConstraintsLabelBasicMaterialsBaseNicotineUnit.gridy = 3;
    jFrameEliquidCalculator.getContentPane().add(labelBasicMaterialsBaseNicotineUnit, gridBagConstraintsLabelBasicMaterialsBaseNicotineUnit);

    textBasicMaterialsBaseLiquidPg = new JTextField();
    textBasicMaterialsBaseLiquidPg.setName("textBasicMaterialsBaseLiquidPg");
    textBasicMaterialsBaseLiquidPg.addFocusListener(focusListener);
    textBasicMaterialsBaseLiquidPg.addKeyListener(keyAdapter);
    textBasicMaterialsBaseLiquidPg.setHorizontalAlignment(SwingConstants.RIGHT);
    textBasicMaterialsBaseLiquidPg.setText(String.valueOf(values.getBasePg()));
    var gridBagConstraintsTextBasicMaterialsBaseLiquidPg = new GridBagConstraints();
    gridBagConstraintsTextBasicMaterialsBaseLiquidPg.insets = new Insets(0, 0, 5, 5);
    gridBagConstraintsTextBasicMaterialsBaseLiquidPg.fill = GridBagConstraints.HORIZONTAL;
    gridBagConstraintsTextBasicMaterialsBaseLiquidPg.gridx = 7;
    gridBagConstraintsTextBasicMaterialsBaseLiquidPg.gridy = 3;
    jFrameEliquidCalculator.getContentPane().add(textBasicMaterialsBaseLiquidPg, gridBagConstraintsTextBasicMaterialsBaseLiquidPg);
    textBasicMaterialsBaseLiquidPg.setColumns(10);

    var labelBasicMaterialsBaseLiquidPgPercentUnit = new JLabel(PERCENT_UNIT);
    labelBasicMaterialsBaseLiquidPgPercentUnit.setName("labelBasicMaterialsBaseLiquidPgPercentUnit");
    var gridBagConstraintsLabelBasicMaterialsBaseLiquidPgPercentUnit = new GridBagConstraints();
    gridBagConstraintsLabelBasicMaterialsBaseLiquidPgPercentUnit.anchor = GridBagConstraints.WEST;
    gridBagConstraintsLabelBasicMaterialsBaseLiquidPgPercentUnit.insets = new Insets(0, 0, 5, 5);
    gridBagConstraintsLabelBasicMaterialsBaseLiquidPgPercentUnit.gridx = 8;
    gridBagConstraintsLabelBasicMaterialsBaseLiquidPgPercentUnit.gridy = 3;
    jFrameEliquidCalculator.getContentPane().add(labelBasicMaterialsBaseLiquidPgPercentUnit, gridBagConstraintsLabelBasicMaterialsBaseLiquidPgPercentUnit);

    textBasicMaterialsBaseLiquidVg = new JTextField();
    textBasicMaterialsBaseLiquidVg.setName("textBasicMaterialsBaseLiquidVg");
    textBasicMaterialsBaseLiquidVg.addFocusListener(focusListener);
    textBasicMaterialsBaseLiquidVg.addKeyListener(keyAdapter);
    textBasicMaterialsBaseLiquidVg.setHorizontalAlignment(SwingConstants.RIGHT);
    textBasicMaterialsBaseLiquidVg.setText(String.valueOf(values.getBaseVg()));
    var gridBagConstraintsTextBasicMaterialsBaseLiquidVg = new GridBagConstraints();
    gridBagConstraintsTextBasicMaterialsBaseLiquidVg.insets = new Insets(0, 0, 5, 5);
    gridBagConstraintsTextBasicMaterialsBaseLiquidVg.fill = GridBagConstraints.HORIZONTAL;
    gridBagConstraintsTextBasicMaterialsBaseLiquidVg.gridx = 10;
    gridBagConstraintsTextBasicMaterialsBaseLiquidVg.gridy = 3;
    jFrameEliquidCalculator.getContentPane().add(textBasicMaterialsBaseLiquidVg, gridBagConstraintsTextBasicMaterialsBaseLiquidVg);
    textBasicMaterialsBaseLiquidVg.setColumns(10);

    var labelBasicMaterialsBaseLiquidVgPercentUnit = new JLabel(PERCENT_UNIT);
    labelBasicMaterialsBaseLiquidVgPercentUnit.setName("labelBasicMaterialsBaseLiquidVgPercentUnit");
    var gridBagConstraintsLabelBasicMaterialsBaseLiquidVgPercentUnit = new GridBagConstraints();
    gridBagConstraintsLabelBasicMaterialsBaseLiquidVgPercentUnit.anchor = GridBagConstraints.WEST;
    gridBagConstraintsLabelBasicMaterialsBaseLiquidVgPercentUnit.insets = new Insets(0, 0, 5, 5);
    gridBagConstraintsLabelBasicMaterialsBaseLiquidVgPercentUnit.gridx = 11;
    gridBagConstraintsLabelBasicMaterialsBaseLiquidVgPercentUnit.gridy = 3;
    jFrameEliquidCalculator.getContentPane().add(labelBasicMaterialsBaseLiquidVgPercentUnit, gridBagConstraintsLabelBasicMaterialsBaseLiquidVgPercentUnit);

    textBasicMaterialsBaseLiquidWater = new JTextField();
    textBasicMaterialsBaseLiquidWater.setName("textBasicMaterialsBaseLiquidWater");
    textBasicMaterialsBaseLiquidWater.addFocusListener(focusListener);
    textBasicMaterialsBaseLiquidWater.addKeyListener(keyAdapter);
    textBasicMaterialsBaseLiquidWater.setHorizontalAlignment(SwingConstants.RIGHT);
    textBasicMaterialsBaseLiquidWater.setText(String.valueOf(values.getBaseWater()));
    var gridBagConstraintsTextBasicMaterialsBaseLiquidWater = new GridBagConstraints();
    gridBagConstraintsTextBasicMaterialsBaseLiquidWater.insets = new Insets(0, 0, 5, 5);
    gridBagConstraintsTextBasicMaterialsBaseLiquidWater.fill = GridBagConstraints.HORIZONTAL;
    gridBagConstraintsTextBasicMaterialsBaseLiquidWater.gridx = 13;
    gridBagConstraintsTextBasicMaterialsBaseLiquidWater.gridy = 3;
    jFrameEliquidCalculator.getContentPane().add(textBasicMaterialsBaseLiquidWater, gridBagConstraintsTextBasicMaterialsBaseLiquidWater);
    textBasicMaterialsBaseLiquidWater.setColumns(10);

    var labelBasicMaterialsBaseLiquidWaterPercentUnit = new JLabel(PERCENT_UNIT);
    labelBasicMaterialsBaseLiquidWaterPercentUnit.setName("labelBasicMaterialsBaseLiquidWaterPercentUnit");
    var gridBagConstraintsLabelBasicMaterialsBaseLiquidWaterPercentUnit = new GridBagConstraints();
    gridBagConstraintsLabelBasicMaterialsBaseLiquidWaterPercentUnit.anchor = GridBagConstraints.WEST;
    gridBagConstraintsLabelBasicMaterialsBaseLiquidWaterPercentUnit.insets = new Insets(0, 0, 5, 5);
    gridBagConstraintsLabelBasicMaterialsBaseLiquidWaterPercentUnit.gridx = 14;
    gridBagConstraintsLabelBasicMaterialsBaseLiquidWaterPercentUnit.gridy = 3;
    jFrameEliquidCalculator.getContentPane().add(labelBasicMaterialsBaseLiquidWaterPercentUnit, gridBagConstraintsLabelBasicMaterialsBaseLiquidWaterPercentUnit);

    var labelBasicMaterialsPg = new JLabel(PG);
    labelBasicMaterialsPg.setName("labelBasicMaterialsPg");
    var gridBagConstraintsLabelBasicMaterialsPg = new GridBagConstraints();
    gridBagConstraintsLabelBasicMaterialsPg.anchor = GridBagConstraints.WEST;
    gridBagConstraintsLabelBasicMaterialsPg.insets = new Insets(0, 0, 5, 5);
    gridBagConstraintsLabelBasicMaterialsPg.gridx = 2;
    gridBagConstraintsLabelBasicMaterialsPg.gridy = 4;
    jFrameEliquidCalculator.getContentPane().add(labelBasicMaterialsPg, gridBagConstraintsLabelBasicMaterialsPg);

    var labelBasicMaterialsVg = new JLabel(VG);
    labelBasicMaterialsVg.setName("labelBasicMaterialsVg");
    var gridBagConstraintsLabelBasicMaterialsVg = new GridBagConstraints();
    gridBagConstraintsLabelBasicMaterialsVg.anchor = GridBagConstraints.WEST;
    gridBagConstraintsLabelBasicMaterialsVg.insets = new Insets(0, 0, 5, 5);
    gridBagConstraintsLabelBasicMaterialsVg.gridx = 2;
    gridBagConstraintsLabelBasicMaterialsVg.gridy = 5;
    jFrameEliquidCalculator.getContentPane().add(labelBasicMaterialsVg, gridBagConstraintsLabelBasicMaterialsVg);

    var textBasicMaterialsVgVg = new JTextField();
    textBasicMaterialsVgVg.setName("textBasicMaterialsVgVg");
    textBasicMaterialsVgVg.addFocusListener(focusListener);
    textBasicMaterialsVgVg.addKeyListener(keyAdapter);
    textBasicMaterialsVgVg.setEditable(false);
    textBasicMaterialsVgVg.setFocusable(false);
    textBasicMaterialsVgVg.setHorizontalAlignment(SwingConstants.RIGHT);
    textBasicMaterialsVgVg.setText("100");
    var gridBagConstraintsTextBasicMaterialsVgVg = new GridBagConstraints();
    gridBagConstraintsTextBasicMaterialsVgVg.insets = new Insets(0, 0, 5, 5);
    gridBagConstraintsTextBasicMaterialsVgVg.fill = GridBagConstraints.HORIZONTAL;
    gridBagConstraintsTextBasicMaterialsVgVg.gridx = 10;
    gridBagConstraintsTextBasicMaterialsVgVg.gridy = 5;
    jFrameEliquidCalculator.getContentPane().add(textBasicMaterialsVgVg, gridBagConstraintsTextBasicMaterialsVgVg);
    textBasicMaterialsVgVg.setColumns(10);

    var labelBasicMaterialsVgVgPercentUnit = new JLabel(PERCENT_UNIT);
    labelBasicMaterialsVgVgPercentUnit.setName("labelBasicMaterialsVgVgPercentUnit");
    var gridBagConstraintsLabelBasicMaterialsVgVgPercentUnit = new GridBagConstraints();
    gridBagConstraintsLabelBasicMaterialsVgVgPercentUnit.insets = new Insets(0, 0, 5, 5);
    gridBagConstraintsLabelBasicMaterialsVgVgPercentUnit.anchor = GridBagConstraints.WEST;
    gridBagConstraintsLabelBasicMaterialsVgVgPercentUnit.gridx = 11;
    gridBagConstraintsLabelBasicMaterialsVgVgPercentUnit.gridy = 5;
    jFrameEliquidCalculator.getContentPane().add(labelBasicMaterialsVgVgPercentUnit, gridBagConstraintsLabelBasicMaterialsVgVgPercentUnit);

    var textBasicMaterialsVgWater = new JTextField();
    textBasicMaterialsVgWater.setName("textBasicMaterialsVgWater");
    textBasicMaterialsVgWater.addFocusListener(focusListener);
    textBasicMaterialsVgWater.addKeyListener(keyAdapter);
    textBasicMaterialsVgWater.setEditable(false);
    textBasicMaterialsVgWater.setFocusable(false);
    textBasicMaterialsVgWater.setHorizontalAlignment(SwingConstants.RIGHT);
    textBasicMaterialsVgWater.setText("0");
    var gridBagConstraintsTextBasicMaterialsVgWater = new GridBagConstraints();
    gridBagConstraintsTextBasicMaterialsVgWater.insets = new Insets(0, 0, 5, 5);
    gridBagConstraintsTextBasicMaterialsVgWater.fill = GridBagConstraints.HORIZONTAL;
    gridBagConstraintsTextBasicMaterialsVgWater.gridx = 13;
    gridBagConstraintsTextBasicMaterialsVgWater.gridy = 5;
    jFrameEliquidCalculator.getContentPane().add(textBasicMaterialsVgWater, gridBagConstraintsTextBasicMaterialsVgWater);
    textBasicMaterialsVgWater.setColumns(10);

    var labelBasicMaterialsVgWaterPercentUnit = new JLabel(PERCENT_UNIT);
    labelBasicMaterialsVgWaterPercentUnit.setName("labelBasicMaterialsVgWaterPercentUnit");
    var gridBagConstraintsLabelBasicMaterialsVgWaterPercentUnit = new GridBagConstraints();
    gridBagConstraintsLabelBasicMaterialsVgWaterPercentUnit.anchor = GridBagConstraints.WEST;
    gridBagConstraintsLabelBasicMaterialsVgWaterPercentUnit.insets = new Insets(0, 0, 5, 5);
    gridBagConstraintsLabelBasicMaterialsVgWaterPercentUnit.gridx = 14;
    gridBagConstraintsLabelBasicMaterialsVgWaterPercentUnit.gridy = 5;
    jFrameEliquidCalculator.getContentPane().add(labelBasicMaterialsVgWaterPercentUnit, gridBagConstraintsLabelBasicMaterialsVgWaterPercentUnit);

    var separatorBasicMaterials = new JSeparator();
    separatorBasicMaterials.setName("separatorBasicMaterials");
    separatorBasicMaterials.setPreferredSize(new Dimension(1024, 2));
    separatorBasicMaterials.setBackground(new Color(0, 0, 0));
    var gridBagConstraintsSeparatorBasicMaterials = new GridBagConstraints();
    gridBagConstraintsSeparatorBasicMaterials.gridwidth = 18;
    gridBagConstraintsSeparatorBasicMaterials.insets = new Insets(0, 0, 5, 5);
    gridBagConstraintsSeparatorBasicMaterials.gridx = 1;
    gridBagConstraintsSeparatorBasicMaterials.gridy = 6;
    jFrameEliquidCalculator.getContentPane().add(separatorBasicMaterials, gridBagConstraintsSeparatorBasicMaterials);

    labelFinishedLiquid = new JLabel(internationalizationService.getMessage(GUI_LABEL_FINISHED_LIQUID_TARGET));
    labelFinishedLiquid.setName("labelFinishedLiquid");
    labelFinishedLiquid.setFont(new Font(DEJA_VU_SANS, Font.BOLD, 14));
    var gridBagConstraintsLabelFinishedLiquid = new GridBagConstraints();
    gridBagConstraintsLabelFinishedLiquid.gridwidth = 3;
    gridBagConstraintsLabelFinishedLiquid.anchor = GridBagConstraints.WEST;
    gridBagConstraintsLabelFinishedLiquid.insets = new Insets(0, 0, 5, 5);
    gridBagConstraintsLabelFinishedLiquid.gridx = 2;
    gridBagConstraintsLabelFinishedLiquid.gridy = 7;
    jFrameEliquidCalculator.getContentPane().add(labelFinishedLiquid, gridBagConstraintsLabelFinishedLiquid);

    labelFinishedLiquidNicotineConcentration = new JLabel(internationalizationService.getMessage(GUI_LABEL_NICOTINE));
    labelFinishedLiquidNicotineConcentration.setName("labelFinishedLiquidNicotineConcentration");
    var gridBagConstraintsLabelFinishedLiquidNicotineConcentration = new GridBagConstraints();
    gridBagConstraintsLabelFinishedLiquidNicotineConcentration.insets = new Insets(0, 0, 5, 5);
    gridBagConstraintsLabelFinishedLiquidNicotineConcentration.gridx = 4;
    gridBagConstraintsLabelFinishedLiquidNicotineConcentration.gridy = 8;
    jFrameEliquidCalculator.getContentPane().add(labelFinishedLiquidNicotineConcentration, gridBagConstraintsLabelFinishedLiquidNicotineConcentration);

    var labelFinishedLiquidPgPercent = new JLabel(PG);
    labelFinishedLiquidPgPercent.setName("labelFinishedLiquidPgPercent");
    var gridBagConstraintsLabelFinishedLiquidPgPercent = new GridBagConstraints();
    gridBagConstraintsLabelFinishedLiquidPgPercent.insets = new Insets(0, 0, 5, 5);
    gridBagConstraintsLabelFinishedLiquidPgPercent.gridx = 7;
    gridBagConstraintsLabelFinishedLiquidPgPercent.gridy = 8;
    jFrameEliquidCalculator.getContentPane().add(labelFinishedLiquidPgPercent, gridBagConstraintsLabelFinishedLiquidPgPercent);

    var labelFinishedLiquidVgPercent = new JLabel(VG);
    labelFinishedLiquidVgPercent.setName("labelFinishedLiquidVgPercent");
    var gridBagConstraintsLabelFinishedLiquidVgPercent = new GridBagConstraints();
    gridBagConstraintsLabelFinishedLiquidVgPercent.insets = new Insets(0, 0, 5, 5);
    gridBagConstraintsLabelFinishedLiquidVgPercent.gridx = 10;
    gridBagConstraintsLabelFinishedLiquidVgPercent.gridy = 8;
    jFrameEliquidCalculator.getContentPane().add(labelFinishedLiquidVgPercent, gridBagConstraintsLabelFinishedLiquidVgPercent);

    labelFinishedLiquidWaterPercent = new JLabel(internationalizationService.getMessage(GUI_LABEL_WATER));
    labelFinishedLiquidWaterPercent.setName("labelFinishedLiquidWaterPercent");
    var gridBagConstraintsLabelFinishedLiquidWaterPercent = new GridBagConstraints();
    gridBagConstraintsLabelFinishedLiquidWaterPercent.anchor = GridBagConstraints.WEST;
    gridBagConstraintsLabelFinishedLiquidWaterPercent.gridwidth = 2;
    gridBagConstraintsLabelFinishedLiquidWaterPercent.insets = new Insets(0, 0, 5, 5);
    gridBagConstraintsLabelFinishedLiquidWaterPercent.gridx = 13;
    gridBagConstraintsLabelFinishedLiquidWaterPercent.gridy = 8;
    jFrameEliquidCalculator.getContentPane().add(labelFinishedLiquidWaterPercent, gridBagConstraintsLabelFinishedLiquidWaterPercent);

    labelFinishedLiquidAmount = new JLabel(internationalizationService.getMessage(GUI_LABEL_AMOUNT));
    labelFinishedLiquidAmount.setName("labelFinishedLiquidAmount");
    var gridBagConstraintsLabelFinishedLiquidAmount = new GridBagConstraints();
    gridBagConstraintsLabelFinishedLiquidAmount.insets = new Insets(0, 0, 5, 5);
    gridBagConstraintsLabelFinishedLiquidAmount.gridx = 16;
    gridBagConstraintsLabelFinishedLiquidAmount.gridy = 8;
    jFrameEliquidCalculator.getContentPane().add(labelFinishedLiquidAmount, gridBagConstraintsLabelFinishedLiquidAmount);

    labelFinishedLiquidFinishedLiquid = new JLabel(internationalizationService.getMessage(GUI_LABEL_FINISHED_LIQUID));
    labelFinishedLiquidFinishedLiquid.setName("labelFinishedLiquidFinishedLiquid");
    var gridBagConstraintsLabelFinishedLiquidFinishedLiquid = new GridBagConstraints();
    gridBagConstraintsLabelFinishedLiquidFinishedLiquid.anchor = GridBagConstraints.WEST;
    gridBagConstraintsLabelFinishedLiquidFinishedLiquid.insets = new Insets(0, 0, 5, 5);
    gridBagConstraintsLabelFinishedLiquidFinishedLiquid.gridx = 2;
    gridBagConstraintsLabelFinishedLiquidFinishedLiquid.gridy = 9;
    jFrameEliquidCalculator.getContentPane().add(labelFinishedLiquidFinishedLiquid, gridBagConstraintsLabelFinishedLiquidFinishedLiquid);

    textFinishedLiquidNicotine = new JTextField();
    textFinishedLiquidNicotine.setName("textFinishedLiquidNicotine");
    textFinishedLiquidNicotine.addFocusListener(focusListener);
    textFinishedLiquidNicotine.addKeyListener(keyAdapter);
    textFinishedLiquidNicotine.setHorizontalAlignment(SwingConstants.RIGHT);
    textFinishedLiquidNicotine.setText(String.valueOf(values.getTargetNicotine()));
    var gridBagConstraintsTextFinishedLiquidNicotine = new GridBagConstraints();
    gridBagConstraintsTextFinishedLiquidNicotine.insets = new Insets(0, 0, 5, 5);
    gridBagConstraintsTextFinishedLiquidNicotine.fill = GridBagConstraints.HORIZONTAL;
    gridBagConstraintsTextFinishedLiquidNicotine.gridx = 4;
    gridBagConstraintsTextFinishedLiquidNicotine.gridy = 9;
    jFrameEliquidCalculator.getContentPane().add(textFinishedLiquidNicotine, gridBagConstraintsTextFinishedLiquidNicotine);
    textFinishedLiquidNicotine.setColumns(10);

    var labelFinishedLiquidNicotineUnit = new JLabel(NICOTINE_CONCENTRATION);
    labelFinishedLiquidNicotineUnit.setName("labelFinishedLiquidNicotineUnit");
    var gridBagConstraintsLabelFinishedLiquidNicotineUnit = new GridBagConstraints();
    gridBagConstraintsLabelFinishedLiquidNicotineUnit.anchor = GridBagConstraints.WEST;
    gridBagConstraintsLabelFinishedLiquidNicotineUnit.insets = new Insets(0, 0, 5, 5);
    gridBagConstraintsLabelFinishedLiquidNicotineUnit.gridx = 5;
    gridBagConstraintsLabelFinishedLiquidNicotineUnit.gridy = 9;
    jFrameEliquidCalculator.getContentPane().add(labelFinishedLiquidNicotineUnit, gridBagConstraintsLabelFinishedLiquidNicotineUnit);

    textFinishedLiquidPg = new JTextField();
    textFinishedLiquidPg.setName("textFinishedLiquidPg");
    textFinishedLiquidPg.addFocusListener(focusListener);
    textFinishedLiquidPg.addKeyListener(keyAdapter);
    textFinishedLiquidPg.setHorizontalAlignment(SwingConstants.RIGHT);
    textFinishedLiquidPg.setText(String.valueOf(values.getTargetPg()));
    var gridBagConstraintsTextFinishedLiquidPg = new GridBagConstraints();
    gridBagConstraintsTextFinishedLiquidPg.insets = new Insets(0, 0, 5, 5);
    gridBagConstraintsTextFinishedLiquidPg.fill = GridBagConstraints.HORIZONTAL;
    gridBagConstraintsTextFinishedLiquidPg.gridx = 7;
    gridBagConstraintsTextFinishedLiquidPg.gridy = 9;
    jFrameEliquidCalculator.getContentPane().add(textFinishedLiquidPg, gridBagConstraintsTextFinishedLiquidPg);
    textFinishedLiquidPg.setColumns(10);

    var labelFinishedLiquidPgPercentUnit = new JLabel(PERCENT_UNIT);
    labelFinishedLiquidPgPercentUnit.setName("labelFinishedLiquidPgPercentUnit");
    var gridBagConstraintsLabelFinishedLiquidPgPercentUnit = new GridBagConstraints();
    gridBagConstraintsLabelFinishedLiquidPgPercentUnit.anchor = GridBagConstraints.WEST;
    gridBagConstraintsLabelFinishedLiquidPgPercentUnit.insets = new Insets(0, 0, 5, 5);
    gridBagConstraintsLabelFinishedLiquidPgPercentUnit.gridx = 8;
    gridBagConstraintsLabelFinishedLiquidPgPercentUnit.gridy = 9;
    jFrameEliquidCalculator.getContentPane().add(labelFinishedLiquidPgPercentUnit, gridBagConstraintsLabelFinishedLiquidPgPercentUnit);

    textFinishedLiquidVg = new JTextField();
    textFinishedLiquidVg.setName("textFinishedLiquidVg");
    textFinishedLiquidVg.addFocusListener(focusListener);
    textFinishedLiquidVg.addKeyListener(keyAdapter);
    textFinishedLiquidVg.setHorizontalAlignment(SwingConstants.RIGHT);
    textFinishedLiquidVg.setText(String.valueOf(values.getTargetVg()));
    var gridBagConstraintsTextFinishedLiquidVg = new GridBagConstraints();
    gridBagConstraintsTextFinishedLiquidVg.insets = new Insets(0, 0, 5, 5);
    gridBagConstraintsTextFinishedLiquidVg.fill = GridBagConstraints.HORIZONTAL;
    gridBagConstraintsTextFinishedLiquidVg.gridx = 10;
    gridBagConstraintsTextFinishedLiquidVg.gridy = 9;
    jFrameEliquidCalculator.getContentPane().add(textFinishedLiquidVg, gridBagConstraintsTextFinishedLiquidVg);
    textFinishedLiquidVg.setColumns(10);

    var labelFinishedLiquidVgPercentUnit = new JLabel(PERCENT_UNIT);
    labelFinishedLiquidVgPercentUnit.setName("labelFinishedLiquidVgPercentUnit");
    var gridBagConstraintsLabelFinishedLiquidVgPercentUnit = new GridBagConstraints();
    gridBagConstraintsLabelFinishedLiquidVgPercentUnit.anchor = GridBagConstraints.WEST;
    gridBagConstraintsLabelFinishedLiquidVgPercentUnit.insets = new Insets(0, 0, 5, 5);
    gridBagConstraintsLabelFinishedLiquidVgPercentUnit.gridx = 11;
    gridBagConstraintsLabelFinishedLiquidVgPercentUnit.gridy = 9;
    jFrameEliquidCalculator.getContentPane().add(labelFinishedLiquidVgPercentUnit, gridBagConstraintsLabelFinishedLiquidVgPercentUnit);

    textFinishedLiquidWater = new JTextField();
    textFinishedLiquidWater.setName("textFinishedLiquidWater");
    textFinishedLiquidWater.addFocusListener(focusListener);
    textFinishedLiquidWater.addKeyListener(keyAdapter);
    textFinishedLiquidWater.setHorizontalAlignment(SwingConstants.RIGHT);
    textFinishedLiquidWater.setText(String.valueOf(values.getTargetWater()));
    var gridBagConstraintsTextFinishedLiquidWater = new GridBagConstraints();
    gridBagConstraintsTextFinishedLiquidWater.insets = new Insets(0, 0, 5, 5);
    gridBagConstraintsTextFinishedLiquidWater.fill = GridBagConstraints.HORIZONTAL;
    gridBagConstraintsTextFinishedLiquidWater.gridx = 13;
    gridBagConstraintsTextFinishedLiquidWater.gridy = 9;
    jFrameEliquidCalculator.getContentPane().add(textFinishedLiquidWater, gridBagConstraintsTextFinishedLiquidWater);
    textFinishedLiquidWater.setColumns(10);

    var labelFinishedLiquidWaterPercentUnit = new JLabel(PERCENT_UNIT);
    labelFinishedLiquidWaterPercentUnit.setName("labelFinishedLiquidWaterPercentUnit");
    var gridBagConstraintsLabelFinishedLiquidWaterPercentUnit = new GridBagConstraints();
    gridBagConstraintsLabelFinishedLiquidWaterPercentUnit.anchor = GridBagConstraints.WEST;
    gridBagConstraintsLabelFinishedLiquidWaterPercentUnit.insets = new Insets(0, 0, 5, 5);
    gridBagConstraintsLabelFinishedLiquidWaterPercentUnit.gridx = 14;
    gridBagConstraintsLabelFinishedLiquidWaterPercentUnit.gridy = 9;
    jFrameEliquidCalculator.getContentPane().add(labelFinishedLiquidWaterPercentUnit, gridBagConstraintsLabelFinishedLiquidWaterPercentUnit);

    textFinishedLiquidAmount = new JTextField();
    textFinishedLiquidAmount.setName("textFinishedLiquidAmount");
    textFinishedLiquidAmount.addFocusListener(focusListener);
    textFinishedLiquidAmount.addKeyListener(keyAdapter);
    textFinishedLiquidAmount.setHorizontalAlignment(SwingConstants.RIGHT);
    textFinishedLiquidAmount.setText(String.valueOf(values.getAmount()));
    var gridBagConstraintsTextFinishedLiquidAmount = new GridBagConstraints();
    gridBagConstraintsTextFinishedLiquidAmount.insets = new Insets(0, 0, 5, 5);
    gridBagConstraintsTextFinishedLiquidAmount.fill = GridBagConstraints.HORIZONTAL;
    gridBagConstraintsTextFinishedLiquidAmount.gridx = 16;
    gridBagConstraintsTextFinishedLiquidAmount.gridy = 9;
    jFrameEliquidCalculator.getContentPane().add(textFinishedLiquidAmount, gridBagConstraintsTextFinishedLiquidAmount);
    textFinishedLiquidAmount.setColumns(10);

    var labelFinishedLiquidAmountUnit = new JLabel(VOLUME_UNIT);
    labelFinishedLiquidAmountUnit.setName("labelFinishedLiquidAmountUnit");
    var gridBagConstraintsLabelFinishedLiquidAmountUnit = new GridBagConstraints();
    gridBagConstraintsLabelFinishedLiquidAmountUnit.anchor = GridBagConstraints.WEST;
    gridBagConstraintsLabelFinishedLiquidAmountUnit.insets = new Insets(0, 0, 5, 5);
    gridBagConstraintsLabelFinishedLiquidAmountUnit.gridx = 17;
    gridBagConstraintsLabelFinishedLiquidAmountUnit.gridy = 9;
    jFrameEliquidCalculator.getContentPane().add(labelFinishedLiquidAmountUnit, gridBagConstraintsLabelFinishedLiquidAmountUnit);
  }

  private void buildCalculatePartOfJFrame() {
    var separatorFinishedLiquid = new JSeparator();
    separatorFinishedLiquid.setName("separatorFinishedLiquid");
    var gridBagConstraintsseparatorFinishedLiquid = new GridBagConstraints();
    gridBagConstraintsseparatorFinishedLiquid.gridwidth = 18;
    gridBagConstraintsseparatorFinishedLiquid.insets = new Insets(0, 0, 5, 5);
    gridBagConstraintsseparatorFinishedLiquid.gridx = 1;
    gridBagConstraintsseparatorFinishedLiquid.gridy = 11;
    jFrameEliquidCalculator.getContentPane().add(separatorFinishedLiquid, gridBagConstraintsseparatorFinishedLiquid);

    buttonCalculate = new JButton(internationalizationService.getMessage(GUI_BUTTON_CALCULATE));
    buttonCalculate.setName("buttonCalculate");
    buttonCalculate.addActionListener(e -> calculateRequiredQuantity());
    var gridBagConstraintsbuttonCalculate = new GridBagConstraints();
    gridBagConstraintsbuttonCalculate.anchor = GridBagConstraints.EAST;
    gridBagConstraintsbuttonCalculate.gridwidth = 3;
    gridBagConstraintsbuttonCalculate.insets = new Insets(0, 0, 5, 5);
    gridBagConstraintsbuttonCalculate.gridx = 15;
    gridBagConstraintsbuttonCalculate.gridy = 12;
    jFrameEliquidCalculator.getContentPane().add(buttonCalculate, gridBagConstraintsbuttonCalculate);

    buttonSave = new JButton(internationalizationService.getMessage(GUI_BUTTON_SAVE));
    buttonSave.addActionListener(e -> saveValues());
    buttonSave.setName("buttonSave");
    var gridBagConstraintsButtonSave = new GridBagConstraints();
    gridBagConstraintsButtonSave.insets = new Insets(0, 0, 5, 5);
    gridBagConstraintsButtonSave.gridx = 18;
    gridBagConstraintsButtonSave.gridy = 12;
    jFrameEliquidCalculator.getContentPane().add(buttonSave, gridBagConstraintsButtonSave);
  }

  private void buildResultPartOfJFrame() {
    var separatorFinishedLiquidCalculateButton = new JSeparator();
    separatorFinishedLiquidCalculateButton.setName("separatorFinishedLiquidCalculateButton");
    separatorFinishedLiquidCalculateButton.setPreferredSize(new Dimension(1024, 2));
    var gridBagConstraintsseparatorFinishedLiquidCalculateButton = new GridBagConstraints();
    gridBagConstraintsseparatorFinishedLiquidCalculateButton.gridwidth = 18;
    gridBagConstraintsseparatorFinishedLiquidCalculateButton.insets = new Insets(0, 0, 5, 5);
    gridBagConstraintsseparatorFinishedLiquidCalculateButton.gridx = 1;
    gridBagConstraintsseparatorFinishedLiquidCalculateButton.gridy = 13;
    jFrameEliquidCalculator.getContentPane().add(separatorFinishedLiquidCalculateButton, gridBagConstraintsseparatorFinishedLiquidCalculateButton);

    labelRequiredQuantity = new JLabel(internationalizationService.getMessage(GUI_LABEL_REQUIRED_QUANTITY));
    labelRequiredQuantity.setName("labelRequiredQuantity");
    labelRequiredQuantity.setFont(new Font(DEJA_VU_SANS, Font.BOLD, 14));
    var gridBagConstraintsLabelRequiredQuantity = new GridBagConstraints();
    gridBagConstraintsLabelRequiredQuantity.gridwidth = 3;
    gridBagConstraintsLabelRequiredQuantity.anchor = GridBagConstraints.WEST;
    gridBagConstraintsLabelRequiredQuantity.insets = new Insets(0, 0, 5, 5);
    gridBagConstraintsLabelRequiredQuantity.gridx = 2;
    gridBagConstraintsLabelRequiredQuantity.gridy = 14;
    jFrameEliquidCalculator.getContentPane().add(labelRequiredQuantity, gridBagConstraintsLabelRequiredQuantity);

    labelRequiredQuantityVolumen = new JLabel(internationalizationService.getMessage(GUI_LABEL_VOLUME));
    labelRequiredQuantityVolumen.setName("labelRequiredQuantityVolumen");
    var gridBagConstraintsLabelRequiredQuantityVolumen = new GridBagConstraints();
    gridBagConstraintsLabelRequiredQuantityVolumen.gridwidth = 2;
    gridBagConstraintsLabelRequiredQuantityVolumen.insets = new Insets(0, 0, 5, 5);
    gridBagConstraintsLabelRequiredQuantityVolumen.gridx = 7;
    gridBagConstraintsLabelRequiredQuantityVolumen.gridy = 15;
    jFrameEliquidCalculator.getContentPane().add(labelRequiredQuantityVolumen, gridBagConstraintsLabelRequiredQuantityVolumen);

    labelRequiredQuantityWeight = new JLabel(internationalizationService.getMessage(GUI_LABEL_WEIGHT));
    labelRequiredQuantityWeight.setName("labelRequiredQuantityWeight");
    var gridBagConstraintsLabelRequiredQuantityWeight = new GridBagConstraints();
    gridBagConstraintsLabelRequiredQuantityWeight.gridwidth = 2;
    gridBagConstraintsLabelRequiredQuantityWeight.insets = new Insets(0, 0, 5, 5);
    gridBagConstraintsLabelRequiredQuantityWeight.gridx = 10;
    gridBagConstraintsLabelRequiredQuantityWeight.gridy = 15;
    jFrameEliquidCalculator.getContentPane().add(labelRequiredQuantityWeight, gridBagConstraintsLabelRequiredQuantityWeight);

    labelRequiredQuantityPercent = new JLabel(internationalizationService.getMessage(GUI_LABEL_PERCENT));
    labelRequiredQuantityPercent.setName("labelRequiredQuantityPercent");
    var gridBagConstraintsLabelRequiredQuantityPercent = new GridBagConstraints();
    gridBagConstraintsLabelRequiredQuantityPercent.gridwidth = 2;
    gridBagConstraintsLabelRequiredQuantityPercent.insets = new Insets(0, 0, 5, 5);
    gridBagConstraintsLabelRequiredQuantityPercent.gridx = 13;
    gridBagConstraintsLabelRequiredQuantityPercent.gridy = 15;
    jFrameEliquidCalculator.getContentPane().add(labelRequiredQuantityPercent, gridBagConstraintsLabelRequiredQuantityPercent);

    labelRequiredQuantityBaseliquid = new JLabel(internationalizationService.getMessage(GUI_LABEL_BASE_LIQUID));
    labelRequiredQuantityBaseliquid.setName("labelRequiredQuantityBaseliquid");
    var gridBagConstraintsLabelRequiredQuantityBaseliquid = new GridBagConstraints();
    gridBagConstraintsLabelRequiredQuantityBaseliquid.anchor = GridBagConstraints.WEST;
    gridBagConstraintsLabelRequiredQuantityBaseliquid.insets = new Insets(0, 0, 5, 5);
    gridBagConstraintsLabelRequiredQuantityBaseliquid.gridx = 2;
    gridBagConstraintsLabelRequiredQuantityBaseliquid.gridy = 16;
    jFrameEliquidCalculator.getContentPane().add(labelRequiredQuantityBaseliquid, gridBagConstraintsLabelRequiredQuantityBaseliquid);

    labelRequiredQuantityBaseLiquidVolumeResult = new JLabel("0");
    labelRequiredQuantityBaseLiquidVolumeResult.setName("labelRequiredQuantityBaseLiquidVolumeResult");
    var gridBagConstraintsLabelRequiredQuantityBaseLiquidVolumeResult = new GridBagConstraints();
    gridBagConstraintsLabelRequiredQuantityBaseLiquidVolumeResult.anchor = GridBagConstraints.EAST;
    gridBagConstraintsLabelRequiredQuantityBaseLiquidVolumeResult.insets = new Insets(0, 0, 5, 5);
    gridBagConstraintsLabelRequiredQuantityBaseLiquidVolumeResult.gridx = 7;
    gridBagConstraintsLabelRequiredQuantityBaseLiquidVolumeResult.gridy = 16;
    jFrameEliquidCalculator.getContentPane().add(labelRequiredQuantityBaseLiquidVolumeResult, gridBagConstraintsLabelRequiredQuantityBaseLiquidVolumeResult);

    var labelRequiredQuantityBaseLiquidVolumeUnit = new JLabel(VOLUME_UNIT);
    labelRequiredQuantityBaseLiquidVolumeUnit.setName("labelRequiredQuantityBaseLiquidVolumeUnit");
    var gridBagConstraintsLabelRequiredQuantityBaseLiquidVolumeUnit = new GridBagConstraints();
    gridBagConstraintsLabelRequiredQuantityBaseLiquidVolumeUnit.anchor = GridBagConstraints.WEST;
    gridBagConstraintsLabelRequiredQuantityBaseLiquidVolumeUnit.insets = new Insets(0, 0, 5, 5);
    gridBagConstraintsLabelRequiredQuantityBaseLiquidVolumeUnit.gridx = 8;
    gridBagConstraintsLabelRequiredQuantityBaseLiquidVolumeUnit.gridy = 16;
    jFrameEliquidCalculator.getContentPane().add(labelRequiredQuantityBaseLiquidVolumeUnit, gridBagConstraintsLabelRequiredQuantityBaseLiquidVolumeUnit);

    labelRequiredQuantityBaseLiquidWeightResult = new JLabel("0");
    labelRequiredQuantityBaseLiquidWeightResult.setName("labelRequiredQuantityBaseLiquidWeightResult");
    var gridBagConstraintsLabelRequiredQuantityBaseLiquidWeightResult = new GridBagConstraints();
    gridBagConstraintsLabelRequiredQuantityBaseLiquidWeightResult.anchor = GridBagConstraints.EAST;
    gridBagConstraintsLabelRequiredQuantityBaseLiquidWeightResult.insets = new Insets(0, 0, 5, 5);
    gridBagConstraintsLabelRequiredQuantityBaseLiquidWeightResult.gridx = 10;
    gridBagConstraintsLabelRequiredQuantityBaseLiquidWeightResult.gridy = 16;
    jFrameEliquidCalculator.getContentPane().add(labelRequiredQuantityBaseLiquidWeightResult, gridBagConstraintsLabelRequiredQuantityBaseLiquidWeightResult);

    var labelRequiredQuantityBaseLiquidWeightUnit = new JLabel(WEIGHT_UNIT);
    labelRequiredQuantityBaseLiquidWeightUnit.setName("labelRequiredQuantityBaseLiquidWeightUnit");
    var gridBagConstraintsLabelRequiredQuantityBaseLiquidWeightUnit = new GridBagConstraints();
    gridBagConstraintsLabelRequiredQuantityBaseLiquidWeightUnit.anchor = GridBagConstraints.WEST;
    gridBagConstraintsLabelRequiredQuantityBaseLiquidWeightUnit.insets = new Insets(0, 0, 5, 5);
    gridBagConstraintsLabelRequiredQuantityBaseLiquidWeightUnit.gridx = 11;
    gridBagConstraintsLabelRequiredQuantityBaseLiquidWeightUnit.gridy = 16;
    jFrameEliquidCalculator.getContentPane().add(labelRequiredQuantityBaseLiquidWeightUnit, gridBagConstraintsLabelRequiredQuantityBaseLiquidWeightUnit);

    labelRequiredQuantityBaseLiquidPercentResult = new JLabel("0");
    labelRequiredQuantityBaseLiquidPercentResult.setName("labelRequiredQuantityBaseLiquidPercentResult");
    var gridBagConstraintsLabelRequiredQuantityBaseLiquidPercentResult = new GridBagConstraints();
    gridBagConstraintsLabelRequiredQuantityBaseLiquidPercentResult.anchor = GridBagConstraints.EAST;
    gridBagConstraintsLabelRequiredQuantityBaseLiquidPercentResult.insets = new Insets(0, 0, 5, 5);
    gridBagConstraintsLabelRequiredQuantityBaseLiquidPercentResult.gridx = 13;
    gridBagConstraintsLabelRequiredQuantityBaseLiquidPercentResult.gridy = 16;
    jFrameEliquidCalculator.getContentPane().add(labelRequiredQuantityBaseLiquidPercentResult, gridBagConstraintsLabelRequiredQuantityBaseLiquidPercentResult);

    var labelRequiredQuantityBaseLiquidPercentageUnit = new JLabel(PERCENT_UNIT);
    labelRequiredQuantityBaseLiquidPercentageUnit.setName("labelRequiredQuantityBaseLiquidPercentageUnit");
    var gridBagConstraintsLabelRequiredQuantityBaseLiquidPercentageUnit = new GridBagConstraints();
    gridBagConstraintsLabelRequiredQuantityBaseLiquidPercentageUnit.anchor = GridBagConstraints.WEST;
    gridBagConstraintsLabelRequiredQuantityBaseLiquidPercentageUnit.insets = new Insets(0, 0, 5, 5);
    gridBagConstraintsLabelRequiredQuantityBaseLiquidPercentageUnit.gridx = 14;
    gridBagConstraintsLabelRequiredQuantityBaseLiquidPercentageUnit.gridy = 16;
    jFrameEliquidCalculator.getContentPane().add(labelRequiredQuantityBaseLiquidPercentageUnit, gridBagConstraintsLabelRequiredQuantityBaseLiquidPercentageUnit);

    var labelRequiredQuantityPg = new JLabel(PG);
    labelRequiredQuantityPg.setName("labelRequiredQuantityPg");
    var gridBagConstraintsLabelRequiredQuantityPg = new GridBagConstraints();
    gridBagConstraintsLabelRequiredQuantityPg.anchor = GridBagConstraints.WEST;
    gridBagConstraintsLabelRequiredQuantityPg.insets = new Insets(0, 0, 5, 5);
    gridBagConstraintsLabelRequiredQuantityPg.gridx = 2;
    gridBagConstraintsLabelRequiredQuantityPg.gridy = 17;
    jFrameEliquidCalculator.getContentPane().add(labelRequiredQuantityPg, gridBagConstraintsLabelRequiredQuantityPg);

    labelRequiredQuantityPgVolumeResult = new JLabel("0");
    labelRequiredQuantityPgVolumeResult.setName("labelRequiredQuantityPgVolumeResult");
    var gridBagConstraintsLabelRequiredQuantityPgVolumeResult = new GridBagConstraints();
    gridBagConstraintsLabelRequiredQuantityPgVolumeResult.anchor = GridBagConstraints.EAST;
    gridBagConstraintsLabelRequiredQuantityPgVolumeResult.insets = new Insets(0, 0, 5, 5);
    gridBagConstraintsLabelRequiredQuantityPgVolumeResult.gridx = 7;
    gridBagConstraintsLabelRequiredQuantityPgVolumeResult.gridy = 17;
    jFrameEliquidCalculator.getContentPane().add(labelRequiredQuantityPgVolumeResult, gridBagConstraintsLabelRequiredQuantityPgVolumeResult);

    var labelRequiredQuantityPgVolumeUnit = new JLabel(VOLUME_UNIT);
    labelRequiredQuantityPgVolumeUnit.setName("labelRequiredQuantityPgVolumeUnit");
    var gridBagConstraintsLabelRequiredQuantityPgVolumeUnit = new GridBagConstraints();
    gridBagConstraintsLabelRequiredQuantityPgVolumeUnit.anchor = GridBagConstraints.WEST;
    gridBagConstraintsLabelRequiredQuantityPgVolumeUnit.insets = new Insets(0, 0, 5, 5);
    gridBagConstraintsLabelRequiredQuantityPgVolumeUnit.gridx = 8;
    gridBagConstraintsLabelRequiredQuantityPgVolumeUnit.gridy = 17;
    jFrameEliquidCalculator.getContentPane().add(labelRequiredQuantityPgVolumeUnit, gridBagConstraintsLabelRequiredQuantityPgVolumeUnit);

    labelRequiredQuantityPgWeightResult = new JLabel("0");
    labelRequiredQuantityPgWeightResult.setName("labelRequiredQuantityPgWeightResult");
    var gridBagConstraintsLabelRequiredQuantityPgWeightResult = new GridBagConstraints();
    gridBagConstraintsLabelRequiredQuantityPgWeightResult.anchor = GridBagConstraints.EAST;
    gridBagConstraintsLabelRequiredQuantityPgWeightResult.insets = new Insets(0, 0, 5, 5);
    gridBagConstraintsLabelRequiredQuantityPgWeightResult.gridx = 10;
    gridBagConstraintsLabelRequiredQuantityPgWeightResult.gridy = 17;
    jFrameEliquidCalculator.getContentPane().add(labelRequiredQuantityPgWeightResult, gridBagConstraintsLabelRequiredQuantityPgWeightResult);

    var labelRequiredQuantityPgWeightUnit = new JLabel(WEIGHT_UNIT);
    labelRequiredQuantityPgWeightUnit.setName("labelRequiredQuantityPgWeightUnit");
    var gridBagConstraintsLabelRequiredQuantityPgWeightUnit = new GridBagConstraints();
    gridBagConstraintsLabelRequiredQuantityPgWeightUnit.anchor = GridBagConstraints.WEST;
    gridBagConstraintsLabelRequiredQuantityPgWeightUnit.insets = new Insets(0, 0, 5, 5);
    gridBagConstraintsLabelRequiredQuantityPgWeightUnit.gridx = 11;
    gridBagConstraintsLabelRequiredQuantityPgWeightUnit.gridy = 17;
    jFrameEliquidCalculator.getContentPane().add(labelRequiredQuantityPgWeightUnit, gridBagConstraintsLabelRequiredQuantityPgWeightUnit);

    labelRequiredQuantityPgPercentResult = new JLabel("0");
    labelRequiredQuantityPgPercentResult.setName("labelRequiredQuantityPgPercentResult");
    var gridBagConstraintsLabelRequiredQuantityPgPercentResult = new GridBagConstraints();
    gridBagConstraintsLabelRequiredQuantityPgPercentResult.anchor = GridBagConstraints.EAST;
    gridBagConstraintsLabelRequiredQuantityPgPercentResult.insets = new Insets(0, 0, 5, 5);
    gridBagConstraintsLabelRequiredQuantityPgPercentResult.gridx = 13;
    gridBagConstraintsLabelRequiredQuantityPgPercentResult.gridy = 17;
    jFrameEliquidCalculator.getContentPane().add(labelRequiredQuantityPgPercentResult, gridBagConstraintsLabelRequiredQuantityPgPercentResult);

    var labelRequiredQuantityPgPercentageUnit = new JLabel(PERCENT_UNIT);
    labelRequiredQuantityPgPercentageUnit.setName("labelRequiredQuantityPgPercentageUnit");
    var gridBagConstraintsLabelRequiredQuantityPgPercentageUnit = new GridBagConstraints();
    gridBagConstraintsLabelRequiredQuantityPgPercentageUnit.anchor = GridBagConstraints.WEST;
    gridBagConstraintsLabelRequiredQuantityPgPercentageUnit.insets = new Insets(0, 0, 5, 5);
    gridBagConstraintsLabelRequiredQuantityPgPercentageUnit.gridx = 14;
    gridBagConstraintsLabelRequiredQuantityPgPercentageUnit.gridy = 17;
    jFrameEliquidCalculator.getContentPane().add(labelRequiredQuantityPgPercentageUnit, gridBagConstraintsLabelRequiredQuantityPgPercentageUnit);

    var labelRequiredQuantityVg = new JLabel(VG);
    labelRequiredQuantityVg.setName("labelRequiredQuantityVg");
    var gridBagConstraintsLabelRequiredQuantityVg = new GridBagConstraints();
    gridBagConstraintsLabelRequiredQuantityVg.anchor = GridBagConstraints.WEST;
    gridBagConstraintsLabelRequiredQuantityVg.insets = new Insets(0, 0, 5, 5);
    gridBagConstraintsLabelRequiredQuantityVg.gridx = 2;
    gridBagConstraintsLabelRequiredQuantityVg.gridy = 18;
    jFrameEliquidCalculator.getContentPane().add(labelRequiredQuantityVg, gridBagConstraintsLabelRequiredQuantityVg);

    labelRequiredQuantityVgVolumeResult = new JLabel("0");
    labelRequiredQuantityVgVolumeResult.setName("labelRequiredQuantityVgVolumeResult");
    var gridBagConstraintsLabelRequiredQuantityVgVolumeResult = new GridBagConstraints();
    gridBagConstraintsLabelRequiredQuantityVgVolumeResult.anchor = GridBagConstraints.EAST;
    gridBagConstraintsLabelRequiredQuantityVgVolumeResult.insets = new Insets(0, 0, 5, 5);
    gridBagConstraintsLabelRequiredQuantityVgVolumeResult.gridx = 7;
    gridBagConstraintsLabelRequiredQuantityVgVolumeResult.gridy = 18;
    jFrameEliquidCalculator.getContentPane().add(labelRequiredQuantityVgVolumeResult, gridBagConstraintsLabelRequiredQuantityVgVolumeResult);

    var labelRequiredQuantityVgVolumeUnit = new JLabel(VOLUME_UNIT);
    labelRequiredQuantityVgVolumeUnit.setName("labelRequiredQuantityVgVolumeUnit");
    var gridBagConstraintsLabelRequiredQuantityVgVolumeUnit = new GridBagConstraints();
    gridBagConstraintsLabelRequiredQuantityVgVolumeUnit.anchor = GridBagConstraints.WEST;
    gridBagConstraintsLabelRequiredQuantityVgVolumeUnit.insets = new Insets(0, 0, 5, 5);
    gridBagConstraintsLabelRequiredQuantityVgVolumeUnit.gridx = 8;
    gridBagConstraintsLabelRequiredQuantityVgVolumeUnit.gridy = 18;
    jFrameEliquidCalculator.getContentPane().add(labelRequiredQuantityVgVolumeUnit, gridBagConstraintsLabelRequiredQuantityVgVolumeUnit);

    labelRequiredQuantityVgWeightResult = new JLabel("0");
    labelRequiredQuantityVgWeightResult.setName("labelRequiredQuantityVgWeightResult");
    var gridBagConstraintsLabelRequiredQuantityVgWeightResult = new GridBagConstraints();
    gridBagConstraintsLabelRequiredQuantityVgWeightResult.anchor = GridBagConstraints.EAST;
    gridBagConstraintsLabelRequiredQuantityVgWeightResult.insets = new Insets(0, 0, 5, 5);
    gridBagConstraintsLabelRequiredQuantityVgWeightResult.gridx = 10;
    gridBagConstraintsLabelRequiredQuantityVgWeightResult.gridy = 18;
    jFrameEliquidCalculator.getContentPane().add(labelRequiredQuantityVgWeightResult, gridBagConstraintsLabelRequiredQuantityVgWeightResult);

    var labelRequiredQuantityVgWeightUnit = new JLabel(WEIGHT_UNIT);
    labelRequiredQuantityVgWeightUnit.setName("labelRequiredQuantityVgWeightUnit");
    var gridBagConstraintsLabelRequiredQuantityVgWeightUnit = new GridBagConstraints();
    gridBagConstraintsLabelRequiredQuantityVgWeightUnit.anchor = GridBagConstraints.WEST;
    gridBagConstraintsLabelRequiredQuantityVgWeightUnit.insets = new Insets(0, 0, 5, 5);
    gridBagConstraintsLabelRequiredQuantityVgWeightUnit.gridx = 11;
    gridBagConstraintsLabelRequiredQuantityVgWeightUnit.gridy = 18;
    jFrameEliquidCalculator.getContentPane().add(labelRequiredQuantityVgWeightUnit, gridBagConstraintsLabelRequiredQuantityVgWeightUnit);

    labelRequiredQuantityVgPercentResult = new JLabel("0");
    labelRequiredQuantityVgPercentResult.setName("labelRequiredQuantityVgPercentResult");
    var gridBagConstraintsLabelRequiredQuantityVgPercentResult = new GridBagConstraints();
    gridBagConstraintsLabelRequiredQuantityVgPercentResult.anchor = GridBagConstraints.EAST;
    gridBagConstraintsLabelRequiredQuantityVgPercentResult.insets = new Insets(0, 0, 5, 5);
    gridBagConstraintsLabelRequiredQuantityVgPercentResult.gridx = 13;
    gridBagConstraintsLabelRequiredQuantityVgPercentResult.gridy = 18;
    jFrameEliquidCalculator.getContentPane().add(labelRequiredQuantityVgPercentResult, gridBagConstraintsLabelRequiredQuantityVgPercentResult);

    var labelRequiredQuantityVgPercentageUnit = new JLabel(PERCENT_UNIT);
    labelRequiredQuantityVgPercentageUnit.setName("labelRequiredQuantityVgPercentageUnit");
    var gridBagConstraintsLabelRequiredQuantityVgPercentageUnit = new GridBagConstraints();
    gridBagConstraintsLabelRequiredQuantityVgPercentageUnit.anchor = GridBagConstraints.WEST;
    gridBagConstraintsLabelRequiredQuantityVgPercentageUnit.insets = new Insets(0, 0, 5, 5);
    gridBagConstraintsLabelRequiredQuantityVgPercentageUnit.gridx = 14;
    gridBagConstraintsLabelRequiredQuantityVgPercentageUnit.gridy = 18;
    jFrameEliquidCalculator.getContentPane().add(labelRequiredQuantityVgPercentageUnit, gridBagConstraintsLabelRequiredQuantityVgPercentageUnit);

    var labelRequiredQuantityWater = new JLabel(internationalizationService.getMessage(GUI_LABEL_WATER));
    labelRequiredQuantityWater.setName("labelRequiredQuantityWater");
    var gridBagConstraintsLabelRequiredQuantityWater = new GridBagConstraints();
    gridBagConstraintsLabelRequiredQuantityWater.anchor = GridBagConstraints.WEST;
    gridBagConstraintsLabelRequiredQuantityWater.insets = new Insets(0, 0, 5, 5);
    gridBagConstraintsLabelRequiredQuantityWater.gridx = 2;
    gridBagConstraintsLabelRequiredQuantityWater.gridy = 19;
    jFrameEliquidCalculator.getContentPane().add(labelRequiredQuantityWater, gridBagConstraintsLabelRequiredQuantityWater);

    labelRequiredQuantityWaterVolumeResult = new JLabel("0");
    labelRequiredQuantityWaterVolumeResult.setName("labelRequiredQuantityWaterVolumeResult");
    var gridBagConstraintsLabelRequiredQuantityWaterVolumeResult = new GridBagConstraints();
    gridBagConstraintsLabelRequiredQuantityWaterVolumeResult.anchor = GridBagConstraints.EAST;
    gridBagConstraintsLabelRequiredQuantityWaterVolumeResult.insets = new Insets(0, 0, 5, 5);
    gridBagConstraintsLabelRequiredQuantityWaterVolumeResult.gridx = 7;
    gridBagConstraintsLabelRequiredQuantityWaterVolumeResult.gridy = 19;
    jFrameEliquidCalculator.getContentPane().add(labelRequiredQuantityWaterVolumeResult, gridBagConstraintsLabelRequiredQuantityWaterVolumeResult);

    var labelRequiredQuantityWaterVolumeUnit = new JLabel(VOLUME_UNIT);
    labelRequiredQuantityWaterVolumeUnit.setName("labelRequiredQuantityWaterVolumeUnit");
    var gridBagConstraintsLabelRequiredQuantityWaterVolumeUnit = new GridBagConstraints();
    gridBagConstraintsLabelRequiredQuantityWaterVolumeUnit.anchor = GridBagConstraints.WEST;
    gridBagConstraintsLabelRequiredQuantityWaterVolumeUnit.insets = new Insets(0, 0, 5, 5);
    gridBagConstraintsLabelRequiredQuantityWaterVolumeUnit.gridx = 8;
    gridBagConstraintsLabelRequiredQuantityWaterVolumeUnit.gridy = 19;
    jFrameEliquidCalculator.getContentPane().add(labelRequiredQuantityWaterVolumeUnit, gridBagConstraintsLabelRequiredQuantityWaterVolumeUnit);

    labelRequiredQuantityWaterWeightResult = new JLabel("0");
    labelRequiredQuantityWaterWeightResult.setName("labelRequiredQuantityWaterWeightResult");
    var gridBagConstraintsLabelRequiredQuantityWaterWeightResult = new GridBagConstraints();
    gridBagConstraintsLabelRequiredQuantityWaterWeightResult.anchor = GridBagConstraints.EAST;
    gridBagConstraintsLabelRequiredQuantityWaterWeightResult.insets = new Insets(0, 0, 5, 5);
    gridBagConstraintsLabelRequiredQuantityWaterWeightResult.gridx = 10;
    gridBagConstraintsLabelRequiredQuantityWaterWeightResult.gridy = 19;
    jFrameEliquidCalculator.getContentPane().add(labelRequiredQuantityWaterWeightResult, gridBagConstraintsLabelRequiredQuantityWaterWeightResult);

    var labelRequiredQuantityWaterWeightUnit = new JLabel(WEIGHT_UNIT);
    labelRequiredQuantityWaterWeightUnit.setName("labelRequiredQuantityWaterWeightUnit");
    var gridBagConstraintsLabelRequiredQuantityWaterWeightUnit = new GridBagConstraints();
    gridBagConstraintsLabelRequiredQuantityWaterWeightUnit.anchor = GridBagConstraints.WEST;
    gridBagConstraintsLabelRequiredQuantityWaterWeightUnit.insets = new Insets(0, 0, 5, 5);
    gridBagConstraintsLabelRequiredQuantityWaterWeightUnit.gridx = 11;
    gridBagConstraintsLabelRequiredQuantityWaterWeightUnit.gridy = 19;
    jFrameEliquidCalculator.getContentPane().add(labelRequiredQuantityWaterWeightUnit, gridBagConstraintsLabelRequiredQuantityWaterWeightUnit);

    labelRequiredQuantityWaterPercentResult = new JLabel("0");
    labelRequiredQuantityWaterPercentResult.setName("labelRequiredQuantityWaterPercentResult");
    var gridBagConstraintsLabelRequiredQuantityWaterPercentResult = new GridBagConstraints();
    gridBagConstraintsLabelRequiredQuantityWaterPercentResult.anchor = GridBagConstraints.EAST;
    gridBagConstraintsLabelRequiredQuantityWaterPercentResult.insets = new Insets(0, 0, 5, 5);
    gridBagConstraintsLabelRequiredQuantityWaterPercentResult.gridx = 13;
    gridBagConstraintsLabelRequiredQuantityWaterPercentResult.gridy = 19;
    jFrameEliquidCalculator.getContentPane().add(labelRequiredQuantityWaterPercentResult, gridBagConstraintsLabelRequiredQuantityWaterPercentResult);

    var labelRequiredQuantityWaterPercentageUnit = new JLabel(PERCENT_UNIT);
    labelRequiredQuantityWaterPercentageUnit.setName("labelRequiredQuantityWaterPercentageUnit");
    var gridBagConstraintsLabelRequiredQuantityWaterPercentageUnit = new GridBagConstraints();
    gridBagConstraintsLabelRequiredQuantityWaterPercentageUnit.anchor = GridBagConstraints.WEST;
    gridBagConstraintsLabelRequiredQuantityWaterPercentageUnit.insets = new Insets(0, 0, 5, 5);
    gridBagConstraintsLabelRequiredQuantityWaterPercentageUnit.gridx = 14;
    gridBagConstraintsLabelRequiredQuantityWaterPercentageUnit.gridy = 19;
    jFrameEliquidCalculator.getContentPane().add(labelRequiredQuantityWaterPercentageUnit, gridBagConstraintsLabelRequiredQuantityWaterPercentageUnit);
  }

  private PersistentValues getValuesFromArgumentsOrProperty(String[] args, CommandLine commandLine) {
    if(args.length != 0) {
      return PersistentValues.builder()
          .withAmount(createIntegerString(commandLine, CLI_AMOUNT_SHORT_OPTION))
          .withBaseNicotine(createIntegerString(commandLine, CLI_BASE_NICOTINE_SHORT_OPTION))
          .withBasePg(createIntegerStringFromDoubleString(commandLine, CLI_BASE_PG_SHORT_OPTION))
          .withBaseVg(createIntegerStringFromDoubleString(commandLine, CLI_BASE_VG_SHORT_OPTION))
          .withBaseWater(createIntegerStringFromDoubleString(commandLine, CLI_BASE_WATER_SHORT_OPTION))
          .withTargetNicotine(createIntegerString(commandLine, CLI_NICOTINE_SHORT_OPTION))
          .withTargetPg(createIntegerStringFromDoubleString(commandLine, CLI_PG_SHORT_OPTION))
          .withTargetVg(createIntegerStringFromDoubleString(commandLine, CLI_VG_SHORT_OPTION))
          .withTargetWater(createIntegerStringFromDoubleString(commandLine, CLI_WATER_SHORT_OPTION))
          .build();
    } else {
      return persistentService.loadValues();
    }
  }

  private String createIntegerString(CommandLine commandLine, String shortOption) {
    return commandLine.hasOption(shortOption) ? commandLine.getOptionValue(shortOption) : "0";
  }

  private String createIntegerStringFromDoubleString(CommandLine commandLine, String shortOption) {
    return commandLine.hasOption(shortOption) ? String.valueOf((int) (Double.valueOf(commandLine.getOptionValue(shortOption)) * 100)) : "0";
  }

  protected void updateLocalizedTextsOnComponents() {
    log.debug("updateTextsOnComponents called.");
    labelBasicMaterials.setText(internationalizationService.getMessage(GUI_LABEL_BASIC_SUBSTANCES));
    labelNicotine.setText(internationalizationService.getMessage(GUI_LABEL_NICOTINE));
    labelBasicMaterialsWaterPercent.setText(internationalizationService.getMessage(GUI_LABEL_WATER));
    labelBasicMaterialsBaseLiquid.setText(internationalizationService.getMessage(GUI_LABEL_BASE_LIQUID));
    labelFinishedLiquid.setText(internationalizationService.getMessage(GUI_LABEL_FINISHED_LIQUID_TARGET));
    labelFinishedLiquidNicotineConcentration.setText(internationalizationService.getMessage(GUI_LABEL_NICOTINE));
    labelFinishedLiquidWaterPercent.setText(internationalizationService.getMessage(GUI_LABEL_WATER));
    labelFinishedLiquidAmount.setText(internationalizationService.getMessage(GUI_LABEL_AMOUNT));
    labelFinishedLiquidFinishedLiquid.setText(internationalizationService.getMessage(GUI_LABEL_FINISHED_LIQUID));
    buttonCalculate.setText(internationalizationService.getMessage(GUI_BUTTON_CALCULATE));
    buttonSave.setText(internationalizationService.getMessage(GUI_BUTTON_SAVE));
    labelRequiredQuantity.setText(internationalizationService.getMessage(GUI_LABEL_REQUIRED_QUANTITY));
    labelRequiredQuantityVolumen.setText(internationalizationService.getMessage(GUI_LABEL_VOLUME));
    labelRequiredQuantityWeight.setText(internationalizationService.getMessage(GUI_LABEL_WEIGHT));
    labelRequiredQuantityPercent.setText(internationalizationService.getMessage(GUI_LABEL_PERCENT));
    labelRequiredQuantityBaseliquid.setText(internationalizationService.getMessage(GUI_LABEL_BASE_LIQUID));

    if (!labelRequiredQuantityBaseLiquidPercentResult.getText().equals("0")) {
      calculateRequiredQuantity();
    }
  }

  protected void calculateRequiredQuantity() {
    try {
      resetBordersToDefault(textBasicMaterialsBaseLiquidPg, textBasicMaterialsBaseLiquidVg, textBasicMaterialsBaseLiquidWater, textBasicMaterialsBaseLiquidNicotine, textFinishedLiquidNicotine, textFinishedLiquidPg, textFinishedLiquidVg, textFinishedLiquidWater, textFinishedLiquidAmount);
      deleteBorders(labelRequiredQuantityBaseLiquidVolumeResult, labelRequiredQuantityBaseLiquidWeightResult, labelRequiredQuantityBaseLiquidPercentResult, labelRequiredQuantityPgVolumeResult, labelRequiredQuantityPgWeightResult, labelRequiredQuantityPgPercentResult, labelRequiredQuantityVgVolumeResult, labelRequiredQuantityVgWeightResult, labelRequiredQuantityVgPercentResult, labelRequiredQuantityWaterVolumeResult, labelRequiredQuantityWaterWeightResult, labelRequiredQuantityWaterPercentResult);
      log.debug("calculateRequiredQuantity called");
      log.debug("Current locale: {}", internationalizationConfiguration.getLocale());
      var amount = Integer.parseInt(textFinishedLiquidAmount.getText());
      var eLiquidBase = ELiquidBase.builder()
          .withNicotine(Integer.parseInt(textBasicMaterialsBaseLiquidNicotine.getText()))
          .withPgPercentage(Double.parseDouble(textBasicMaterialsBaseLiquidPg.getText()) / 100)
          .withVgPercentage(Double.parseDouble(textBasicMaterialsBaseLiquidVg.getText()) / 100)
          .withWaterPercentage(Double.parseDouble(textBasicMaterialsBaseLiquidWater.getText()) / 100)
          .build();
      var targetNicotine = Integer.parseInt(textFinishedLiquidNicotine.getText());
      var pgPercent = Double.parseDouble(textFinishedLiquidPg.getText()) / 100;
      var vgPercent = Double.parseDouble(textFinishedLiquidVg.getText()) / 100;
      var waterPercent = Double.parseDouble(textFinishedLiquidWater.getText()) / 100;

      log.debug("ELiquidBase values: {}", eLiquidBase);
      log.debug("amount value: {}", amount);
      log.debug("targetNicotine value: {}", targetNicotine);
      log.debug("pgPercent value: {}", pgPercent);
      log.debug("vgPercent value: {}", vgPercent);
      log.debug("waterPercent value: {}", waterPercent);
      var eLiquidResult = calculator.calculateResult(amount, eLiquidBase, targetNicotine, pgPercent, vgPercent, waterPercent);
      var resultVolumeWeightPercentage = resultVolumeWeightPercentageMapper.mapToResultVolumeWeightPercentage(eLiquidResult);
      labelRequiredQuantityBaseLiquidVolumeResult.setText(internationalizationService.fiveDigitNumberOutput(resultVolumeWeightPercentage.getBaseLiquidVolume()));
      labelRequiredQuantityBaseLiquidWeightResult.setText(internationalizationService.fiveDigitNumberOutput(resultVolumeWeightPercentage.getBaseLiquidWeight()));
      labelRequiredQuantityBaseLiquidPercentResult.setText(internationalizationService.fiveDigitNumberOutput(resultVolumeWeightPercentage.getBaseLiquidPercentage()));

      labelRequiredQuantityPgVolumeResult.setText(internationalizationService.fiveDigitNumberOutput(resultVolumeWeightPercentage.getPgVolume()));
      labelRequiredQuantityPgWeightResult.setText(internationalizationService.fiveDigitNumberOutput(resultVolumeWeightPercentage.getPgWeight()));
      labelRequiredQuantityPgPercentResult.setText(internationalizationService.fiveDigitNumberOutput(resultVolumeWeightPercentage.getPgPercentage()));

      labelRequiredQuantityVgVolumeResult.setText(internationalizationService.fiveDigitNumberOutput(resultVolumeWeightPercentage.getVgVolume()));
      labelRequiredQuantityVgWeightResult.setText(internationalizationService.fiveDigitNumberOutput(resultVolumeWeightPercentage.getVgWeight()));
      labelRequiredQuantityVgPercentResult.setText(internationalizationService.fiveDigitNumberOutput(resultVolumeWeightPercentage.getVgPercentage()));

      labelRequiredQuantityWaterVolumeResult.setText(internationalizationService.fiveDigitNumberOutput(resultVolumeWeightPercentage.getWaterVolume()));
      labelRequiredQuantityWaterWeightResult.setText(internationalizationService.fiveDigitNumberOutput(resultVolumeWeightPercentage.getWaterWeight()));
      labelRequiredQuantityWaterPercentResult.setText(internationalizationService.fiveDigitNumberOutput(resultVolumeWeightPercentage.getWaterPercentage()));

      checkResultsAreGreaterEqualZero(resultVolumeWeightPercentage);

    } catch (ValidationException e) {
      markFieldsThatHoldsWrongValues(textBasicMaterialsBaseLiquidPg, textBasicMaterialsBaseLiquidVg, textBasicMaterialsBaseLiquidWater);
      openDialog(e.getMessage(), "ValidationException");
    } catch (ConstraintViolationException e) {
      if (e.getMessage().contains("calculateResult.arg0")) {
        markFieldsThatHoldsWrongValues(textFinishedLiquidAmount);
      }
      if (e.getMessage().contains("calculateResult.<cross-parameter>")) {
        markFieldsThatHoldsWrongValues(textFinishedLiquidPg, textFinishedLiquidVg, textFinishedLiquidWater);
      }
      openDialog(e.getMessage(), "ConstraintViolationException");
    } catch (NumberFormatException e) {
      openDialog(internationalizationService.getMessage("calculator.exception.number.format.errormessage"), "NumberFormatException");
    } catch (IllegalArgumentException e) {
      var errorMessage = internationalizationService.getMessage(e.getMessage());
      markFieldsThatHoldsWrongValues(textBasicMaterialsBaseLiquidNicotine, textFinishedLiquidNicotine);
      openDialog(errorMessage, "IllegalArgumentException");
    }
  }

  protected void saveValues() {
    var persistendValues = PersistentValues.builder()
        .withBaseNicotine(textBasicMaterialsBaseLiquidNicotine.getText())
        .withBasePg(textBasicMaterialsBaseLiquidPg.getText())
        .withBaseVg(textBasicMaterialsBaseLiquidVg.getText())
        .withBaseWater(textBasicMaterialsBaseLiquidWater.getText())
        .withTargetNicotine(textFinishedLiquidNicotine.getText())
        .withTargetPg(textFinishedLiquidPg.getText())
        .withTargetVg(textFinishedLiquidVg.getText())
        .withTargetWater(textFinishedLiquidWater.getText())
        .withAmount(textFinishedLiquidAmount.getText())
        .build();
    try {
      persistentService.saveValues(persistendValues);
      var message = internationalizationService.getMessage(GUI_DIALOG_SAVE_SUCCESS_MESSAGE) + persistentService.getApplicationDataDirectoryFile().getAbsolutePath();
      var title = internationalizationService.getMessage(GUI_DIALOG_SAVE_SUCCESS_TITLE);
      openDialog(message, title);
    } catch (Exception e) {
      var message = internationalizationService.getMessage(GUI_DIALOG_SAVE_FAILURE_MESSAGE) + persistentService.getApplicationDataDirectoryFile().getAbsolutePath();
      var title = internationalizationService.getMessage(GUI_DIALOG_SAVE_FAILURE_TITLE);
      log.warn(message, e);
      openDialog(message, title);
    }
  }

  protected void resetBordersToDefault(JTextField ...jTextFields) {
    Arrays.stream(jTextFields).forEach(jTextField -> jTextField.setBorder(UIManager.getLookAndFeel().getDefaults().getBorder("TextField.border")));
  }

  protected void deleteBorders(JLabel ...jLabels) {
    Arrays.stream(jLabels).forEach(jLabel -> jLabel.setBorder(null));
  }

  protected void openDialog(String message, String title) {
    var labelMessage = new JLabel(message, SwingConstants.CENTER);
    var jOptionPane = new JOptionPane(labelMessage);
    var jDialog = jOptionPane.createDialog(title);
    jDialog.setModalityType(ModalityType.MODELESS);
    jDialog.setVisible(true);
  }

  protected void markFieldsThatHoldsWrongValues(JComponent ...jComponents) {
    Arrays.stream(jComponents).forEach(jComponent -> jComponent.setBorder(BorderFactory.createLineBorder(Color.RED)));
  }

  protected void checkResultsAreGreaterEqualZero(ResultVolumeWeightPercentage resultVolumeWeightPercentage) {
    if (resultVolumeWeightPercentage.getPgPercentage() < 0) {
      markFieldsThatHoldsWrongValues(labelRequiredQuantityPgVolumeResult, labelRequiredQuantityPgWeightResult, labelRequiredQuantityPgPercentResult);
    }
    if (resultVolumeWeightPercentage.getVgPercentage() < 0) {
      markFieldsThatHoldsWrongValues(labelRequiredQuantityVgVolumeResult, labelRequiredQuantityVgWeightResult, labelRequiredQuantityVgPercentResult);
    }
    if (resultVolumeWeightPercentage.getWaterPercentage() < 0) {
      markFieldsThatHoldsWrongValues(labelRequiredQuantityWaterVolumeResult, labelRequiredQuantityWaterWeightResult, labelRequiredQuantityWaterPercentResult);
    }
  }
}
