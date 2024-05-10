//package matilda.sharding.config;
//
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.boot.context.properties.ConfigurationProperties;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Primary;
//import org.springframework.boot.jdbc.DataSourceBuilder;
//import javax.sql.DataSource;
//
//@Configuration
//public class DataSourceConfig {
//
//    @Bean(name = "db0")
//    @Primary
//    @ConfigurationProperties(prefix = "spring.datasources.salin07")
//    public DataSource salin07DataSource() {
//        return DataSourceBuilder.create().build();
//    }
//
//    @Bean(name = "db1")
//    @Primary
//    @ConfigurationProperties(prefix = "spring.datasources.salin08")
//    public DataSource salin08DataSource() {
//        return DataSourceBuilder.create().build();
//    }
//
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
//}
