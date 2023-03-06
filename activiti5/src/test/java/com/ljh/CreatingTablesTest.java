package com.ljh;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.ProcessEngines;
import org.junit.Test;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

/**
 * 创建数据库表
 *
 * @author ljh
 * @since 2023/2/24 9:48
 */
public class CreatingTablesTest {

    @Test
    public void test1() {
        // 创建数据源
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://127.0.0.1:3306/activiti");
        dataSource.setUsername("root");
        dataSource.setPassword("root");

        // 创建 ProcessEngine
        ProcessEngine processEngine = ProcessEngineConfiguration.createStandaloneProcessEngineConfiguration()
                // .setJdbcDriver("com.mysql.cj.jdbc.Driver")
                // .setJdbcUrl("jdbc:mysql://127.0.0.1:3306/activiti")
                // .setJdbcUsername("root")
                // .setJdbcPassword("root")
                .setDataSource(dataSource)
                .setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_CREATE_DROP)
                .buildProcessEngine();
    }

    @Test
    public void test2() {
        ProcessEngine processEngine = ProcessEngineConfiguration.createProcessEngineConfigurationFromResource("activiti.cfg.xml").buildProcessEngine();
    }

    @Test
    public void test3() {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
    }
}
