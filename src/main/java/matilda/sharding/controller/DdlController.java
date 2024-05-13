package matilda.sharding.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Random;

@RestController
@RequiredArgsConstructor
public class DdlController {
    @Qualifier("salin07JdbcTemplate")
    private final JdbcTemplate salin07JdbcTemplate;

//    @Qualifier("salin08JdbcTemplate")
//    private final JdbcTemplate salin08JdbcTemplate;

    @Qualifier("salin09JdbcTemplate")
    private final JdbcTemplate salin09JdbcTemplate;

    //    @Qualifier("salin10JdbcTemplate")
//    private final JdbcTemplate salin10JdbcTemplate;

    public String logInsertSql = "insert into logs(log_id, linker_id, log_type, message) values(?,?,?,?)";
    String[] logTypes = {"application", "system", "security", "performance", "error", "audit", "access"};

    @GetMapping("/init/logs")
    public String initLogs() {
        Random random = new Random();
        LocalDate now = LocalDate.now();
        now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        int id = 1;
        for (int i = 1; i <= 960000; i++) {
            int linkerIdRandomValue = random.nextInt(1000) + 1;
            int logTypeRandomValue1 = random.nextInt(logTypes.length);
            int logTypeRandomValue2 = random.nextInt(logTypes.length);
            if (linkerIdRandomValue % 2 == 1) salin07JdbcTemplate.update(logInsertSql, id++, linkerIdRandomValue, logTypes[logTypeRandomValue1], "");
            else salin09JdbcTemplate.update(logInsertSql, id++, linkerIdRandomValue, logTypes[logTypeRandomValue2], "");
        }

        return "init logs ok";
    }
}
