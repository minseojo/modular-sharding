package matilda.sharding.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@org.springframework.web.bind.annotation.RestController
@Slf4j
public class RestController {

    private final JdbcTemplate salin07JdbcTemplate;
    private final JdbcTemplate salin08JdbcTemplate;
    private final JdbcTemplate salin09JdbcTemplate;
    private final JdbcTemplate salin07Shard3Template;
    private final JdbcTemplate salin08Shard3Template;
    private final JdbcTemplate salin09Shard3Template;

    public RestController(@Qualifier("salin07JdbcTemplate") JdbcTemplate salin07JdbcTemplate,
                          @Qualifier("salin08JdbcTemplate") JdbcTemplate salin08JdbcTemplate,
                          @Qualifier("salin09JdbcTemplate") JdbcTemplate salin09JdbcTemplate,
                          @Qualifier("salin07Shard3JdbcTemplate") JdbcTemplate salin07Shard3Template,
                          @Qualifier("salin08Shard3JdbcTemplate") JdbcTemplate salin08Shard3Template,
                          @Qualifier("salin09Shard3JdbcTemplate") JdbcTemplate salin09Shard3Template) {
        this.salin07JdbcTemplate = salin07JdbcTemplate;
        this.salin08JdbcTemplate = salin08JdbcTemplate;
        this.salin09JdbcTemplate = salin09JdbcTemplate;
        this.salin07Shard3Template = salin07Shard3Template;
        this.salin08Shard3Template = salin08Shard3Template;
        this.salin09Shard3Template = salin09Shard3Template;
    }

//    @Qualifier("salin10JdbcTemplate")
//    private final JdbcTemplate salin10JdbcTemplate;

    private final ExecutorService executorService = Executors.newFixedThreadPool(3);

    @GetMapping("/shard1/{use_index}/select/sequence/{begin}/{end}")
    public String selectSequence2(@PathVariable("use_index") boolean useIndex, @PathVariable("begin") int begin, @PathVariable("end") int end) {
        double diffTime = 0;
        String sql = "select SQL_NO_CACHE * from logs where log_id between ? and ?";
        if (useIndex) {
            sql = "select SQL_NO_CACHE log_id from logs where log_id between ? and ?";
        }
        double startTime = System.currentTimeMillis();
        salin08JdbcTemplate.queryForList(sql, new Object[]{begin, end});
        double endTime = System.currentTimeMillis();
        diffTime += (endTime - startTime);
        return String.valueOf(diffTime);
    }

    @GetMapping("/shard2/{use_index}/select/sequence/{begin}/{end}")
    public String selectSequence(@PathVariable("use_index") boolean useIndex, @PathVariable("begin") int begin, @PathVariable("end") int end) {
        double diffTime = 0;
        String sql = "select SQL_NO_CACHE * from logs where log_id between ? and ?";
        if (useIndex) {
            sql = "select SQL_NO_CACHE log_id from logs where log_id between ? and ?";
        }

        final String finalSql = sql;

        CompletableFuture<Void> future1 = CompletableFuture.runAsync(() -> salin07JdbcTemplate.queryForList(finalSql, new Object[]{begin, end}), executorService);
        CompletableFuture<Void> future2 = CompletableFuture.runAsync(() -> salin09JdbcTemplate.queryForList(finalSql, new Object[]{begin, end}), executorService);

        double startTime = System.currentTimeMillis();
        CompletableFuture.allOf(future1, future2).join();
        double endTime = System.currentTimeMillis();
        diffTime += (endTime - startTime);
        return String.valueOf(diffTime);
    }

