package com.udacity.jwdnd.course1.cloudstorage.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

@Controller
@Slf4j
public class ErrorController implements org.springframework.boot.web.servlet.error.ErrorController {

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        String errorHeader = "Something went wrong!";
        String errorMessage = "We are looking into it!";
        if (status != null) {
            Integer statusCode = Integer.valueOf(status.toString());
            if(statusCode == HttpStatus.NOT_FOUND.value()) {
                errorHeader = "Page not found!";
                errorMessage = "We couldn't find the page you are looking for.";
            }
            log.error("{} , {} ------- {}", errorHeader, errorMessage, request);
        }
        model.addAttribute("errorHeader", errorHeader);
        model.addAttribute("errorMessage", errorMessage);
        return "error";
    }

    @Override
    public String getErrorPath() {
        return null;
    }
}
