package com.dkd.manage.domain.vo;

import com.dkd.manage.domain.Order;
import lombok.Data;

@Data
public class OrderVo extends Order {

    // 将id转换成String类型
    private String stringId;

    // 商品总数
    private Integer totalCount;

    // 商品总金额
    private Double totalAmount;
}
