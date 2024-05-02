package com.lms.hustoj.config;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

@Configuration
@MapperScan(basePackages = "com.lms.hustoj.mapper", sqlSessionFactoryRef = "hustojSqlSessionFactory")
public class HustojDataSourceConfig {

    @Bean(name = "dataSourceHustoj")
    @ConfigurationProperties(prefix = "spring.datasource.druid.hustoj")
    public DataSource dataSourceHustoj() {
        return DruidDataSourceBuilder.create().build();
    }


    @Bean("hustojSqlSessionFactory")
    public SqlSessionFactory hustojSqlSessionFactory(@Qualifier("dataSourceHustoj") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(dataSource);
        sessionFactory.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:/hustoj/mapper/*.xml"));
        return sessionFactory.getObject();
    }

    @Bean(name = "hustojTransactionManager")
    public DataSourceTransactionManager hustojTransactionManager(@Qualifier("dataSourceHustoj") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }
}
