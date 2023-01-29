package com.ljh;

import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.*;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
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
        log.warn("Number of process instances: {}", runtimeService.createProcessInstanceQuery().count());
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
     * <a href="https://www.activiti.org/5.x/userguide/#_transactions">Spring 事务集成</a>
     */
    @Test
    public void testSpringTransactionIntegration() {
        ClassPathXmlApplicationContext applicationContext =
                new ClassPathXmlApplicationContext("SpringTransactionIntegrationTest-context.xml");
        RepositoryService repositoryService = (RepositoryService) applicationContext.getBean("repositoryService");
    }
}
