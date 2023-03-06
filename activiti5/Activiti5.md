# Activiti5

---
## Reference
1. [Activiti 5.15 用户手册](http://1json.com/activiti/activiti-userguide.html)
2. [Activiti 5.22 User Guide](https://www.activiti.org/5.x/userguide/)
3. [Activiti 5.2 视频教程](https://www.bilibili.com/video/BV1t64y147v4)
---
## [配置](https://www.activiti.org/5.x/userguide/#_configuration)
- @see [activiti.cfg.xml](src/main/resources/activiti.cfg.xml)
### [数据库表名称说明](https://www.activiti.org/5.x/userguide/#database.tables.explained)
| Identification | Word       | Use Case               |
|----------------|------------|------------------------|
| ACT_RE_*       | repository | 静态信息：流程定义、流程资源（图像、规则等） |
| ACT_RU_*       | runtime    | 运行时数据：流程实例、用户任务、变量、作业等 |
| ACT_ID_*       | identity   | 身份信息：用户、组等             |
| ACT_HI_*       | history    | 历史数据：流程实例、变量、任务等       |
| ACT_GE_*       | general    | 通用数据                   |
### [在表达式和脚本中暴露配置 beans](https://www.activiti.org/5.x/userguide/#exposingConfigurationBeans)

### [日志](https://www.activiti.org/5.x/userguide/#loggingConfiguration)
- [映射诊断上下文 (MDC)](https://www.activiti.org/5.x/userguide/#MDC)

| ID                   |                        |
|----------------------|------------------------|
| processDefinition Id | mdcProcessDefinitionID |
| processInstance Id   | mdcProcessInstanceID   |
| execution Id         | mdcExecutionId         |
```properties
# log4j.properties
log4j.appender.consoleAppender.layout.ConversionPattern=ProcessDefinitionId=%X{mdcProcessDefinitionID} executionId=%X{mdcExecutionId} mdcProcessInstanceID=%X{mdcProcessInstanceID} mdcBusinessKey=%X{mdcBusinessKey} %m%n
```
### [事件](https://www.activiti.org/5.x/userguide/#eventDispatcher)

---
## [Activiti API](https://www.activiti.org/5.x/userguide/#chapterApi)
- @see [ActivitiTest](src/test/java/com/ljh/ActivitiTest.java)
### 服务
| Service           | explanation      | Tables   |
|-------------------|------------------|----------|
| RepositoryService | 部署、流程定义          | ACT_RE_* |
| RuntimeService    | 流程实例             | ACT_RU_* |
| TaskService       | 任务               | ACT_RU_* |
| HistoryService    | 历史数据             | ACT_HI_* |
| IdentityService   | 群组、用户            | ACT_ID_* |
| ManagementService | 查询数据表和表的元数据、异步操作 |          |
| FormService       | 启动表单、任务表单        |          |
---
