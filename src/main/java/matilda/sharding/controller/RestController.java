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
import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PathVariable;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.*;

@org.springframework.web.bind.annotation.RestController
@RequiredArgsConstructor
@Slf4j
public class RestController {
    @Qualifier("salin07JdbcTemplate")
    private final JdbcTemplate salin07JdbcTemplate;

//    @Qualifier("salin08JdbcTemplate")
//    private final JdbcTemplate salin08JdbcTemplate;

    @Qualifier("salin09JdbcTemplate")
    private final JdbcTemplate salin09JdbcTemplate;

//    @Qualifier("salin10JdbcTemplate")
//    private final JdbcTemplate salin10JdbcTemplate;

    static String userInsertSql = "Insert into users(user_id, username, password) values(?, 'qwer1234', 'qwer1234')";
    static String serverInsertSql = "Insert into servers values(?, '8gb', '512gb', true)";
    static String linkerInsertSql = "Insert into linkers values(?, ?)";
    static String serverLinkerInsertSql = "Insert into server_linkers values(?, ?, ?)";
//    private final UserRepository userRepository;
//    private final LogRepository logRepository;
//    private final ServerRepository serverRepository;
//    private final ServerLinkerRepository serverLinkerRepository;
//    private final LinkerRepository linkerRepository;
//    static int count = 0;

    @GetMapping("/init")
    public String init() {
        Random random = new Random();
        for (int i = 2501; i <= 5000; i++) {
//            int value = random.nextInt(500) + 1;
//            salin07JdbcTemplate.update(linkerInsertSql, i,value);
//            salin08JdbcTemplate.update(linkerInsertSql, i,value);
//            salin09JdbcTemplate.update(linkerInsertSql,i, value);
//            salin10JdbcTemplate.update(linkerInsertSql, i,value);
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


    /**
     * sequence access [a ~ b]
     */
    @GetMapping("/select/sequence/{a}/{b}")
    public String selectSequence(@PathVariable("a") int a, @PathVariable("b") int b) {
        double diffTime = 0;

        for (int testCase = 0; testCase < 25; testCase++) {
            String sql = "select user_id from users where user_id between ? and ?";
            long startTIme = System.currentTimeMillis();
            salin07JdbcTemplate.queryForList(sql, new Object[]{a, b});
            salin09JdbcTemplate.queryForList(sql, new Object[]{a, b});
            long endTIme = System.currentTimeMillis();
            diffTime += (endTIme - startTIme);
        }
        return String.valueOf(diffTime / 100);
    }

    /**
     * random access (count = {count})
     */
    @GetMapping("/select/random/{count}")
    public String selectRandom(@PathVariable("count") int count) {
        double diffTime = 0.0;
        String sql = "select user_id from users where user_id = ?";
        long startTIme = System.currentTimeMillis();
        long endTIme = 0;
                Random random = new Random();
        for (int i = 0; i < count; i++) {
            long value = random.nextInt(count) + 1;
            salin07JdbcTemplate.queryForList(sql, value);
            salin09JdbcTemplate.queryForList(sql, value);
            endTIme = System.currentTimeMillis();
        }
        diffTime = (endTIme - startTIme);
        return String.valueOf(diffTime);
    }


    @GetMapping("/select/usersForLinker/{a}/{b}")
    public String selectUserForLinker(@PathVariable("a") int a, @PathVariable("b") int b) {
        String sql = "SELECT linker_id, COUNT(*) FROM linkers JOIN users USING(user_id) " +
                "WHERE linker_id BETWEEN ? AND ? " +
                "GROUP BY linker_id";
        long startTime = System.currentTimeMillis();

        for (int i = 0; i < 25; i++) {
            List<Map<String, Object>> result07 = salin07JdbcTemplate.queryForList(sql, a, b);
            List<Map<String, Object>> result09 = salin09JdbcTemplate.queryForList(sql, a, b);
            System.out.printf(result07.toString());
        }

        long endTime = System.currentTimeMillis();
        double diffTime = (endTime - startTime) / 25.0; // 각 쿼리 실행 시간의 평균을 계산
        return String.valueOf(diffTime);
    }


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
