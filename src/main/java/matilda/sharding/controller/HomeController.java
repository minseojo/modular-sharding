package matilda.sharding.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import matilda.sharding.domain.*;
import matilda.sharding.repository.*;
import matilda.sharding.service.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class HomeController {

    private final UserRepository userRepository;
    private final LogRepository logRepository;
    private final ServerRepository serverRepository;
    private final ServerLinkerRepository serverLinkerRepository;
    private final LinkerRepository linkerRepository;
    static int count = 0;

    @GetMapping("/init")
    public String init() {
        for (int i = 0; i < 10; i++) {
            User user = new User((long) i, "qwer1234", "qwer1234");
            userRepository.save(user);
            Server server = new Server("172.16.5" + i, "8gb", "512gb", true);
            serverRepository.save(server);
            Linker linker = new Linker((long) i, user);
            linkerRepository.save(linker);
            ServerLinker serverLinker = new ServerLinker(server, linker);
            serverLinkerRepository.save(serverLinker);
        }
        return "init";
    }
    @GetMapping("/")
    public String home() throws SQLException {
        log.info("hello");
        List<Linker> all = linkerRepository.findAll();
        for (int i = 0; i < 10; i++) {
            logRepository.save(new Log( all.get(i%10)));
        }

//        userService.save(new User((long) count++, "minseo", "qwer1234"));
        return "asd";
    }
}
