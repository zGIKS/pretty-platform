package com.pretty.platform.promotions.interfaces.rest.transform;

import com.pretty.platform.promotions.domain.model.commands.CreateCouponCommand;
import com.pretty.platform.promotions.interfaces.rest.resources.CreateCouponResource;

import java.util.UUID;

/**
 * Assembler to convert CreateCouponResource to CreateCouponCommand
 */
public class CreateCouponCommandFromResourceAssembler {

    public static CreateCouponCommand toCommand(CreateCouponResource resource) {
        return new CreateCouponCommand(
            resource.couponCode(),
            UUID.fromString(resource.productId()),
            resource.discountAmount(),
            resource.discountPercentage() != null ? resource.discountPercentage().intValue() : null,
            resource.startDate(),
            resource.endDate(),
            resource.maxUsage()
        );
    }
}