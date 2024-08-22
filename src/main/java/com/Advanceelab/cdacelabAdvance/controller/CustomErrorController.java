package com.Advanceelab.cdacelabAdvance.controller;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class CustomErrorController implements ErrorController {

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request) {
//        // Determine the error status code
//        int statusCode = (int) request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
//
//        // Customize the response based on the status code without exposing sensitive details
//        if (statusCode == 404) {
//            return "error";
//        } else {
            return "error";
//        }
    }

    public String getErrorPath() {
        // This method must be implemented to return the path for handling errors.
        return "/error";
    }
}

