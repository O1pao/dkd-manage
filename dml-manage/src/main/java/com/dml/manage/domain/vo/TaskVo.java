package com.dml.manage.domain.vo;

import com.dml.manage.domain.Task;
import com.dml.manage.domain.TaskType;
import lombok.Data;

@Data
public class TaskVo extends Task {

    // 工单类型
    private TaskType taskType;
}
