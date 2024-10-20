package com.dkd.manage.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 过去7天的订单统计
 */

@Data
@ApiModel(value = "OrderStatistics", description = "过去一周的订单统计信息")
public class OrderDatesAndAmountVo {

    @ApiModelProperty("趋势图x轴日期数据")
    private List<String> lineXAxisData;

    @ApiModelProperty("趋势图x轴日期对应的销售额")
    private List<Double> lineSeriesData;

    @ApiModelProperty("当周排名前三点位")
    private List<String> barXAxisData;

    @ApiModelProperty("当周排名前三点位对应的销售额")
    private List<Double> barSeriesData;

    @ApiModelProperty("开始时间")
    private String startTime;

    @ApiModelProperty("结束时间")
    private String endTime;

}
