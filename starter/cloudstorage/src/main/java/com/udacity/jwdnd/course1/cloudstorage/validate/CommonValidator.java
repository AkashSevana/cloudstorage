package com.udacity.jwdnd.course1.cloudstorage.validate;

import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Component
public class CommonValidator {

    private final UserService userService;

    public CommonValidator(UserService userService) {
        this.userService = userService;
    }

    public void validateUser(User user) {
        if (!userService.isUsernameAvailable(user.getUsername())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The username already exists.");
        }
    }
}
