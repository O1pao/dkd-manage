package com.dml.manage.domain.dto;

import lombok.Data;

/**
 * 工单详情Dto
 */

@Data
public class TaskDetailDto {
    // 货道编号
    private String channelCode;
    // 期望补货数量
    private Long expectCapacity;
    // 商品id
    private Long skuId;
    // 商品名称
    private String skuName;
    // 商品图片
    private String skuImage;
}
