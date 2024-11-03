package com.dml.manage.domain.vo;

import com.dml.manage.domain.Region;
import lombok.Data;

@Data
public class RegionVo extends Region {
    // 点位数量
    private Integer nodeCount;
}
