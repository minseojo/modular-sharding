package matilda.sharding.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfig {

    @Bean
    @Qualifier("salin09DataSource")
    public DataSource salin09DataSource() {
        return DataSourceBuilder.create()
                .driverClassName("com.mysql.cj.jdbc.Driver")
                .url("jdbc:mysql://172.16.5.238:3306/matilda")
                .username("qwer1234")
                .password("qwer1234")
                .build();
    }

    @Bean
    @Qualifier("salin09JdbcTemplate")
    public JdbcTemplate salin09JdbcTemplate(@Qualifier("salin09DataSource") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    @Bean
    @Qualifier("salin10DataSource")
    public DataSource salin10DataSource() {
        return DataSourceBuilder.create()
                .driverClassName("com.mysql.cj.jdbc.Driver")
                .url("jdbc:mysql://172.16.5.239:3306/matilda")
                .username("hsh")
                .password("hshhsh")
                .build();
    }

    @Bean
    @Qualifier("salin10JdbcTemplate")
    public JdbcTemplate salin10JdbcTemplate(@Qualifier("salin10DataSource") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }


//    @Bean(name = "db2")
//    @Primary
//    @ConfigurationProperties(prefix = "spring.datasources.salin09")
//    public DataSource salin09DataSource() {
//        return DataSourceBuilder.create().build();
//    }
//
//    @Bean(name = "db3")
//    @Primary
//    @ConfigurationProperties(prefix = "spring.datasources.salin10")
//    public DataSource salin10DataSource() {
//        return DataSourceBuilder.create().build();
//    }
}
