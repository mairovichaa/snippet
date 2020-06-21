package com.amairovi;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    public TestController(){
        System.out.println("here");
    }

    @GetMapping("/test")
    public String test() {
        return "<h1>Hello, world!</h1>";
    }

}
