# Activiti5

---
## Reference
1. [Activiti 5.15 用户手册](http://1json.com/activiti/activiti-userguide.html)
2. [Activiti 5.22 User Guide](https://www.activiti.org/5.x/userguide/)
3. [Activiti 5.2 视频教程](https://www.bilibili.com/video/BV1t64y147v4)
---
## [配置](https://www.activiti.org/5.x/userguide/#_configuration)
- @see [activiti.cfg.xml](src/main/resources/activiti.cfg.xml)
### [JNDI 数据源配置](https://www.activiti.org/5.x/userguide/#jndiDatasourceConfig)

### 数据库表名称说明
| Identification | Word       | Use Case               |
|----------------|------------|------------------------|
| ACT_RE_        | repository | 静态信息：流程定义、流程资源（图像、规则等） |
| ACT_RU_        | runtime    | 运行时数据：流程实例、用户任务、变量、作业等 |
| ACT_ID_        | identity   | 身份信息：用户、组等             |
| ACT_HI_        | history    | 历史数据：流程实例、变量、任务等       |
| ACT_GE_        | general    | 通用数据                   |
### [在表达式和脚本中暴露配置 beans](https://www.activiti.org/5.x/userguide/#exposingConfigurationBeans)
### 日志
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
---
