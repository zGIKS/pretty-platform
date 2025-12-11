package com.pretty.platform.promotions.interfaces.rest.transform;

import com.pretty.platform.promotions.domain.model.commands.CreateBasePriceCommand;
import com.pretty.platform.promotions.interfaces.rest.resources.CreateBasePriceResource;

import java.util.UUID;

/**
 * Assembler to convert CreateBasePriceResource to CreateBasePriceCommand
 */
public class CreateBasePriceCommandFromResourceAssembler {

    public static CreateBasePriceCommand toCommand(CreateBasePriceResource resource) {
        return new CreateBasePriceCommand(
            UUID.fromString(resource.productId()),
            resource.price(),
            resource.currency()
        );
    }
}