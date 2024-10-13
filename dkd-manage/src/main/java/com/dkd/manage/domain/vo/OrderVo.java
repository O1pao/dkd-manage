package com.dkd.manage.domain.vo;

import com.dkd.manage.domain.Order;
import lombok.Data;

@Data
public class OrderVo extends Order {
    // 将id转换成String类型
    private String stringId;
}
