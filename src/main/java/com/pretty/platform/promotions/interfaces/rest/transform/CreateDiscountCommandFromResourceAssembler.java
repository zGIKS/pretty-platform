package com.pretty.platform.promotions.interfaces.rest.transform;

import com.pretty.platform.promotions.domain.model.commands.CreateDiscountCommand;
import com.pretty.platform.promotions.interfaces.rest.resources.CreateDiscountResource;

import java.util.UUID;

/**
 * Assembler to convert CreateDiscountResource to CreateDiscountCommand
 */
public class CreateDiscountCommandFromResourceAssembler {

    public static CreateDiscountCommand toCommand(CreateDiscountResource resource) {
        return new CreateDiscountCommand(
            UUID.fromString(resource.productId()),
            resource.discountAmount(),
            resource.discountPercentage() != null ? resource.discountPercentage().intValue() : null,
            resource.startDate(),
            resource.endDate()
        );
    }
}