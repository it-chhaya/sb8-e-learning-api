package co.istad.elearningapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@SpringBootApplication
@Controller
public class ELearningApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(ELearningApiApplication.class, args);
    }

    @GetMapping("/test")
    String testTemplate() {
        return "verify-email";
    }

}
