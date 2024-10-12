package com.dkd.manage.domain.dto;

import lombok.Data;

import java.util.List;

/**
 * 新建工单Dto
 */

@Data
public class TaskDto {
    // 创建类型
    private Long createType;
    // 关联设备编号
    private String innerCode;
    // 任务执行人id
    private Long userId;
    // 用户创建人id
    private Long assignorId;
    // 工单类型
    private Long productTypeId;
    // 描述信息
    private String desc;
    // 工单详情
    private List<TaskDetailDto> details;
}
