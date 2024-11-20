package com.ddelval.receiptprocessor.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = TotalMatchesItemSumValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface TotalMatchesItemSum {
    String message() default "Total must match sum of item prices";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
