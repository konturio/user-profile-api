package io.kontur.userprofile.model.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.math.BigDecimal;
import java.util.List;

public class ExtentValidator implements ConstraintValidator<ValidExtent, List<BigDecimal>> {

    @Override
    public void initialize(ValidExtent value) {
    }

    @Override
    public boolean isValid(List<BigDecimal> extent, ConstraintValidatorContext ctx) {
        if (extent == null || extent.isEmpty()) {
            return true;
        }
        if (extent.size() != 4) {
            ctx.buildConstraintViolationWithTemplate("extent should be provided as 4.")
                    .addConstraintViolation();
            return false;
        }
        var b = checkLon(extent.get(0))
                && checkLat(extent.get(1))
                && checkLon(extent.get(2))
                && checkLat(extent.get(3));
        if (!b) {
            ctx.buildConstraintViolationWithTemplate("extent coordinates don't conform to WGS84 coordinate system")
                    .addConstraintViolation();
            return false;
        }
        return true;
    }

    private boolean checkLat(BigDecimal lat) {
        return BigDecimal.valueOf(90).compareTo(lat) >= 0 && BigDecimal.valueOf(-90).compareTo(lat) <= 0;
    }

    private boolean checkLon(BigDecimal lon) {
        return BigDecimal.valueOf(180).compareTo(lon) >= 0 && BigDecimal.valueOf(-180).compareTo(lon) <= 0;
    }
}
