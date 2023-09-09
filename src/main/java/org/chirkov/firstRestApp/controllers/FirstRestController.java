package org.chirkov.firstRestApp.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


// @RestController = @Controller + mark @ResponseBody from every method this @Controller.
@RestController
@RequestMapping("/api")
public class FirstRestController {

//    @ResponseBody // если не указывать RestController
    @GetMapping("/sayHello")
    public String sayHello() {
        return "hello world";
    }
}
