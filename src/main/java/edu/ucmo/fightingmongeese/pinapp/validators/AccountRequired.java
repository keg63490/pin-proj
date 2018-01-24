package edu.ucmo.fightingmongeese.pinapp.validators;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = AccountRequiredValidator.class)
public @interface AccountRequired {

    String message() default "New PINs must supply an alphanumeric account";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
