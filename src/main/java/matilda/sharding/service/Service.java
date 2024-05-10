package matilda.sharding.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import matilda.sharding.domain.Log;
import matilda.sharding.repository.LogRepository;
import org.springframework.transaction.annotation.Transactional;

@org.springframework.stereotype.Service
@RequiredArgsConstructor
@Slf4j
public class Service {

    //    private final UserRepository userRepository;
    private final LogRepository logRepository;

//    @Autowired
//    @Qualifier("db1")
//    private DataSource db1;
//
//    @Autowired
//    @Qualifier("db2")
//    private DataSource db2;

    @Transactional
    public void save(Log log)  {
            logRepository.save(log);

//        DataSource targetedDataSource = user.getUserId() % 2 == 0 ? db1 : db2;
//        Connection conn = null;
//        try {
//            conn = targetedDataSource.getConnection();
//            PreparedStatement pstmt = conn.prepareStatement("insert into users (user_id, username, password) values (?, ?, ?)");
//            for (int i = 120; i < 1200; i++) {
//                pstmt.setLong(1, i);
//                pstmt.setString(2, user.getUsername());
//                pstmt.setString(3, user.getPassword());
//                pstmt.executeUpdate();
//            }
//        } catch (SQLException e) {
//            log.error(e.getMessage());
//
//            //userRepository.save(user);
//            log.info("{}에 데이터 저장 : {}", targetedDataSource, user.getUserId());
//        } finally {
//            conn.close();
//        }
    }
}
