package com.pretty.platform.federation.interfaces.rest.transform;

import com.pretty.platform.federation.domain.model.aggregates.User;
import com.pretty.platform.federation.interfaces.rest.resources.UserResource;

public class UserResourceFromEntityAssembler {

    public static UserResource toResourceFromEntity(User user) {
        return new UserResource(
            user.getId(),
            user.getName(),
            user.getEmail(),
            user.getPictureUrl()
        );
    }
}
