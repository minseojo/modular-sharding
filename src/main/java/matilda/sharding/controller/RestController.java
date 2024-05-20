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

    @Qualifier("salin08JdbcTemplate")
    private final JdbcTemplate salin08JdbcTemplate;

    @Qualifier("salin09JdbcTemplate")
    private final JdbcTemplate salin09JdbcTemplate;

    @Qualifier("salin10JdbcTemplate")
    private final JdbcTemplate salin10JdbcTemplate;

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
     * 순차 접근 [에이, 비]
     * 에스큐엘 *, 로그 아이디 본인이 설정
     */
    @GetMapping("/select/sequence/{a}/{b}")
    public String selectSequence(@PathVariable("a") int a, @PathVariable("b") int b) {
        double diffTime = 0;

        String sql = "select * from logs where log_id between ? and ?";

        double startTIme = System.currentTimeMillis();

        salin07JdbcTemplate.queryForList(sql, new Object[]{a, b});
        //salin08JdbcTemplate.queryForList(sql, new Object[]{a, b});
        salin09JdbcTemplate.queryForList(sql, new Object[]{a, b});
        //salin10JdbcTemplate.queryForList(sql, new Object[]{a, b});
        double endTIme = System.currentTimeMillis();
        diffTime += (endTIme - startTIme);
        return String.valueOf(diffTime);
    }

    /**
     * 스타트, 엔드 시간 범위에 대해 조회하기
     * *, 로그 아이디 에스큐엘은 본인이 다시 쓰기
     */

    // 스타트, 엔드는 문서에 써있음
    //
    @GetMapping("/select/sequence/created-at/{start}/{end}")
    public String selectSequence(@PathVariable("start") String start, @PathVariable("end") String end) {
        double diffTime = 0;

        String sql = "select log_id from logs where created_at between ? and ?";

        double startTIme = System.currentTimeMillis();
        salin07JdbcTemplate.queryForList(sql, new Object[]{start, end});
        //salin08JdbcTemplate.queryForList(sql, new Object[]{start, end});
        salin09JdbcTemplate.queryForList(sql, new Object[]{start, end});
        //salin10JdbcTemplate.queryForList(sql, new Object[]{start, end});
        double endTIme = System.currentTimeMillis();
        diffTime += (endTIme - startTIme);
        return String.valueOf(diffTime);
    }

    /**
     * 로그 테이블에서 해당 링커 아이디에 대한 정보 가져오기
     * 에스큐엘 *, 로그 아이디는 본인이 바꾸기
     */
    @GetMapping("/select/random/linker")
    public String selectIndexRandom() {

        double diffTime = 0.0;
        String sql = "select * from logs where linker_id = ?";
        long startTIme = System.currentTimeMillis();
        long endTIme = 0;
        Random random = new Random();
        int shardKey = 4;
        long randomLinkerId = random.nextInt(2500) + 1;
        if (randomLinkerId % shardKey == 0) {
            salin07JdbcTemplate.queryForList(sql, randomLinkerId);
        } else if (randomLinkerId % shardKey == 1) {
            salin08JdbcTemplate.queryForList(sql, randomLinkerId);
        } else if (randomLinkerId % shardKey == 2) {
            salin09JdbcTemplate.queryForList(sql, randomLinkerId);
        } else {
            salin10JdbcTemplate.queryForList(sql, randomLinkerId);
        }

        endTIme = System.currentTimeMillis();
        diffTime = (endTIme - startTIme);
        return String.valueOf(diffTime);
    }



    @GetMapping("/select/userForLogType/{type}")
    public String selectUserForLog(@PathVariable String type) {

        String sql = "SELECT * " +
                " FROM (select user_id from logs join linkers using(linker_id)" +
                " where log_type = ?) E join users U using (user_id)";
        long startTime = System.currentTimeMillis();


        salin07JdbcTemplate.queryForList(sql, type);
        salin08JdbcTemplate.queryForList(sql, type);
        salin09JdbcTemplate.queryForList(sql, type);
        salin10JdbcTemplate.queryForList(sql, type);

        long endTime = System.currentTimeMillis();
        double diffTime = (endTime - startTime);// 각 쿼리 실행 시간의 평균을 계산
        return String.valueOf(diffTime);
    }

    @GetMapping("/select/logForUser/{userId}")
    public String selectLogForUser(@PathVariable("userId") int userId) {

        String sql = "select * from (select linker_id from linkers join users using(user_id) " +
                " where user_id = ?) as LKID" +
                " join logs using(linker_id)";
        long startTime = System.currentTimeMillis();

        salin07JdbcTemplate.queryForList(sql, userId);
        salin08JdbcTemplate.queryForList(sql, userId);
        salin09JdbcTemplate.queryForList(sql, userId);
        salin10JdbcTemplate.queryForList(sql, userId);

        long endTime = System.currentTimeMillis();
        double diffTime = (endTime - startTime);
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
