package com.example.imaggenbackend;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GetImageController {

    @RequestMapping("/")
    public String getImage() {
        return "I have no idea what I'm doing.";
    }
}