    @GetMapping("/shard3/{use_index}/select/sequence/{begin}/{end}")
    public String selectSequence3(@PathVariable("use_index") boolean useIndex, @PathVariable("begin") int begin, @PathVariable("end") int end) {
        double diffTime = 0;
        String sql = "select SQL_NO_CACHE * from logs where log_id between ? and ?";
        if (useIndex) {
            sql = "select SQL_NO_CACHE log_id from logs where log_id between ? and ?";
        }

        final String finalSql = sql;
        CompletableFuture<Void> future1 = CompletableFuture.runAsync(() -> salin07Shard3Template.queryForList(finalSql, new Object[]{begin, end}), executorService);
        CompletableFuture<Void> future2 = CompletableFuture.runAsync(() -> salin08Shard3Template.queryForList(finalSql, new Object[]{begin, end}), executorService);
        CompletableFuture<Void> future3 = CompletableFuture.runAsync(() -> salin09Shard3Template.queryForList(finalSql, new Object[]{begin, end}), executorService);


        double startTime = System.currentTimeMillis();
//        salin07Shard3Template.queryForList(sql, new Object[]{begin, end});
//        salin08Shard3Template.queryForList(sql, new Object[]{begin, end});
//        salin09Shard3Template.queryForList(sql, new Object[]{begin, end});
        CompletableFuture.allOf(future1, future2, future3).join();
        double endTime = System.currentTimeMillis();
        diffTime += (endTime - startTime);
        return String.valueOf(diffTime);
    }

    @GetMapping("/shard1/{use_index}/select/sequence/created-at/{startTime}/{endTime}")
    public String selectSequenceTime(@PathVariable("use_index") boolean useIndex, @PathVariable("startTime") String start, @PathVariable("endTime") String end) {
        double diffTime = 0;
        String sql = "select SQL_NO_CACHE * from logs where created_at between ? and ?";
        if (useIndex) {
            sql = "select SQL_NO_CACHE log_id from logs where created_at between ? and ?";
        }
        double startTime = System.currentTimeMillis();
        salin08JdbcTemplate.queryForList(sql, new Object[]{start, end});
        double endTime = System.currentTimeMillis();
        diffTime += (endTime - startTime);
        return String.valueOf(diffTime);
    }

    @GetMapping("/shard2/{use_index}/select/sequence/created-at/{startTime}/{endTime}")
    public String selectSequenceTime2(@PathVariable("use_index") boolean useIndex, @PathVariable("startTime") String start, @PathVariable("endTime") String end) {
        System.out.println(useIndex + " " + start + " " + end);
        double diffTime = 0;
        String sql = "select SQL_NO_CACHE * from logs where created_at between ? and ?";
        if (useIndex) {
            sql = "select SQL_NO_CACHE log_id from logs where created_at between ? and ?";
        }

        final String finalSql = sql;

        CompletableFuture<Void> future1 = CompletableFuture.runAsync(() -> salin07JdbcTemplate.queryForList(finalSql, new Object[]{start, end}), executorService);
        CompletableFuture<Void> future2 = CompletableFuture.runAsync(() -> salin09JdbcTemplate.queryForList(finalSql, new Object[]{start, end}), executorService);

        double startTime = System.currentTimeMillis();
        CompletableFuture.allOf(future1, future2).join();
        double endTime = System.currentTimeMillis();
        diffTime += (endTime - startTime);
        System.out.println(diffTime);
        return String.valueOf(diffTime);
    }

    @GetMapping("/shard3/{use_index}/select/sequence/created-at/{startTime}/{endTime}")
    public String selectSequenceTime3(@PathVariable("use_index") boolean useIndex, @PathVariable("startTime") String start, @PathVariable("endTime") String end) {
        System.out.println(useIndex + " " + start + " " + end);
        double diffTime = 0;
        String sql = "select * from logs where created_at between ? and ?";
        if (useIndex) {
            sql = "select log_id from logs where created_at between ? and ?";
        }

        final String finalSql = sql;

        CompletableFuture<Void> future1 = CompletableFuture.runAsync(() -> salin07Shard3Template.queryForList(finalSql, new Object[]{start, end}), executorService);
        CompletableFuture<Void> future2 = CompletableFuture.runAsync(() -> salin08Shard3Template.queryForList(finalSql, new Object[]{start, end}), executorService);
        CompletableFuture<Void> future3 = CompletableFuture.runAsync(() -> salin09Shard3Template.queryForList(finalSql, new Object[]{start, end}), executorService);

        double startTime = System.currentTimeMillis();
        CompletableFuture.allOf(future1, future2, future3).join();
        double endTime = System.currentTimeMillis();
        diffTime += (endTime - startTime);
        System.out.println(diffTime);
        return String.valueOf(diffTime);
    }

