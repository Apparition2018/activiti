package com.ljh;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.*;
import org.activiti.engine.history.HistoricVariableInstance;
import org.activiti.engine.runtime.ProcessInstance;
import org.junit.Test;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <a href="https://www.bilibili.com/video/BV1Ut411y7NT?p=12">流程变量</a>
 * <p><a href="https://www.activiti.org/5.x/userguide/#restVariables">变量类型</a>
 *
 * @author ljh
 * @since 2023/3/8 11:31
 */
@Slf4j
public class ProcessVariablesTest {

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
    public void testStartProcess() {
        RuntimeService runtimeService = this.processEngine.getRuntimeService();
        Map<String, Object> variables = new HashMap<>();
        variables.put("请假天数", 5);
        variables.put("请假原因", "约会");
        variables.put("请假时间", new Date());
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("HelloWorld", variables);
        log.info("启动成功：实例 ID 为 {}", processInstance.getId());
    }

    /**
     * 设置流程变量
     * <p>taskService.setVariable() 和 taskService.setVariables() 不会记录 Task ID
     */
    @Test
    public void testSetVariable() {
        /* RuntimeService */
        RuntimeService runtimeService = this.processEngine.getRuntimeService();
        runtimeService.setVariable("2501", "请假人", "小明");
        Map<String, Object> variables = new HashMap<>();
        variables.put("请假天数", 5);
        variables.put("请假原因", "约会");
        // 对比之前设置的值发生了变化，以新值覆盖旧值，并且 REV_ + 1
        variables.put("请假时间", new Date());
        runtimeService.setVariables("2501", variables);

        /* TaskService */
        TaskService taskService = this.processEngine.getTaskService();
        taskService.setVariable("2507", "请假人", "小明");
        variables = new HashMap<>();
        // 序列化对象存在 ACT_GE_BYTEARRAY
        variables.put("用户对象", new User(1, "小明"));
        taskService.setVariables("2507", variables);
    }

    /**
     * 设置流程局部变量
     * <p>当前活动的节点都会设置变量
     * <p>taskService.setVariablesLocal() 和 taskService.setVariablesLocal() 会记录 Task ID
     */
    @Test
    public void testSetVariableLocal() {
        /* RuntimeService */
        RuntimeService runtimeService = this.processEngine.getRuntimeService();
        runtimeService.setVariableLocal("2501", "请假人", "小明");
        Map<String, Object> variables = new HashMap<>();
        variables.put("请假天数", 5);
        variables.put("请假原因", "约会");
        variables.put("请假时间", new Date());
        runtimeService.setVariablesLocal("2501", variables);

        /* TaskService */
        TaskService taskService = this.processEngine.getTaskService();
        taskService.setVariableLocal("2507", "请假人", "小明");
        variables = new HashMap<>();
        variables.put("用户对象", new User(1, "小明"));
        taskService.setVariablesLocal("2507", variables);
    }

    /**
     * 获取流程变量
     */
    @Test
    public void testGetVariable() {
        RuntimeService runtimeService = this.processEngine.getRuntimeService();
        String executionId = "2501";
        log.info("请假天数：{}", runtimeService.getVariable(executionId, "请假天数"));
        log.info("用户对象：{}", runtimeService.getVariable(executionId, "用户对象"));
    }

    /**
     * 查询历史流程变量
     */
    @Test
    public void testHistoryVariable() {
        HistoryService historyService = this.processEngine.getHistoryService();
        List<HistoricVariableInstance> historicVariableInstanceList = historyService.createHistoricVariableInstanceQuery().processInstanceId("2501").list();
        historicVariableInstanceList.forEach(historicVariableInstance -> {
            log.info("ID：{}", historicVariableInstance.getId());
            log.info("变量名称：{}", historicVariableInstance.getVariableName());
            log.info("变量类型：{}", historicVariableInstance.getVariableTypeName());
            log.info("变量值：{}", historicVariableInstance.getValue());
            log.info("==============================");
        });
    }

    @Data
    private static class User implements Serializable {
        private static final long serialVersionUID = -351281797484149871L;
        private Integer id;
        private String name;

        public User(Integer id, String name) {
            this.id = id;
            this.name = name;
        }
    }
}
