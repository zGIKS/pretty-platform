package com.pretty.platform.promotions.interfaces.rest.transform;

import com.pretty.platform.promotions.domain.model.commands.CreateCampaignPriceCommand;
import com.pretty.platform.promotions.interfaces.rest.resources.CreateCampaignPriceResource;

import java.util.UUID;

/**
 * Assembler to convert CreateCampaignPriceResource to CreateCampaignPriceCommand
 */
public class CreateCampaignPriceCommandFromResourceAssembler {

    public static CreateCampaignPriceCommand toCommand(CreateCampaignPriceResource resource) {
        return new CreateCampaignPriceCommand(
            UUID.fromString(resource.productId()),
            resource.campaignName(),
            resource.campaignPrice(),
            resource.currency(),
            resource.startDate(),
            resource.endDate()
        );
    }
}