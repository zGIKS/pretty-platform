package com.pretty.platform.promotions.interfaces.rest.transform;

import com.pretty.platform.promotions.domain.model.commands.CreatePackCommand;
import com.pretty.platform.promotions.interfaces.rest.resources.CreatePackResource;

import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Assembler to convert CreatePackResource to CreatePackCommand
 */
public class CreatePackCommandFromResourceAssembler {

    public static CreatePackCommand toCommand(CreatePackResource resource) {
        return new CreatePackCommand(
            resource.packName(),
            resource.productIds().stream()
                .map(UUID::fromString)
                .collect(Collectors.toList()),
            resource.packPrice(),
            resource.currency(),
            resource.startDate(),
            resource.endDate()
        );
    }
}