package com.github.rkpunjal.sqlsafe;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * <h2>SQLInjectionSafe</h2>
 * Annotation to help verify if the annotated string field or parameter is sql injection safe
 *
 * @author  Ramakrishna Punjal
 * @version 1.0.0
 * @since   2016-08-26
 *
 */
@Documented
@Constraint(validatedBy = SQLInjectionSafeValidator.class)
@Target( { ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface SQLInjectionSafe {

    String message() default "{SQLInjectionSafe}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}