package io.pivotal.pal.tracker;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Value;

@RestController
public class WelcomeController {


    private String welcomeMessage;


    public WelcomeController(@Value("${welcome.message}") String message){
        this.welcomeMessage = message;
    }

    @GetMapping("/")
    public String sayHello() {
        return welcomeMessage;
    }
}
