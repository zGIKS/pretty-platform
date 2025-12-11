package com.pretty.platform.promotions.interfaces.rest.transform;

import com.pretty.platform.promotions.domain.model.commands.CreateComboCommand;
import com.pretty.platform.promotions.interfaces.rest.resources.CreateComboResource;

import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Assembler to convert CreateComboResource to CreateComboCommand
 */
public class CreateComboCommandFromResourceAssembler {

    public static CreateComboCommand toCommand(CreateComboResource resource) {
        return new CreateComboCommand(
            resource.comboName(),
            resource.productIds().stream()
                .map(UUID::fromString)
                .collect(Collectors.toList()),
            resource.comboPrice(),
            resource.currency(),
            resource.startDate(),
            resource.endDate()
        );
    }
}