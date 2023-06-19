package com.gambasoftware.users.application;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class UserService {

    private Logger logger = LoggerFactory.getLogger(UserService.class);

    private List<User> users;

    public Multi<User> getUsers(int number) {
        return Multi.createFrom().iterable(createUsers(number))
                .onItem().transformToUniAndMerge(user -> {
                    Uni<String> userName = Uni.createFrom().item(() -> {
                        String name = getUserNameSync(user.getId());
                        return name;
                    });
                    Uni<String> email = getEmailAddress(String.valueOf(user.getId()));
                    return Uni.combine().all().unis(userName, email).asTuple()
                            .onItem().transform(objects -> {
                                user.setEmailAddress(objects.getItem2());
                                user.setName(objects.getItem1());
                                return user;
                            });
                })
                .onItem().transform(user -> {
                    if (user.getId() == 8)
                        throw new UserValidationException("Invalid number of users!");
                    return user;
                })
                .onFailure().transform(throwable -> new UserValidationException(throwable.getMessage()));
    }

    public List<User> createUsers(int number) {
        List<User> users = new ArrayList<>();
        for (int i = 0; i < number; i++) {
            User user = new User();
            user.setId(i);
            users.add(user);
        }
        return users;
    }

    public String getUserNameSync(long id) {
        logger.info("getting userName for id=" + id);
        try {
            Thread.sleep(id * 100);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return "name-delay-in-" + id;
    }

    public Uni<String> getEmailAddress(String id) {
        logger.info("getting email for id=" + id);
        return Uni.createFrom().item(UUID.randomUUID().toString());
    }
}
