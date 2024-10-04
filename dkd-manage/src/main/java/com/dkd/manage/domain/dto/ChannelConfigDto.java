package com.dkd.manage.domain.dto;

import lombok.Data;

import java.util.List;

@Data
public class ChannelConfigDto {
    // 售货机编号
    private String innerCode;
    // 货道配置集合
    private List<ChannelSkuDto> channelList;
}
