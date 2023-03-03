package com.example.imaggenbackend;

import java.net.MalformedURLException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

@SpringBootApplication
@RestController
public class imgGenRestAPI {

    public static void main(String[] args) {
        SpringApplication.run(imgGenRestAPI.class, args);
    }

    @Controller
    public class HomeController {
        @RequestMapping("/")
        public String index() {
            return "index";
        }
    }

    @GetMapping("/helloget")
    public String getHello(@RequestParam(value = "name", defaultValue = "Vishal") String name,
            @RequestParam(value = "prompt", defaultValue = "No prompt") String prompt) {
        if ((prompt != "") || (prompt != null)) {
            return "Hello " + name + ", welcome to Vishal & Bimal's spring application!"
                    + "The prompt you have given is "
                    + prompt;
        } else {
            return "Hello " + name + ", welcome to Vishal's spring application!" + "You have not given any prompt";
        }
    }
    @Controller
    public class postHelloController {
        DallEClient dallEClient;
        public postHelloController() throws MalformedURLException {
            this.dallEClient = new DallEClient();
        }

        @PostMapping("/response")
        public String postHello(@RequestParam(name = "name", required = true, defaultValue = "Vishal") String name,
                @RequestParam(name = "prompt", required = true) String prompt) throws JsonMappingException, JsonProcessingException {
            // RandomImage img = new RandomImage();
            // String img_src = "https://spring.io/img/extra/quickstart-1-dark.png";
            // String img_src = this.dallEClient.imgGenerator.generateImage(prompt, 1,
            // "1024x1024");
            String img_src = this.dallEClient.imgGenerator.generateImage(prompt, 1, "1024x1024");
            System.out.println(img_src);
            return img_src;
        }
    }
}
