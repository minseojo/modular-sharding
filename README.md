@Configuration
@EnableJpaRepositories(
        basePackages = "com.sample.core.maria",
        entityManagerFactoryRef = "primaryEntityManager",
        transactionManagerRef = "primaryTransactionManager"
)
public class PrimaryConfig {

    @Bean
    @Primary
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource primaryDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean
    @Primary
    public LocalContainerEntityManagerFactoryBean primaryEntityManager() {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(primaryDataSource());
        em.setPackagesToScan(new String[] {"com.sample.core.maria.test"});
        
        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        vendorAdapter.setShowSql(true);
        vendorAdapter.setGenerateDdl(true);
        em.setJpaVendorAdapter(vendorAdapter);
        
        HashMap<String, Object> prop = new HashMap<>();
        prop.put("hibernate.dialect", "org.hibernate.dialect.MariaDBDialect");
        prop.put("hibernate.hbm2ddl.auto", "update");
        prop.put("hibernate.format_sql", true);
        em.setJpaPropertyMap(prop);
        
        return em;
    }

    @Bean
    @Primary
    public PlatformTransactionManager primaryTransactionManager() {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(primaryEntityManager().getObject());
        return transactionManager;
    }
}
출처: https://jong-bae.tistory.com/58 [기록하는 프로그래머:티스토리]
