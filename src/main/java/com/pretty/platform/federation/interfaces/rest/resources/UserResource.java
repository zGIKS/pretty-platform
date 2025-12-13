package com.pretty.platform.federation.interfaces.rest.resources;

import java.util.UUID;

public record UserResource(
    UUID id,
    String name,
    String email,
    String pictureUrl
) {
}
