package com.ljh.listener;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

/**
 * MyAssignmentHandler
 *
 * @author ljh
 * @since 2023/3/15 15:20
 */
public class MyAssignmentHandler implements TaskListener {

    private static final long serialVersionUID = -82692668064297872L;

    @Override
    public void notify(DelegateTask delegateTask) {
        delegateTask.setAssignee("赵六");
    }
}
