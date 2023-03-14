package com.ljh;

import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.runtime.Execution;
import org.junit.Test;

/**
 * <a href="https://www.activiti.org/5.x/userguide/#bpmnReceiveTask">Receive Task</a>
 *
 * @author ljh
 * @since 2023/3/14 16:08
 */
@Slf4j
public class ReceiveTaskTest {

    private final ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

    /**
     * 部署流程
     */
    @Test
    public void testDeploymentByClasspathResource() {
        RepositoryService repositoryService = this.processEngine.getRepositoryService();
        repositoryService.createDeployment()
                .name("销售额汇总流程")
                .addClasspathResource("processes/ReceiveTask.bpmn20.xml")
                .addClasspathResource("processes/ReceiveTask.png")
                .deploy();
    }

    /**
     * 启动流程
     */
    @Test
    public void testStartProcess() {
        RuntimeService runtimeService = this.processEngine.getRuntimeService();
        runtimeService.startProcessInstanceByKey("ReceiveTask");
    }

    /**
     * 汇总当日销售额
     */
    @Test
    public void testSignalReceiveTask1() {
        RuntimeService runtimeService = this.processEngine.getRuntimeService();

        Execution execution1 = runtimeService.createExecutionQuery()
                .processInstanceId("2501").activityId("receiveTask1").singleResult();

        // 查询数据库进行汇总，耗时操作
        int sales = 10000;
        runtimeService.setVariable(execution1.getId(), "当日销售额", sales);

        runtimeService.signal(execution1.getId());
    }

    /**
     * 发短信给领导
     */
    @Test
    public void testSignalReceiveTask2() {
        RuntimeService runtimeService = this.processEngine.getRuntimeService();
        String executionId = "2501";
        int sales = (int) runtimeService.getVariable(executionId, "当日销售额");
        this.sendMessage(sales);
        runtimeService.signal(executionId);
    }

    private boolean sendMessage(int sales) {
        log.info("短信：当日销售额{}", sales);
        return true;
    }
}
