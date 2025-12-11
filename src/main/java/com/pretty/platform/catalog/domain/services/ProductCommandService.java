package com.pretty.platform.catalog.domain.services;

import com.pretty.platform.catalog.domain.model.commands.CreateProductCommand;
import java.util.Optional;
import java.util.UUID;

/**
 * Domain service for product command operations
 */
public interface ProductCommandService {
    Optional<UUID> handle(CreateProductCommand command);
}