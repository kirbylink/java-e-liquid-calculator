package de.dddns.kirbylink.eliquidcalculator.exceptions;

import static java.lang.String.format;
import java.util.Arrays;
import java.util.Locale;
import java.util.Objects;
import java.util.ResourceBundle;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ValidationException extends IllegalArgumentException {

  private static final long serialVersionUID = -232219403321551928L;

  private final String messageKey;
  private final Locale locale = Locale.getDefault();
  private final Double pg;
  private final Double vg;
  private final Double water;

  public ValidationException(String messageKey) {
    this(messageKey, null, null, null);
  }

  @Override
  public String getMessage() {
    return Messages.getMessageForLocale(messageKey, locale, pg, vg, water);
  }

  private static class Messages {

    public static String getMessageForLocale(String messageKey, Locale locale, Double... doubles) {
      var message = ResourceBundle.getBundle("messages/messages", locale).getString(messageKey);
      return Arrays.asList(doubles).stream().noneMatch(Objects::nonNull) ? message : format(message, doubles[0], doubles[1], doubles[2]);
    }
  }
}
