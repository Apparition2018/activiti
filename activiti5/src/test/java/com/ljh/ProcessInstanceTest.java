package com.ljh;

import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.*;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Test;

import java.util.List;

/**
 * <a href="https://www.bilibili.com/video/BV1Ut411y7NT/?p=11">流程实例</a>
 *
 * @author ljh
 * @since 2023/3/6 15:14
 */
@Slf4j
public class ProcessInstanceTest {

    private final ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

    /**
     * 部署流程
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
     * 启动流程
     */
    @Test
    public void testStartProc() {
        RuntimeService runtimeService = this.processEngine.getRuntimeService();
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("HelloWorld");
        // runtimeService.startProcessInstanceByKey(processDefinitionKey[, businessKey, variables]);
        // runtimeService.startProcessInstanceById(processDefinitionId[, businessKey, variables]);
        log.info("流程启动成功：ID 为 {}，ProcessDefinitionId 为 {}，ProcessInstanceId 为 {}",
                processInstance.getId(), processInstance.getProcessDefinitionId(), processInstance.getProcessInstanceId());
    }

    /**
     * 任务
     */
    @Test
    public void testQueryTask() {
        TaskService taskService = this.processEngine.getTaskService();

        // 查询任务
        List<Task> taskList = taskService.createTaskQuery()
                .taskAssignee("张三")
                .orderByTaskCreateTime().desc()
                .list();
        taskList.forEach(task -> {
            log.info("任务 ID：{}", task.getId());

            // 完成任务
            taskService.complete(task.getId());
            // taskService.complete(taskId[, variables, localScope]);
            log.info("完成任务");
        });
    }

    /**
     * 判断流程是否结束
     * 作用：更新业务表的状态
     */
    @Test
    public void testIsComplete() {
        RuntimeService runtimeService = this.processEngine.getRuntimeService();

        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId("2501").singleResult();
        if (processInstance != null) {
            log.info("流程未结束");
        } else {
            log.info("流程结束");
        }
    }

    /**
     * 查询历史流程实例
     */
    @Test
    public void testQueryHistoryProcessInstance() {
        HistoryService historyService = this.processEngine.getHistoryService();
        List<HistoricProcessInstance> historicProcessInstances = historyService.createHistoricProcessInstanceQuery().list();
        historicProcessInstances.forEach(historicProcessInstance -> {
            log.info("实例 ID：{}", historicProcessInstance.getId());
            log.info("流程启动时间：{}", historicProcessInstance.getStartTime());
            log.info("==============================");
        });
    }

    /**
     * 查询历史任务
     */
    @Test
    public void testQueryHistoryTask() {
        HistoryService historyService = this.processEngine.getHistoryService();
        List<HistoricTaskInstance> historicTaskInstanceList = historyService.createHistoricTaskInstanceQuery().list();
        historicTaskInstanceList.forEach(historicTaskInstance -> {
            log.info("任务 ID：{}", historicTaskInstance.getId());
            log.info("任务创建时间：{}", historicTaskInstance.getCreateTime());
            log.info("任务结束时间：{}", historicTaskInstance.getEndTime());
            log.info("任务持续时间：{}", historicTaskInstance.getDurationInMillis());
            log.info("==============================");
        });
    }
}
