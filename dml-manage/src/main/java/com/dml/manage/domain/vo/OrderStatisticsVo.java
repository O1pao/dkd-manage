package com.dml.manage.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 过去7天的订单统计
 */

@Data
@ApiModel(value = "OrderStatistics", description = "过去一周的订单统计信息")
public class OrderStatisticsVo {

    @ApiModelProperty("过去一周的订单量")
    private Integer orderCountNum;

    @ApiModelProperty("过去一周的销售额")
    private Integer orderAmountNum;

    @ApiModelProperty("开始时间")
    private String startTime;

    @ApiModelProperty("结束时间")
    private String endTime;

    @ApiModelProperty("热销商品Top10")
    private List<OrderVo> top10;
}
