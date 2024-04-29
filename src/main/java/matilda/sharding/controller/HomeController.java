package matilda.sharding.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import matilda.sharding.domain.User;
import matilda.sharding.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class HomeController {

    private final UserService userService;
    static int count = 0;

    @GetMapping("/")
    public String home() {
        userService.save(new User((long) count++, "minseo", "qwer1234"));
        return "asd";
    }
}
