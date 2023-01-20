package com.ljh;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.ProcessEngines;
import org.junit.Test;

/**
 * ActivitiTests
 *
 * @author ljh
 * @since 2023/1/19 10:51
 */
public class ActivitiTests {
    
    @Test
    public void testProcessEngine() {
        /* 1 通过 ProcessEngines 创建 ProcessEngine
         *  https://www.activiti.org/5.x/userguide/#configuration
         *  在 classpath 下搜索 activiti.cfg.xml，并基于这个文件中的配置构建引擎
         */
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

        /* 2 通过 ProcessEngineConfiguration 创建 ProcessEngine（不使用配置文件） */
        processEngine = ProcessEngineConfiguration.createStandaloneInMemProcessEngineConfiguration()
                .setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_FALSE)
                .setJdbcUrl("jdbc:h2:mem:activiti;DB_CLOSE_DELAY=1000")
                .setJobExecutorActivate(Boolean.TRUE)
                .buildProcessEngine();
    }
}
