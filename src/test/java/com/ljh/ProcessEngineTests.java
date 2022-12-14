package com.ljh;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.ProcessEngines;
import org.junit.Test;

/**
 * ProcessEngineTest
 *
 * @author ljh
 * created on 2022/6/12 15:02
 */
public class ProcessEngineTests {

    /**
     * 使用默认流程引擎，生成 Activiti 的表
     */
    @Test
    public void getDefaultProcessEngine() {
        // 使用 classpath 下的 activiti.cfg.xml 中的配置来创建 ProcessEngine 对象
        ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
        System.out.println(engine);
    }

    /**
     * 按自定义方式加载配置文件
     */
    @Test
    public void buildProcessEngine() {
        ProcessEngineConfiguration configuration = ProcessEngineConfiguration.createProcessEngineConfigurationFromResource("activiti.cfg.xml");
        ProcessEngine engine = configuration.buildProcessEngine();
        System.out.println(engine);
    }
}
