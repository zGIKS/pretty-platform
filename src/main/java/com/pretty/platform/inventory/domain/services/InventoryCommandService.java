package com.pretty.platform.inventory.domain.services;

import com.pretty.platform.inventory.domain.model.commands.CreateInventoryCommand;
import com.pretty.platform.inventory.domain.model.commands.ReleaseStockCommand;
import com.pretty.platform.inventory.domain.model.commands.ReserveStockCommand;
import com.pretty.platform.inventory.domain.model.commands.SellStockCommand;
import com.pretty.platform.inventory.domain.model.commands.UpdateStockCommand;

import java.util.Optional;

/**
 * Domain service for inventory command operations
 */
public interface InventoryCommandService {
    Optional<Long> handle(CreateInventoryCommand command);
    Optional<Long> handle(UpdateStockCommand command);
    Optional<Long> handle(ReserveStockCommand command);
    Optional<Long> handle(ReleaseStockCommand command);
    Optional<Long> handle(SellStockCommand command);
}