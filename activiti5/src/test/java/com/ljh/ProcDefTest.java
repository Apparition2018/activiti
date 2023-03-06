package com.ljh;

import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Objects;
import java.util.zip.ZipInputStream;

/**
 * <a href="https://www.bilibili.com/video/BV1Ut411y7NT?p=10">流程定义</a>
 *
 * @author ljh
 * @since 2023/3/6 10:32
 */
@Slf4j
public class ProcDefTest {

    private final ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

    /**
     * 部署流程 (classpath)
     */
    @Test
    public void testDeploymentByClasspathResource() {
        RepositoryService repositoryService = this.processEngine.getRepositoryService();
        repositoryService.createDeployment()
                .name("请假流程001")
                .addClasspathResource("processes/HelloWorld.bpmn20.xml")
                .addClasspathResource("processes/HelloWorld.png")
                .deploy();
    }

    /**
     * 部署流程 (zip)
     * 注：流程修改后重新部署，只要 Key 不变，版本号 +1
     */
    @Test
    public void testDeploymentByZip() {
        RepositoryService repositoryService = this.processEngine.getRepositoryService();
        repositoryService.createDeployment()
                .name("请假流程001")
                .addZipInputStream(new ZipInputStream(Objects.requireNonNull(this.getClass().getResourceAsStream("/processes/HelloWorld.zip"))))
                .deploy();
    }

    /**
     * 查询
     */
    @Test
    public void testQuery() {
        RepositoryService repositoryService = this.processEngine.getRepositoryService();

        log.info("查询部署信息");
        List<Deployment> deploymentList = repositoryService.createDeploymentQuery()
                // .deploymentId("2501")
                .deploymentName("请假流程001")
                .orderByDeploymentId().asc()
                .orderByDeploymentName().asc()
                // .count()
                // .singleResult()
                // .list()
                .listPage(0, 10);
        deploymentList.forEach(System.out::println);

        log.info("查询流程定义");
        List<ProcessDefinition> processDefinitionList = repositoryService.createProcessDefinitionQuery()
                // .deploymentId("2001")
                .processDefinitionKey("HelloWorld")
                .processDefinitionVersionGreaterThan(1)
                .orderByProcessDefinitionId().asc()
                .list();
        processDefinitionList.forEach(System.out::println);
    }

    /**
     * 启动流程
     */
    @Test
    public void testStartProc() {
        RuntimeService runtimeService = this.processEngine.getRuntimeService();
        runtimeService.startProcessInstanceByKey("HelloWorld");
    }

    /**
     * 查询流程图
     */
    @Test
    public void testProcessDiagram() throws IOException {
        RepositoryService repositoryService = this.processEngine.getRepositoryService();
        InputStream inputStream = repositoryService.getProcessDiagram("HelloWorld:1:4");
        log.info(inputStream.available() + "");
    }

    /**
     * 删除部署
     */
    @Test
    public void testDeleteDeployment() {
        RepositoryService repositoryService = this.processEngine.getRepositoryService();
        // true 表示级联删除 process instances, history process instances 和 jobs.
        repositoryService.deleteDeployment("2501", true);
        log.info("删除成功");
    }
}
