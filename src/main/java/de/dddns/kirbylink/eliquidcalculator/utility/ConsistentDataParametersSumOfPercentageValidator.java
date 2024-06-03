package de.dddns.kirbylink.eliquidcalculator.utility;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.constraintvalidation.SupportedValidationTarget;
import jakarta.validation.constraintvalidation.ValidationTarget;

@SupportedValidationTarget(ValidationTarget.PARAMETERS)
public class ConsistentDataParametersSumOfPercentageValidator implements ConstraintValidator<ConsistentDataParametersSumOfPercentage, Object[]> {
  @Override
  public boolean isValid(Object[] value, ConstraintValidatorContext context) {
    return ((double) value[3] + (double) value[4] + (double) value[5] == 1.0);
  }
}
