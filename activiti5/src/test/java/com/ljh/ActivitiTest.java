package com.ljh;

import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.*;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.test.ActivitiRule;
import org.junit.Rule;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ActivitiTests
 *
 * @author ljh
 * @since 2023/1/19 10:51
 */
@Slf4j
public class ActivitiTest {

    /**
     * <a href="https://www.activiti.org/5.x/userguide/#configuration">创建 ProcessEngine</a>
     */
    @Test
    public void testCreatingProcessEngine() {
        /* 1 通过 ProcessEngines 创建 ProcessEngine
         *  在 classpath 下搜索 activiti.cfg.xml，并基于这个文件中的配置构建引擎
         */
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

        /* 2 通过 ProcessEngineConfiguration 创建 ProcessEngine（不使用配置文件） */
        processEngine = ProcessEngineConfiguration.createStandaloneInMemProcessEngineConfiguration()
                .setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_FALSE)
                .setJdbcUrl("jdbc:h2:mem:activiti;DB_CLOSE_DELAY=1000")
                .setJobExecutorActivate(Boolean.TRUE)
                .buildProcessEngine();

        ProcessEngines.init();
    }

    /**
     * <a href="https://www.activiti.org/5.x/userguide/#api.services">使用 Activiti 服务</a>
     */
    @Test
    public void testActivitiServices() {
        /* 部署流程 */
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        RepositoryService repositoryService = processEngine.getRepositoryService();
        repositoryService.createDeployment()
                .addClasspathResource("processes/VacationRequest.bpmn20.xml")
                .deploy();
        log.warn("Number of process definitions: " + repositoryService.createProcessDefinitionQuery().count());
        // 暂停/激活 流程
        repositoryService.suspendProcessDefinitionByKey("vacationRequest");
        repositoryService.activateProcessDefinitionByKey("vacationRequest");

        /* 启动流程实例 */
        Map<String, Object> variables = new HashMap<>();
        variables.put("employeeName", "Kermit");
        variables.put("numberOfDays", 4);
        variables.put("vacationMotivation", "I'm really tired!");
        RuntimeService runtimeService = processEngine.getRuntimeService();
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("vacationRequest", variables);
        log.warn("Number of process instances: " + runtimeService.createProcessInstanceQuery().count());
        // 暂停/激活 流程实例
        runtimeService.suspendProcessInstanceById(processInstance.getId());
        runtimeService.activateProcessInstanceById(processInstance.getId());

        // 完成任务
        TaskService taskService = processEngine.getTaskService();
        List<Task> tasks = taskService.createTaskQuery().taskCandidateGroup("management").list();
        for (Task task : tasks) {
            log.warn("Task available: " + task.getName());
        }
        Task task = tasks.get(0);
        Map<String, Object> taskVariables = new HashMap<>();
        taskVariables.put("vacationApproved", "false");
        taskVariables.put("managerMotivation", "We have a tight deadline!");
        taskService.complete(task.getId(), taskVariables);
    }

    /**
     * <a href="https://www.activiti.org/5.x/userguide/#apiUnitTesting">单元测试</a>
     */
    @Rule
    public ActivitiRule activitiRule = new ActivitiRule();

    /**
     * <a href="https://www.activiti.org/5.x/userguide/#_transactions">Spring 事务集成</a>
     */
    @Test
    public void testSpringTransactionIntegration() {
        ClassPathXmlApplicationContext applicationContext =
                new ClassPathXmlApplicationContext("SpringTransactionIntegrationTest-context.xml");
        RepositoryService repositoryService = (RepositoryService) applicationContext.getBean("repositoryService");
    }

    /**
     * <a href="https://www.activiti.org/5.x/userguide/#_getting_started_10_minute_tutorial">Demo</a>
     */
    @Test
    public void testDemo() {
        ProcessEngine processEngine = ProcessEngineConfiguration.createStandaloneInMemProcessEngineConfiguration().buildProcessEngine();
        RepositoryService repositoryService = processEngine.getRepositoryService();
        RuntimeService runtimeService = processEngine.getRuntimeService();
        TaskService taskService = activitiRule.getTaskService();
        HistoryService historyService = activitiRule.getHistoryService();

        // 部署流程定义
        Deployment deployment = repositoryService.createDeployment()
                .addClasspathResource("processes/FinancialReportProcess.bpmn20.xml")
                .deploy();

        // 启动流程实例，并返回实例 ID
        String procId = runtimeService.startProcessInstanceByKey("financialReport").getId();

        // 查询 accountancy 组的任务
        List<Task> tasks = taskService.createTaskQuery().taskCandidateGroup("accountancy").list();
        // 分配任务给 fozzie
        tasks.forEach(t -> taskService.claim(t.getId(), "fozzie"));
        // 查询分配给 fozzie 的任务
        tasks = taskService.createTaskQuery().taskAssignee("fozzie").list();
        // 完成任务
        tasks.forEach(t -> taskService.complete(t.getId()));
        // 查询 management 组的任务
        tasks = taskService.createTaskQuery().taskCandidateGroup("management").list();
        // 分配任务给 kermit
        tasks.forEach(t -> taskService.claim(t.getId(), "kermit"));
        // 完成任务
        tasks.forEach(t -> taskService.complete(t.getId()));

        // 根据流程例 ID 查询历史记录
        HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery().processInstanceId(procId).singleResult();
        log.warn("Process instance end time: " + historicProcessInstance.getEndTime());
    }
}
