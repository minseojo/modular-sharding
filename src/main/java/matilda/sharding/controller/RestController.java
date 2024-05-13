package matilda.sharding.controller;

//import lombok.RequiredArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
//import matilda.sharding.domain.*;
//import matilda.sharding.repository.*;
//import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.List;

@org.springframework.web.bind.annotation.RestController
@RequiredArgsConstructor
@Slf4j
public class RestController {
    @Qualifier("salin07JdbcTemplate")
    private final JdbcTemplate salin07JdbcTemplate;

    @Qualifier("salin08JdbcTemplate")
    private final JdbcTemplate salin08JdbcTemplate;

    @Qualifier("salin09JdbcTemplate")
    private final JdbcTemplate salin09JdbcTemplate;

    @Qualifier("salin10JdbcTemplate")
    private final JdbcTemplate salin10JdbcTemplate;

    String userInsertSql = "Insert into users(user_id, username, password) values(?, 'qwer1234', 'qwer1234')";
    String serverInsertSql = "Insert into servers values(?, '8gb', '512gb', true)";
    String linkerInsertSql = "Insert into linkers values(?, ?)";
    String serverLinkerInsertSql = "Insert into server_linkers values(?, ?, ?)";


//    private final UserRepository userRepository;
//    private final LogRepository logRepository;
//    private final ServerRepository serverRepository;
//    private final ServerLinkerRepository serverLinkerRepository;
//    private final LinkerRepository linkerRepository;
//    static int count = 0;

    @GetMapping("/init")
    public String init() {
        for (int i = 2; i < 10; i++) {
            salin07JdbcTemplate.update(userInsertSql, i);
            salin08JdbcTemplate.update(userInsertSql, i);
            salin09JdbcTemplate.update(userInsertSql, i);
            salin10JdbcTemplate.update(userInsertSql, i);
//            salin09JdbcTemplate.update(serverInsertSql, "172.16.5" + i);
//            salin10JdbcTemplate.update(serverInsertSql, "172.16.5" + i);
//            salin09JdbcTemplate.update(linkerInsertSql, i, i);
//            salin10JdbcTemplate.update(linkerInsertSql, i, i);
//            salin09JdbcTemplate.update(serverLinkerInsertSql, i, "172.16.5" + i, i);
//            salin10JdbcTemplate.update(serverLinkerInsertSql, i, "172.16.5" + i, i);
        }
        return "init";
    }
//    @GetMapping("/insert")
//    public String insert() throws SQLException {
//        log.info("hello");
//        List<Linker> all = linkerRepository.findAll();
//        for (int i = 0; i < 10; i++) {
//            logRepository.save(new Log( all.get(i%10)));
//        }
//
////        userService.save(new User((long) count++, "minseo", "qwer1234"));
//        return "asd";
//    }
//    @GetMapping("/insert/{userCount}")
//    public String insert(@PathVariable("userCount") int userCount) {
//        log.info("insert into 작업 실행 : {}명의 유저 추가", userCount);
//        List<Linker> all = linkerRepository.findAll();
//        for (int i = 0; i < userCount; i++) {
//            logRepository.save(new Log(all.get(i%10)));
//        }
//
//        return "ok";
//    }
//
//    @GetMapping("/select/sequence/{logCount}")
//    public String selectSequence(@PathVariable("logCount") int logCount) {
//        log.info("select 작업 실행 : {}개의 로그 순차 조회", logCount);
//        for (int i = 1; i <= logCount; i++) {
//            logRepository.findById((long) i);
//        }
//
//        return "ok";
//    }

}
