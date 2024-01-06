package co.istad.elearningapi;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FirstController {

    @GetMapping("/admin")
    String admin() {
        return "Admin";
    }

    @GetMapping("/secure")
    String secure() {
        return "Secure Route";
    }

    @GetMapping("/public")
    String viewPublic() {
        return "Public Route";
    }

}
