package com.gobrs.async;

import com.gobrs.async.autoconfig.GobrsAsyncProperties;
import com.gobrs.async.domain.AsyncParam;
import com.gobrs.async.domain.AsyncResult;
import com.gobrs.async.exception.NotTaskRuleException;
import com.gobrs.async.task.AsyncTask;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @program: gobrs-async-starter
 * @ClassName gobrs-Async
 * @description:
 * @author: sizegang
 * @create: 2022-03-16
 **/
public class GobrsAsync {


    @Autowired
    private GobrsAsyncProperties gobrsAsyncProperties;
    private Map<String, TaskFlow> taskFlow;

    private Map<String, TaskProcess> trigger;


    public TaskRecevie begin(String taskName, AsyncTask... tasks) {
        return taskFlow.get(taskName).start(tasks);
    }


    public TaskRecevie begin(String taskName, List<AsyncTask> asyncTasks) {
        if (taskFlow == null) {
            loadTaskFlow(taskName);
        }
        if (taskFlow.get(taskName) == null) {
            loadTaskFlowForOne(taskName);
        }
        return taskFlow.get(taskName).start(asyncTasks);
    }

    public TaskRecevie after(String taskName, AsyncTask... eventHandlers) {
        return taskFlow.get(taskName).after(eventHandlers);
    }

    public AsyncResult go(String taskName, AsyncParam param, long timeout) {
        if (check(taskName).isPresent()) {
            return trigger.get(taskName).trigger(param, timeout).load();
        }
        throw new NotTaskRuleException("Gobrs Rule Name Is Error");

    }

    public AsyncResult go(String taskName, AsyncParam param) {
        return go(taskName, param, 0L);
    }


    public synchronized void readyTo(String taskName) {
        if (trigger == null) {
            loadTrigger(taskName);
        }
        if (trigger.get(taskName) == null) {
            loadTriggerForOne(taskName);
        }
    }

    private void loadTaskFlow(String taskName) {
        taskFlow = new HashMap<>();
        TaskFlow tf = new TaskFlow();
        tf.setGobrsAsyncProperties(gobrsAsyncProperties);
        taskFlow.put(taskName, tf);
    }

    private void loadTaskFlowForOne(String taskName) {
        TaskFlow tf = new TaskFlow();
        tf.setGobrsAsyncProperties(gobrsAsyncProperties);
        taskFlow.put(taskName, tf);
    }

    private void loadTrigger(String taskName) {
        trigger = new HashMap<>();
        TaskProcess tr = new TaskProcess(taskFlow.get(taskName));
        trigger.put(taskName, tr);
    }

    private void loadTriggerForOne(String taskName) {
        TaskProcess tr = new TaskProcess(taskFlow.get(taskName));
        trigger.put(taskName, tr);
    }

    private Optional<TaskProcess> check(String ruleName) {
        return Optional.ofNullable(trigger.get(ruleName));
    }

}
