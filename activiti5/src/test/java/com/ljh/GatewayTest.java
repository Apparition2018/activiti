package com.ljh;

import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.*;
import org.activiti.engine.task.Task;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <a href="https://www.activiti.org/5.x/userguide/#bpmnGateways">网关</a>
 *
 * @author ljh
 * @since 2023/3/8 17:24
 */
@Slf4j
public class GatewayTest {

    /**
     * <a href="https://www.bilibili.com/video/BV1Ut411y7NT/?p=15">排他网关</a>
     */
    public static class ExclusiveGatewayTest {

        private final ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

        /**
         * 部署流程
         */
        @Test
        public void testDeploymentByClasspathResource() {
            RepositoryService repositoryService = this.processEngine.getRepositoryService();
            repositoryService.createDeployment()
                    .name("报销流程")
                    .addClasspathResource("processes/ExclusiveGateway.bpmn20.xml")
                    .addClasspathResource("processes/ExclusiveGateway.png")
                    .deploy();
        }

        /**
         * 启动流程
         */
        @Test
        public void testStartProcess() {
            RuntimeService runtimeService = this.processEngine.getRuntimeService();
            runtimeService.startProcessInstanceByKey("ExclusiveGateway");
        }

        /**
         * 张三提交申请
         */
        @Test
        public void testCompleteTask1() {
            TaskService taskService = this.processEngine.getTaskService();
            List<Task> taskList = taskService.createTaskQuery().taskAssignee("张三").list();
            Map<String, Object> variables = new HashMap<>();
            // money < 500          → 财务
            // 500 <= money < 1000  → 部门经理
            // money > 1000         → 总经理
            variables.put("money", "1500");
            taskList.forEach(task -> {
                taskService.complete(task.getId(), variables);
                log.info("完成任务");
            });
        }

        /**
         * 总经理审批通过
         */
        @Test
        public void testCompleteTask2() {
            TaskService taskService = this.processEngine.getTaskService();
            List<Task> taskList = taskService.createTaskQuery().taskAssignee("总经理").list();
            taskList.forEach(task -> {
                taskService.complete(task.getId());
                log.info("完成任务");
            });
        }
    }
}
