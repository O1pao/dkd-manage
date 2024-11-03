package com.dml.manage.domain.vo;

import com.dml.manage.domain.Node;
import com.dml.manage.domain.Partner;
import com.dml.manage.domain.Region;
import lombok.Data;

@Data
public class NodeVo extends Node {
    // 设备数量
    private int vmCount;

    // 区域
    private Region region;

    // 合作商
    private Partner partner;

    // 合作商数量
    private Integer partnerCount;
}
