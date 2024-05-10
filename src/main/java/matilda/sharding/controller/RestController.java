package matilda.sharding.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import matilda.sharding.domain.*;
import matilda.sharding.repository.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.sql.SQLException;
import java.util.List;
import java.util.Random;

@org.springframework.web.bind.annotation.RestController
@RequiredArgsConstructor
@Slf4j
public class RestController {

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
    @GetMapping("/insert")
    public String insert() throws SQLException {
        log.info("hello");
        List<Linker> all = linkerRepository.findAll();
        for (int i = 0; i < 10; i++) {
            logRepository.save(new Log( all.get(i%10)));
        }

//        userService.save(new User((long) count++, "minseo", "qwer1234"));
        return "asd";
    }
    @GetMapping("/insert/{userCount}")
    public String insert(@PathVariable("userCount") int userCount) {
        log.info("insert into 작업 실행 : {}명의 유저 추가", userCount);
        List<Linker> all = linkerRepository.findAll();
        for (int i = 0; i < userCount; i++) {
            logRepository.save(new Log(all.get(i%10)));
        }

        return "ok";
    }

    @GetMapping("/select/sequence/{logCount}")
    public String selectSequence(@PathVariable("logCount") int logCount) {
        log.info("select 작업 실행 : {}개의 로그 순차 조회", logCount);
        for (int i = 1; i <= logCount; i++) {
            logRepository.findById((long) i);
        }
        return "ok";
    }

    @GetMapping("/select/random/{logCount}")
    public String selectRandom(@PathVariable("logCount") int logCount) {
        Random random = new Random();
        log.info("select 작업 실행 : {}개의 로그 랜덤 조회", logCount);

        for (int i = 1; i <= logCount; i++) {
            long randomIndex= random.nextLong(logCount) + 1;
            logRepository.findById(randomIndex);
        }
        return "ok";
    }

}
