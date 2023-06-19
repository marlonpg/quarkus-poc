package com.gambasoftware.users.api;

import com.gambasoftware.users.application.User;
import com.gambasoftware.users.application.UserService;
import io.smallrye.mutiny.Multi;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;

@Path("/users")
public class UserController {

    @Inject
    private UserService userService;

    @GET
    public Multi<User> getUsers() {
        return userService.getUsers(5);
    }

    @GET
    @Path("/{numberOfUsers}")
    public Multi<User> getUsers(Integer numberOfUsers) {
        return userService.getUsers(numberOfUsers);
    }
}
