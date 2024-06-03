package de.dddns.kirbylink.eliquidcalculator.config;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JFrame;
import javax.swing.JTextField;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GuiConfiguration {
  @Bean
  public JFrame jFrame() {
    return new JFrame();
  }

  @Bean
  public FocusListener focusListener() {
    return new FocusListener() {
      @Override
      public void focusGained(FocusEvent focusEvent) {
        ((JTextField) focusEvent.getSource()).selectAll();
      }

      @Override
      public void focusLost(FocusEvent e) {
        // no action needed
      }
    };
  }

  public KeyAdapter keyAdapter(Runnable calculateRunnable) {
    return new KeyAdapter() {
      @Override
      public void keyPressed(KeyEvent keyEvent) {
        if (keyEvent.getKeyCode() == KeyEvent.VK_ENTER) {
          calculateRunnable.run();
        }
      }
    };
  }
}
