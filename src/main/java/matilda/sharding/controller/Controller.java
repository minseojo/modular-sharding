package matilda.sharding.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;

@org.springframework.stereotype.Controller
@RequiredArgsConstructor
public class Controller {

    @GetMapping("/")
    public String home() {
        return "index";
    }
}
