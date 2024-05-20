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


    /**
     * 순차 접근 [에이, 비]
     * 에스큐엘 *, 로그 아이디 본인이 설정
     */
    @GetMapping("/shard1/select/sequence/{begin}/{end}")
    public String selectSequence2(@PathVariable("begin") int begin, @PathVariable("end") int end) {
        double diffTime = 0;
        String sql = "select * from logs where log_id between ? and ?";
        double startTIme = System.currentTimeMillis();
        salin08JdbcTemplate.queryForList(sql, new Object[]{begin, end});
        double endTIme = System.currentTimeMillis();
        diffTime += (endTIme - startTIme);
        return String.valueOf(diffTime);
    }

    @GetMapping("/shard2/select/sequence/{begin}/{end}")
    public String selectSequence(@PathVariable("begin") int a, @PathVariable("end") int b) {
        double diffTime = 0;
        String sql = "select * from logs where log_id between ? and ?";
        double startTIme = System.currentTimeMillis();
        salin07JdbcTemplate.queryForList(sql, new Object[]{a, b});
        salin09JdbcTemplate.queryForList(sql, new Object[]{a, b});
        double endTIme = System.currentTimeMillis();
        diffTime += (endTIme - startTIme);
        return String.valueOf(diffTime);
    }

    /**
     * 스타트, 엔드 시간 범위에 대해 조회하기
     * *, 로그 아이디 에스큐엘은 본인이 다시 쓰기
     */

    // 스타트, 엔드는 문서에 써있음
    @GetMapping("/shard1/select/sequence/created-at/{startTime}/{endTime}")
    public String selectSequenceTime(@PathVariable("startTime") String start, @PathVariable("endTime") String end) {
        double diffTime = 0;
        String sql = "select log_id from logs where created_at between ? and ?";
        double startTIme = System.currentTimeMillis();
        salin08JdbcTemplate.queryForList(sql, new Object[]{start, end});
        double endTIme = System.currentTimeMillis();
        diffTime += (endTIme - startTIme);
        return String.valueOf(diffTime);
    }
    @GetMapping("/shard2/select/sequence/created-at/{startTime}/{endTime}")
    public String selectSequenceTime2(@PathVariable("startTime") String start, @PathVariable("endTime") String end) {
        double diffTime = 0;
        String sql = "select log_id from logs where created_at between ? and ?";
        double startTIme = System.currentTimeMillis();
        salin07JdbcTemplate.queryForList(sql, new Object[]{start, end});
        salin09JdbcTemplate.queryForList(sql, new Object[]{start, end});
        double endTIme = System.currentTimeMillis();
        diffTime += (endTIme - startTIme);
        return String.valueOf(diffTime);
    }

    /**
     * 로그 테이블에서 해당 링커 아이디에 대한 정보 가져오기
     * 에스큐엘 *, 로그 아이디는 본인이 바꾸기
     */
    @GetMapping("/shard1/select/random/linker/{linkerId}")
    public String selectIndexRandom(@PathVariable int linkerId) {
        double diffTime = 0.0;
        String sql = "select * from logs where linker_id = ?";
        long startTIme = System.currentTimeMillis();
        salin08JdbcTemplate.queryForList(sql, linkerId);
        long endTIme = System.currentTimeMillis();
        diffTime = (endTIme - startTIme);
        return String.valueOf(diffTime);
    }
    @GetMapping("/shard2/select/random/linker/{linkerId}")
    public String selectIndexRandom2(@PathVariable int linkerId) {
        double diffTime = 0.0;
        String sql = "select * from logs where linker_id = ?";
        long startTIme = System.currentTimeMillis();
        int shardKey = 2;
        if (linkerId % shardKey == 0) {
            salin07JdbcTemplate.queryForList(sql, linkerId);
        } else if (linkerId % shardKey == 1) {
            salin09JdbcTemplate.queryForList(sql, linkerId);
        }
        long endTIme = System.currentTimeMillis();
        diffTime = (endTIme - startTIme);
        return String.valueOf(diffTime);
    }



    @GetMapping("/shard1/select/userForLogType/{type}")
    public String selectUserForLog(@PathVariable String type) {
        String sql = "SELECT * " +
                " FROM (select user_id from logs join linkers using(linker_id)" +
                " where log_type = ?) E join users U using (user_id)";
        long startTime = System.currentTimeMillis();
        salin08JdbcTemplate.queryForList(sql, type);
        long endTime = System.currentTimeMillis();
        double diffTime = (endTime - startTime);// 각 쿼리 실행 시간의 평균을 계산
        return String.valueOf(diffTime);
    }

    @GetMapping("/shard2/select/userForLogType/{type}")
    public String selectUserForLog2(@PathVariable String type) {
        String sql = "SELECT * " +
                " FROM (select user_id from logs join linkers using(linker_id)" +
                " where log_type = ?) E join users U using (user_id)";
        long startTime = System.currentTimeMillis();
        salin07JdbcTemplate.queryForList(sql, type);
        salin09JdbcTemplate.queryForList(sql, type);
        long endTime = System.currentTimeMillis();
        double diffTime = (endTime - startTime);// 각 쿼리 실행 시간의 평균을 계산
        return String.valueOf(diffTime);
    }

    @GetMapping("/shard1/select/logForUser/{userId}")
    public String selectLogForUser(@PathVariable("userId") int userId) {
        String sql = "select * from (select linker_id from linkers join users using(user_id) " +
                " where user_id = ?) as LKID" +
                " join logs using(linker_id)";
        long startTime = System.currentTimeMillis();
        salin08JdbcTemplate.queryForList(sql, userId);
        long endTime = System.currentTimeMillis();
        double diffTime = (endTime - startTime);
        return String.valueOf(diffTime);
    }

    @GetMapping("/shard2/select/logForUser/{userId}")
    public String selectLogForUser2(@PathVariable("userId") int userId) {
        String sql = "select * from (select linker_id from linkers join users using(user_id) " +
                " where user_id = ?) as LKID" +
                " join logs using(linker_id)";
        long startTime = System.currentTimeMillis();
        salin07JdbcTemplate.queryForList(sql, userId);
        salin09JdbcTemplate.queryForList(sql, userId);
        long endTime = System.currentTimeMillis();
        double diffTime = (endTime - startTime);
        return String.valueOf(diffTime);
    }

}
