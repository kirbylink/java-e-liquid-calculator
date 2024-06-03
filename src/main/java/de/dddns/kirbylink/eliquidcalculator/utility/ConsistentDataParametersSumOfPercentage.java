package de.dddns.kirbylink.eliquidcalculator.utility;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ConsistentDataParametersSumOfPercentageValidator.class)
public @interface ConsistentDataParametersSumOfPercentage {
  String message() default "{consistent.data.parameters.sum.of.percentage.validator}";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
