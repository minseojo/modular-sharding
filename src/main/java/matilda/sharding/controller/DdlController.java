package matilda.sharding.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.file.attribute.UserPrincipal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Random;

@RestController
@RequiredArgsConstructor
public class DdlController {
    @Qualifier("salin07JdbcTemplate")
    private final JdbcTemplate salin07JdbcTemplate;

    @Qualifier("salin08JdbcTemplate")
    private final JdbcTemplate salin08JdbcTemplate;

    @Qualifier("salin09JdbcTemplate")
    private final JdbcTemplate salin09JdbcTemplate;

    @Qualifier("salin10JdbcTemplate")
    private final JdbcTemplate salin10JdbcTemplate;

    public String logInsertSql = "insert into logs(log_id, linker_id, log_type, message) values(?,?,?,?)";
    public String userInsertSql = "insert into users(user_id, username, password) values(?,?,?)";
    public String linkerInsertSql = "insert into linkers(linker_id, user_id) values(?,?)";

    String[] logTypes = {"application", "system", "security", "performance", "error", "audit", "access"};
    public int MAX_LINKER_SIZE = 1000;
    public int MAX_USER_SIZE = 200;

    @GetMapping("/init/logs")
    public String initLogs() {
        int[] testCase = {960_000};

        Random random = new Random();

        // 2tl 36qns tlwkr 50ch
        for (int i = 1; i <= testCase[0]; i++) {
            int linkerIdRandomValue = random.nextInt(MAX_LINKER_SIZE) + 1;
            int logTypeRandomValue = random.nextInt(logTypes.length);
            salin08JdbcTemplate.update(logInsertSql, i, linkerIdRandomValue, logTypes[logTypeRandomValue], "");
            if (linkerIdRandomValue % 2 == 1) {
                salin07JdbcTemplate.update(logInsertSql, i, linkerIdRandomValue, logTypes[logTypeRandomValue], "");
            }
            else {
                salin09JdbcTemplate.update(logInsertSql, i, linkerIdRandomValue, logTypes[logTypeRandomValue], "");
            }
        }

        return "init logs ok";
    }


    @GetMapping("/init/linkers-users")
    public String initLinkers() {
        int testCase = MAX_LINKER_SIZE;

        Random random = new Random();

        // 2tl 36qns tlwkr 50ch

        for (int userIdRandomValue = 1; userIdRandomValue <= MAX_USER_SIZE; userIdRandomValue++) {
            String userInfo = String.valueOf(userIdRandomValue);
//            salin08JdbcTemplate.update(userInsertSql, userIdRandomValue, userInfo, userInfo);
//            salin10JdbcTemplate.update(userInsertSql, userIdRandomValue, userInfo, userInfo);
//            salin09JdbcTemplate.update(userInsertSql, userIdRandomValue, userInfo, userInfo);
        }

//        for (int linkerIdRandomValue = 1; linkerIdRandomValue <= MAX_LINKER_SIZE; linkerIdRandomValue++) {
//            int userIdRandomValue = random.nextInt(MAX_USER_SIZE) + 1;
//            salin08JdbcTemplate.update(linkerInsertSql, linkerIdRandomValue, userIdRandomValue);
//            if (linkerIdRandomValue % 2 == 1) {
//                salin07JdbcTemplate.update(linkerInsertSql, linkerIdRandomValue, userIdRandomValue);
//            } else {
//                salin09JdbcTemplate.update(linkerInsertSql, linkerIdRandomValue, userIdRandomValue);
//            }
//        }

        return "init linkers and users table";
    }

}
