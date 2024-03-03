package io.kontur.userprofile.model.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.PARAMETER, ElementType.TYPE, ElementType.METHOD, ElementType.FIELD, ElementType.CONSTRUCTOR, ElementType.ANNOTATION_TYPE, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ExtentValidator.class)
@Documented
public @interface ValidExtent {
    String message() default "extent has wrong format";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
