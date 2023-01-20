# Activiti7

---
## Reference
1. [Activiti](https://www.activiti.org/)
2. [Activiti/Activiti](https://github.com/Activiti/Activiti)
3. [Activiti 最全讲解](https://www.bilibili.com/video/BV1t64y147v4)
---
## 术语
- BPM：Business Process Management，业务流程管理
- BPMN：Business Process Model and Notation，业务流程模型和符号
---
## 使用步骤
1. 部署 Activiti
2. 定义流程
3. 部署流程
4. 启动流程实例
5. 查询代办任务
6. 办理任务
7. 流程结束
---
## 基本使用
1. [依赖](pom.xml)
2. [log4j 配置文件](src/main/resources/log4j.properties)
3. [activiti 配置文件](src/main/resources/activiti.cfg.xml)
4. [Activiti 的表](https://blog.csdn.net/qq_38828126/article/details/118889329)
    - [ProcessEngine](src/test/java/com/ljh/ProcessEngineTests.java)
        - ge: general
        - hi: history
        - re: repository
        - ru: runtime
5. [Service](https://blog.csdn.net/qq_38828126/article/details/118889329)
    - [Service](src/test/java/com/ljh/ServiceTests.java)
        - RepositoryService: 资源管理类
        - RuntimeService: 流程运行管理类
        - TaskService: 任务管理类
        - HistoryService: 历史管理类
        - ManagerService: 引擎管理类
---
