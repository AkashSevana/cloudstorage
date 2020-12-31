package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import com.udacity.jwdnd.course1.cloudstorage.validate.CommonValidator;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

@Controller()
@RequestMapping("/signup")
public class SignupController {

    private final UserService userService;
    private final CommonValidator commonValidator;

    public SignupController(UserService userService, CommonValidator commonValidator) {
        this.userService = userService;
        this.commonValidator = commonValidator;
    }

    @GetMapping()
    public String signupView() {
        return "signup";
    }

    @PostMapping()
    public String signupUser(@ModelAttribute User user, Model model) {
        try {
            commonValidator.validateUser(user);
            userService.createUser(user);
            model.addAttribute("signupSuccess", true);
        } catch (ResponseStatusException responseStatusException) {
            model.addAttribute("signupError", responseStatusException.getReason());
        }
        return "signup";
    }
}
