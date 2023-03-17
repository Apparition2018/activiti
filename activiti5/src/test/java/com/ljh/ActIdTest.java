package com.ljh;

import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.IdentityService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.impl.persistence.entity.GroupEntity;
import org.activiti.engine.impl.persistence.entity.UserEntity;
import org.junit.Test;

/**
 * <a href="https://www.bilibili.com/video/BV1Ut411y7NT?p=21">ACT_ID_*</a>
 *
 * @author ljh
 * @since 2023/3/15 17:26
 */
@Slf4j
public class ActIdTest {

    private final ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

    @Test
    public void testCreateGroupAndUser() {
        IdentityService identityService = this.processEngine.getIdentityService();

        // ACT_ID_GROUP
        GroupEntity group = new GroupEntity("1");
        group.setName("部门经理");
        identityService.saveGroup(group);
        GroupEntity group2 = new GroupEntity("2");
        group2.setName("总经理");
        identityService.saveGroup(group2);
        // ACT_ID_USER
        UserEntity user = new UserEntity("1");
        user.setFirstName("小明");
        identityService.saveUser(user);
        UserEntity user2 = new UserEntity("2");
        user2.setFirstName("小张");
        identityService.saveUser(user2);
        UserEntity user3 = new UserEntity("3");
        user3.setFirstName("小王");
        identityService.saveUser(user3);
        // ACT_ID_MEMBERSHIP
        identityService.createMembership("1", "1");
        identityService.createMembership("2", "1");
        identityService.createMembership("3", "2");
    }
}
