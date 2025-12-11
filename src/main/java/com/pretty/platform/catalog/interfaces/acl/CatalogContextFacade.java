package com.pretty.platform.catalog.interfaces.acl;

import java.util.UUID;

/**
 * ACL Facade for Catalog Context
 */
public interface CatalogContextFacade {
    boolean productExists(UUID productId);
}