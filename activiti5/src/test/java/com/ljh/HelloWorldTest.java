package com.ljh;

import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.*;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Test;

import java.util.List;

/**
 * <a href="https://www.bilibili.com/video/BV1Ut411y7NT/?p=9">HelloWorld Demo</a>
 * <pre>
 * 部署 ID         ACT_RE_DEPLOYMENT   ID_
 * 流程定义 ID      ACT_RE_PROCDEF      ID_
 * 流程实例 ID      ACT_RU_EXECUTION    PROC_INST_ID_
 * 流程执行 ID      ACT_RU_EXECUTION    ID_
 * 任务 ID         ACT_RU_TASK         ID_
 * </pre>
 *
 * @author ljh
 * @since 2023/2/24 17:21
 */
@Slf4j
public class HelloWorldTest {

    private final ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

    /**
     * 部署流程
     */
    @Test
    public void testDeployment() {
        RepositoryService repositoryService = this.processEngine.getRepositoryService();
        Deployment deployment = repositoryService.createDeployment()
                .name("请假流程001")
                .addClasspathResource("processes/HelloWorld.bpmn20.xml")
                .addClasspathResource("processes/HelloWorld.png")
                .deploy();
        log.info("部署成功：部署 ID 为 {}", deployment.getId());
    }

    /**
     * 启动流程
     */
    @Test
    public void testStartProcess() {
        RuntimeService runtimeService = this.processEngine.getRuntimeService();
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("HelloWorld");
        log.info("启动成功：实例 ID 为 {}", processInstance.getId());
    }

    /**
     * 任务
     */
    @Test
    public void testTask() {
        TaskService taskService = this.processEngine.getTaskService();

        // 查询任务
        // 分别改变参数 张三/李四/王五 执行
        List<Task> taskList = taskService.createTaskQuery().taskAssignee("王五").list();
        taskList.forEach(task -> {
            log.info("任务 ID：{}", task.getId());
            log.info("流程执行 ID：{}", task.getExecutionId());
            log.info("流程实例 ID：{}", task.getProcessInstanceId());
            log.info("流程定义 ID：{}", task.getProcessDefinitionId());
            log.info("任务名称 ID：{}", task.getName());
            log.info("任务办理人 ID：{}", task.getAssignee());
            log.info("==============================");

            // 完成任务
            taskService.complete(task.getId());
        });
    }
}
