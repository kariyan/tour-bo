package com.wemakeprice.tour.bo.config.datasource;

import com.wemakeprice.tour.bo.common.annotation.TestExcluded;
import com.wemakeprice.tour.bo.common.mapper.TourBoMapper;
import com.wemakeprice.tour.bo.config.kms.KmsProperties;
import com.wemakeprice.tour.bo.config.kms.vo.DbUserVO;
import com.zaxxer.hikari.HikariDataSource;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.mybatis.replication.datasource.ReplicationRoutingDataSource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

@TestExcluded
@Configuration
@EnableTransactionManagement(proxyTargetClass = true)
@MapperScan(basePackages = {"com.wemakeprice.tour.bo.service.store"}, annotationClass = TourBoMapper.class,
        sqlSessionTemplateRef = "tourBoSqlSessionTemplate")
@RequiredArgsConstructor
public class TourBoDataSourceConfig {

    private final KmsProperties kmsProperties;
    private final ApplicationContext applicationContext;

    @Value("${database.tour-bo.cluster-url}")
    private String databaseClusterUrl;
    @Value("${database.tour-bo.reader-url}")
    private String databaseReaderUrl;
    @Value("${database.options:#{null}}")
    private String databaseOptions;
    @Value("${database.connection-timeout:#{null}}")
    private String connectionTimeOut;
    @Value("${database.maximum-pool-size:#{null}}")
    private String maximumPoolSize;

    @Bean(name = "tourBoReadWriteDataSource")
    public DataSource tourBoReadWriteDataSource() {
        DataSourceProperties properties = buildDataSourceProperties(false);
        DataSource dataSource = properties.initializeDataSourceBuilder().build();
        setAdditionalHikariProperties(dataSource);
        return dataSource;
    }

    @Bean(name = "tourBoReadOnlyDataSource")
    public DataSource tourBoReadOnlyDataSource() {
        DataSourceProperties properties = buildDataSourceProperties(true);
        DataSource dataSource = properties.initializeDataSourceBuilder().build();
        setAdditionalHikariProperties(dataSource);
        return dataSource;
    }

    private void setAdditionalHikariProperties(DataSource dataSource) {
        if (dataSource instanceof HikariDataSource) {
            if (connectionTimeOut != null) {
                ((HikariDataSource) dataSource).setConnectionTimeout(Long.parseLong(connectionTimeOut));
            }
            if (maximumPoolSize != null) {
                ((HikariDataSource) dataSource).setMaximumPoolSize(Integer.parseInt(maximumPoolSize));
            }
        }
    }

    private DataSourceProperties buildDataSourceProperties(boolean readOnly) {
        DbUserVO dbUserVO;
        if (kmsProperties != null && kmsProperties.getDatabase() != null) {
            if (readOnly) {
                dbUserVO = kmsProperties.getDatabase().getReader(); // readOnly DataSource 정보
            } else {
                dbUserVO = kmsProperties.getDatabase().getCluster(); // read/write DataSource 정보
            }
        } else {
            dbUserVO = new DbUserVO();
        }

        return buildConnectionUrl(readOnly, dbUserVO.getUsername(), dbUserVO.getPassword());
    }

    private DataSourceProperties buildConnectionUrl(boolean readOnly, String username, String password) {
        DataSourceProperties dsp = new DataSourceProperties();
        String url = (readOnly ? databaseReaderUrl : databaseClusterUrl);
        url += databaseOptions != null ? databaseOptions
                : "?verifyServerCertificate=false&useSSL=false&useUnicode=true&characterEncoding=utf8";

        dsp.setUrl(url);
        dsp.setUsername(username);
        dsp.setPassword(password);
        return dsp;
    }

    @Bean(name = "tourBoRoutingDataSource")
    public DataSource tourBoRoutingDataSource(@Qualifier("tourBoReadWriteDataSource") DataSource readWriteDataSource,
                                              @Qualifier("tourBoReadOnlyDataSource") DataSource readOnlyDataSource) {
        List<DataSource> readOnlyDataSources = new ArrayList<>();
        readOnlyDataSources.add(readOnlyDataSource);

        return new ReplicationRoutingDataSource(readWriteDataSource, readOnlyDataSources);
    }

    @Bean(name = "tourBoDataSource")
    public DataSource tourBoDataSource(@Qualifier("tourBoRoutingDataSource") DataSource routingDataSource) {
        return new LazyConnectionDataSourceProxy(routingDataSource);
    }

    @Primary
    @Bean(name = "tourBoTransactionManager")
    public PlatformTransactionManager tourBoTransactionManager(@Qualifier("tourBoDataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean(name = "tourBoSqlSessionFactory")
    public SqlSessionFactory tourBoSqlSessionFactory(@Qualifier("tourBoDataSource") DataSource dataSource)
            throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource);
        sqlSessionFactoryBean.setConfigLocation(applicationContext.getResource("classpath:mybatis-config.xml"));
        return sqlSessionFactoryBean.getObject();
    }

    @Bean(name = "tourBoSqlSessionTemplate")
    public SqlSessionTemplate tourBoSqlSessionTemplate(
            @Qualifier("tourBoSqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }
}
