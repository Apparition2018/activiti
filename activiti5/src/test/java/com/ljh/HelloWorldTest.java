package com.ljh;

import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;
import org.junit.Test;

/**
 * <a href="https://www.bilibili.com/video/BV1Ut411y7NT/?p=9">HelloWorld Demo</a>
 *
 * @author ljh
 * @since 2023/2/24 17:21
 */
@Slf4j
public class HelloWorldTest {

    private final ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

    /**
     * 部署
     */
    @Test
    public void testDeployment() {
        RepositoryService repositoryService = processEngine.getRepositoryService();
        Deployment deployment = repositoryService.createDeployment()
                .name("请假流程001")
                .addClasspathResource("processes/HelloWorld.bpmn20.xml")
                .addClasspathResource("processes/HelloWorld.png")
                .deploy();
        log.info("部署成功：部署 ID 为 {}", deployment.getId());
    }
}
