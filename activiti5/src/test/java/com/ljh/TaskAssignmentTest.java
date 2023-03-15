package com.ljh;

import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.*;
import org.activiti.engine.task.IdentityLink;
import org.activiti.engine.task.Task;
import org.junit.Test;

import java.util.List;

/**
 * <pre>
 * <a href="https://www.activiti.org/5.x/userguide/#bpmnUserTaskAssignment">User assignment</a>
 * <a href="https://www.activiti.org/5.x/userguide/#bpmnUserTaskUserAssignmentExtension">Activiti extensions for task assignment</a>
 * <a href="https://www.activiti.org/5.x/userguide/#bpmnUserTaskUserCustomAssignmentTaskListeners">Custom Assignment via task listeners</a>
 * </pre>
 *
 * @author ljh
 * @since 2023/3/14 17:29
 */
@Slf4j
public class TaskAssignmentTest {

    private final ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

    /**
     * 部署流程
     */
    @Test
    public void testDeploymentByClasspathResource() {
        RepositoryService repositoryService = this.processEngine.getRepositoryService();
        repositoryService.createDeployment()
                .name("审批流程")
                .addClasspathResource("processes/TaskAssignment.bpmn20.xml")
                .addClasspathResource("processes/TaskAssignment.png")
                .deploy();
    }

    /**
     * 启动流程
     */
    @Test
    public void testStartProcess() {
        RuntimeService runtimeService = this.processEngine.getRuntimeService();
        runtimeService.startProcessInstanceByKey("TaskAssignment");
    }

    /**
     * 任务服务
     */
    @Test
    public void testTaskService() {
        TaskService taskService = this.processEngine.getTaskService();
        List<Task> taskList = taskService.createTaskQuery().taskCandidateUser("张三").list();
        taskList.forEach(task -> {
            log.info("任务 ID：{}，办理人 ID：{}", task.getId(), task.getAssignee());

            taskService.addCandidateUser(task.getId(), "王五");
            taskService.deleteCandidateUser(task.getId(), "王五");

            List<IdentityLink> identityLinkList = taskService.getIdentityLinksForTask(task.getId());
            identityLinkList.forEach(identityLink -> {
                log.info("类型：{}，用户 ID：{}，任务 ID：{}，流程实例 ID：{}", identityLink.getType(), identityLink.getUserId(), identityLink.getTaskId(), identityLink.getProcessInstanceId());
            });
        });
    }

    /**
     * 声明任务责任人
     */
    @Test
    public void testClaim() {
        TaskService taskService = this.processEngine.getTaskService();
        taskService.claim("2504", "张三");

        List<Task> taskList = taskService.createTaskQuery().taskAssignee("张三").list();
        taskList.forEach(task -> {
            log.info("任务 ID：{}", task.getId());
        });
    }

    /**
     * 任务责任人回退
     */
    @Test
    public void testClaimBack() {
        TaskService taskService = this.processEngine.getTaskService();
        taskService.setAssignee("2504", null);

        List<Task> taskList = taskService.createTaskQuery().taskCandidateUser("张三").list();
        taskList.forEach(task -> {
            log.info("任务 ID：{}", task.getId());
        });
    }
}
