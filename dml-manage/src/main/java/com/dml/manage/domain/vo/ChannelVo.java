package com.dml.manage.domain.vo;

import com.dml.manage.domain.Channel;
import com.dml.manage.domain.Sku;
import lombok.Data;

@Data
public class ChannelVo extends Channel {

    // 商品对象
    private Sku sku;
}