    @GetMapping("/shard1/{use_index}/select/random/linker/{linkerId}")
    public String selectIndexRandom(@PathVariable("use_index") boolean useIndex, @PathVariable int linkerId) {
        double diffTime = 0.0;
        String sql = "select SQL_NO_CACHE * from logs where linker_id = ?";
        if (useIndex) {
            sql = "select SQL_NO_CACHE log_id from logs where linker_id = ?";
        }
        long startTime = System.currentTimeMillis();
        salin08JdbcTemplate.queryForList(sql, linkerId);
        long endTime = System.currentTimeMillis();
        diffTime = (endTime - startTime);
        return String.valueOf(diffTime);
    }

    @GetMapping("/shard2/{use_index}/select/random/linker/{linkerId}")
    public String selectIndexRandom2(@PathVariable("use_index") boolean useIndex, @PathVariable int linkerId) {
        double diffTime = 0.0;
        String sql = "select SQL_NO_CACHE * from logs where linker_id = ?";
        if (useIndex) {
            sql = "select SQL_NO_CACHE log_id from logs where linker_id = ?";
        }

        int shardKey = 2;
        long startTime = System.currentTimeMillis();
        if (linkerId % shardKey == 0) {
            salin07JdbcTemplate.queryForList(sql, linkerId);
        } else {
            salin09JdbcTemplate.queryForList(sql, linkerId);
        }
        long endTime = System.currentTimeMillis();
        diffTime = (endTime - startTime);
        return String.valueOf(diffTime);
    }

    @GetMapping("/shard3/{use_index}/select/random/linker/{linkerId}")
    public String selectIndexRandom3(@PathVariable("use_index") boolean useIndex, @PathVariable int linkerId) {
        double diffTime = 0.0;
        String sql = "select SQL_NO_CACHE * from logs where linker_id = ?";
        if (useIndex) {
            sql = "select SQL_NO_CACHE log_id from logs where linker_id = ?";
        }

        int shardKey = 3;
        long startTime = System.currentTimeMillis();
        if (linkerId % shardKey == 0) {
            salin07Shard3Template.queryForList(sql, linkerId);
        } else if (linkerId % shardKey == 1) {
            salin08Shard3Template.queryForList(sql, linkerId);
        } else {
            salin09Shard3Template.queryForList(sql, linkerId);
        }
        long endTime = System.currentTimeMillis();
        diffTime = (endTime - startTime);
        return String.valueOf(diffTime);
    }

    @GetMapping("/shard1/{use_index}/select/userForLogType/{type}")
    public String selectUserForLog(@PathVariable("use_index") boolean useIndex, @PathVariable String type) {
        String sql = "SELECT * " +
                " FROM (select user_id from logs join linkers using(linker_id)" +
                " where log_type = ?) E join users U using (user_id)";
        if (useIndex) {
            sql = "SELECT user_id " +
                    " FROM (select user_id from logs join linkers using(linker_id)" +
                    " where log_type = ?) E join users U using (user_id)";
        }
        long startTime = System.currentTimeMillis();
        salin08JdbcTemplate.queryForList(sql, type);
        long endTime = System.currentTimeMillis();
        double diffTime = (endTime - startTime);
        return String.valueOf(diffTime);
    }

    @GetMapping("/shard2/{use_index}/select/userForLogType/{type}")
    public String selectUserForLog2(@PathVariable("use_index") boolean useIndex, @PathVariable String type) {
        String sql = "SELECT * " +
                " FROM (select user_id from logs join linkers using(linker_id)" +
                " where log_type = ?) E join users U using (user_id)";
        if (useIndex) {
            sql = "SELECT user_id " +
                    " FROM (select user_id from logs join linkers using(linker_id)" +
                    " where log_type = ?) E join users U using (user_id)";
        }

        final String finalSql = sql;

        CompletableFuture<Void> future1 = CompletableFuture.runAsync(() -> salin07JdbcTemplate.queryForList(finalSql, type), executorService);
        CompletableFuture<Void> future2 = CompletableFuture.runAsync(() -> salin09JdbcTemplate.queryForList(finalSql, type), executorService);

        long startTime = System.currentTimeMillis();
        CompletableFuture.allOf(future1, future2).join();
        long endTime = System.currentTimeMillis();
        double diffTime = (endTime - startTime);
        return String.valueOf(diffTime);
    }

