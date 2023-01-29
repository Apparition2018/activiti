package com.ljh.listener;

import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.delegate.event.ActivitiEvent;
import org.activiti.engine.delegate.event.ActivitiEventListener;

/**
 * MyEventListener
 * <p><a href="https://www.activiti.org/5.x/userguide/#eventDispatcher">事件监听器实现</a>
 *
 * @author ljh
 * @since 2023/1/28 8:58
 */
@Slf4j
public class MyEventListener implements ActivitiEventListener {
    @Override
    public void onEvent(ActivitiEvent event) {
        switch (event.getType()) {
            case JOB_EXECUTION_SUCCESS:
                log.warn("A job well done!");
                break;
            case JOB_EXECUTION_FAILURE:
                log.warn("A job has failed...");
                break;
            default:
                log.warn("Event received: {}", event.getType());
        }
    }

    @Override
    public boolean isFailOnException() {
        // false-忽略异常，true-异常冒泡
        // 如果监听器中的行为不是业务关键，建议返回 false
        return false;
    }
}
