package com.ljh;

import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.*;
import org.activiti.engine.task.Task;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <a href="https://www.activiti.org/5.x/userguide/#bpmnSequenceFlow">顺序流</a>
 * <p><a href="https://www.bilibili.com/video/BV1Ut411y7NT?p=14">顺序流</a>
 *
 * @author ljh
 * @since 2023/3/8 16:20
 */
@Slf4j
public class SequenceFlowTest {

    private final ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

    /**
     * 部署流程
     */
    @Test
    public void testDeploymentByClasspathResource() {
        RepositoryService repositoryService = this.processEngine.getRepositoryService();
        repositoryService.createDeployment()
                .name("报销流程")
                .addClasspathResource("processes/SequenceFlow.bpmn20.xml")
                .addClasspathResource("processes/SequenceFlow.png")
                .deploy();
    }

    /**
     * 启动流程
     */
    @Test
    public void testStartProcess() {
        RuntimeService runtimeService = this.processEngine.getRuntimeService();
        runtimeService.startProcessInstanceByKey("SequenceFlow");
    }

    /**
     * 张三提交申请
     */
    @Test
    public void testCompleteTask1() {
        TaskService taskService = this.processEngine.getTaskService();
        List<Task> taskList = taskService.createTaskQuery().taskAssignee("张三").list();
        taskList.forEach(task -> taskService.complete(task.getId()));
    }

    /**
     * 李四审批通过
     */
    @Test
    public void testCompleteTask2() {
        TaskService taskService = this.processEngine.getTaskService();
        List<Task> taskList = taskService.createTaskQuery().taskAssignee("李四").list();
        Map<String, Object> variables = new HashMap<>();
        variables.put("outcome", "不重要");
        taskList.forEach(task -> taskService.complete(task.getId(), variables));
    }
}
