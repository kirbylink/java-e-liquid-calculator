package de.dddns.kirbylink.eliquidcalculator.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import java.awt.Container;
import java.io.IOException;
import java.util.Locale;
import javax.swing.JFrame;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import de.dddns.kirbylink.eliquidcalculator.config.CommandLineConfiguration;
import de.dddns.kirbylink.eliquidcalculator.config.GuiConfiguration;
import de.dddns.kirbylink.eliquidcalculator.config.InternationalizationConfiguration;
import de.dddns.kirbylink.eliquidcalculator.service.PersistentService.PersistentValues;

@ExtendWith(MockitoExtension.class)
class GuiServiceTest {

  @Mock
  Container container;
  @Mock
  private JFrame jFrame;
  @Mock
  private InternationalizationService internationalizationService;
  @Mock
  private InternationalizationConfiguration internationalizationConfiguration;
  @Mock
  private CommandLineConfiguration commandLineConfiguration;
  @Mock
  private CommandLineService commandLineService;
  @Mock
  private Options options;
  @Mock
  private CommandLine commandLine;
  @Mock
  private GuiConfiguration guiConfiguration;
  @Mock
  private PersistentService persistentService;
  @InjectMocks
  private GuiService guiService;

  @Test
  void testOpenWindow() throws IOException {

    // Given
    when(jFrame.getContentPane()).thenReturn(container);
    when(internationalizationConfiguration.getLocale()).thenReturn(Locale.ENGLISH);
    when(commandLineConfiguration.getGuiOptions()).thenReturn(options);
    when(commandLineService.getCommandLineFromArgumentsAndOptions(any(String[].class), eq(options))).thenReturn(commandLine);
    when(persistentService.loadValues()).thenReturn(PersistentValues.builder().build());

    // When
    guiService.openWindow(new String[0]);

    // Then
    verify(jFrame).setTitle("E-Liquid-Calculator");
    verify(jFrame).setSize(1024, 576);
    verify(jFrame).setVisible(true);
  }

  @Test
  void testOpenWindow_WhenProgramArgumentsContainsHelpOption_ThenJFrameIsNotBuild() throws IOException {

    // Given
    when(commandLineConfiguration.getGuiOptions()).thenReturn(options);
    when(commandLineService.getCommandLineFromArgumentsAndOptions(any(String[].class), eq(options))).thenReturn(null);

    // When
    guiService.openWindow(new String[] {"-h"});

    // Then
    verifyNoInteractions(jFrame);
  }
}
