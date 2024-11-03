package com.dml.manage.domain.vo;

import com.dml.common.constant.DelContents;
import lombok.Data;

import java.util.Date;

/**
 * 当月工单统计
 */
@Data
public class TaskStatisticsVO {

    private Integer total;

    private Integer completedTotal;

    private Integer cancelTotal;

    private Integer progressTotal;

    private Integer workerCount;

    private Boolean repair;

    private Date date;

    private String startTime;

    private String endTime;

    private Long completedStatus = DelContents.TASK_STATUS_FINISH;

    private Long cancelStatus = DelContents.TASK_STATUS_CANCEL;

    private Long progressStatus = DelContents.TASK_STATUS_PROGRESS;
}