    @GetMapping("/shard3/{use_index}/select/userForLogType/{type}")
    public String selectUserForLog3(@PathVariable("use_index") boolean useIndex, @PathVariable String type) {
        String sql = "SELECT * " +
                " FROM (select user_id from logs join linkers using(linker_id)" +
                " where log_type = ?) E join users U using (user_id)";
        if (useIndex) {
            sql = "SELECT user_id " +
                    " FROM (select user_id from logs join linkers using(linker_id)" +
                    " where log_type = ?) E join users U using (user_id)";
        }

        final String finalSql = sql;

        CompletableFuture<Void> future1 = CompletableFuture.runAsync(() -> salin07Shard3Template.queryForList(finalSql, type), executorService);
        CompletableFuture<Void> future2 = CompletableFuture.runAsync(() -> salin08Shard3Template.queryForList(finalSql, type), executorService);
        CompletableFuture<Void> future3 = CompletableFuture.runAsync(() -> salin09Shard3Template.queryForList(finalSql, type), executorService);


        long startTime = System.currentTimeMillis();
        CompletableFuture.allOf(future1, future2, future3).join();
        long endTime = System.currentTimeMillis();
        double diffTime = (endTime - startTime);
        return String.valueOf(diffTime);
    }

    @GetMapping("/shard1/{use_index}/select/logForUser/{userId}")
    public String selectLogForUser(@PathVariable("use_index") boolean useIndex, @PathVariable("userId") int userId) {
        String sql = "select * from (select linker_id from linkers join users using(user_id) " +
                " where user_id = ?) as LKID" +
                " join logs using(linker_id)";
        if (useIndex) {
            sql = "select log_id from (select linker_id from linkers join users using(user_id) " +
                    " where user_id = ?) as LKID" +
                    " join logs using(linker_id)";
        }
        long startTime = System.currentTimeMillis();
        salin08JdbcTemplate.queryForList(sql, userId);
        long endTime = System.currentTimeMillis();
        double diffTime = (endTime - startTime);
        return String.valueOf(diffTime);
    }

    @GetMapping("/shard2/{use_index}/select/logForUser/{userId}")
    public String selectLogForUser2(@PathVariable("use_index") boolean useIndex, @PathVariable("userId") int userId) {
        String sql = "select * from (select linker_id from linkers join users using(user_id) " +
                " where user_id = ?) as LKID" +
                " join logs using(linker_id)";
        if (useIndex) {
            sql = "select log_id from (select linker_id from linkers join users using(user_id) " +
                    " where user_id = ?) as LKID" +
                    " join logs using(linker_id)";
        }

        final String finalSql = sql;

        CompletableFuture<Void> future1 = CompletableFuture.runAsync(() -> salin07JdbcTemplate.queryForList(finalSql, userId), executorService);
        CompletableFuture<Void> future2 = CompletableFuture.runAsync(() -> salin09JdbcTemplate.queryForList(finalSql, userId), executorService);

        long startTime = System.currentTimeMillis();
        CompletableFuture.allOf(future1, future2).join();
        long endTime = System.currentTimeMillis();
        double diffTime = (endTime - startTime);
        return String.valueOf(diffTime);
    }

    @GetMapping("/shard3/{use_index}/select/logForUser/{userId}")
    public String selectLogForUser3(@PathVariable("use_index") boolean useIndex, @PathVariable("userId") int userId) {
        String sql = "select * from (select linker_id from linkers join users using(user_id) " +
                " where user_id = ?) as LKID" +
                " join logs using(linker_id)";
        if (useIndex) {
            sql = "select log_id from (select linker_id from linkers join users using(user_id) " +
                    " where user_id = ?) as LKID" +
                    " join logs using(linker_id)";
        }

        final String finalSql = sql;

        CompletableFuture<Void> future1 = CompletableFuture.runAsync(() -> salin07Shard3Template.queryForList(finalSql, userId), executorService);
        CompletableFuture<Void> future2 = CompletableFuture.runAsync(() -> salin08Shard3Template.queryForList(finalSql, userId), executorService);
        CompletableFuture<Void> future3 = CompletableFuture.runAsync(() -> salin09Shard3Template.queryForList(finalSql, userId), executorService);

        long startTime = System.currentTimeMillis();
        CompletableFuture.allOf(future1, future2, future3).join();
        long endTime = System.currentTimeMillis();
        double diffTime = (endTime - startTime);
        return String.valueOf(diffTime);
    }
}
