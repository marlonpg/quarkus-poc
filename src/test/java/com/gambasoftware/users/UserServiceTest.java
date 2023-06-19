package com.gambasoftware.users;

import com.gambasoftware.users.application.User;
import com.gambasoftware.users.application.UserService;
import com.gambasoftware.users.application.UserValidationException;
import io.smallrye.mutiny.helpers.test.AssertSubscriber;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserServiceTest {

    private UserService userService = new UserService();

    @Test
    public void getUsers_shouldReturn2Users_whenSuccessful() {
        AssertSubscriber<User> userAssertSubscriber = userService.getUsers(2)
                .subscribe().withSubscriber(AssertSubscriber.create(2));
        List<User> userList = userAssertSubscriber.getItems();

        assertEquals(0, userList.get(0).getId());
        assertEquals(1, userList.get(1).getId());
    }
    @Test
    public void getUsers_shouldFailed_whenTryingToCreateMoreThan10Users() {
        AssertSubscriber<User> userAssertSubscriber = userService.getUsers(11)
                .subscribe().withSubscriber(AssertSubscriber.create(11));

        userAssertSubscriber.assertFailedWith(UserValidationException.class, "Invalid number of users!");
    }
}
