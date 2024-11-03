package com.dml.manage.service.impl;

import java.util.*;
import java.util.stream.Collectors;

import com.dml.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.dml.manage.mapper.SkuMapper;
import com.dml.manage.domain.Sku;
import com.dml.manage.service.ISkuService;

/**
 * 商品管理Service业务层处理
 *
 * @author op
 * @date 2024-10-02
 */
@Service
public class SkuServiceImpl implements ISkuService
{
    @Autowired
    private SkuMapper skuMapper;

    @Autowired
    private ChannelServiceImpl channelService;

    /**
     * 查询商品管理
     *
     * @param skuId 商品管理主键
     * @return 商品管理
     */
    @Override
    public Sku selectSkuBySkuId(Long skuId)
    {
        return skuMapper.selectSkuBySkuId(skuId);
    }

    /**
     * 查询商品管理列表
     *
     * @param sku 商品管理
     * @return 商品管理
     */
    @Override
    public List<Sku> selectSkuList(Sku sku)
    {
        return skuMapper.selectSkuList(sku);
    }

    /**
     * 新增商品管理
     *
     * @param sku 商品管理
     * @return 结果
     */
    @Override
    public int insertSku(Sku sku)
    {
        sku.setCreateTime(DateUtils.getNowDate());
        return skuMapper.insertSku(sku);
    }

    /**
     * 批量新增商品管理
     * @param skuList
     * @return
     */
    @Override
    public int batchInsertSku(List<Sku> skuList){
        return skuMapper.batchInsertSku(skuList);
    }

    /**
     * 修改商品管理
     *
     * @param sku 商品管理
     * @return 结果
     */
    @Override
    public int updateSku(Sku sku)
    {
        sku.setUpdateTime(DateUtils.getNowDate());
        return skuMapper.updateSku(sku);
    }

    /**
     * 批量删除商品管理
     *
     * @param skuIds 需要删除的商品管理主键
     * @return 结果
     */
    @Override
    public int deleteSkuBySkuIds(Long[] skuIds)
    {
        // 1. 判断商品的id集合是否有关联货道
        List<Long> skuIdsList = Arrays.stream(skuIds).collect(Collectors.toList());
        int count = channelService.countChannelBySkuIds(skuIdsList);
        if(count > 0){
            throw new SecurityException("商品关联了货道，无法删除！");
        }
        // 2.商品未关联货道，直接删除
        return skuMapper.deleteSkuBySkuIds(skuIds);
    }

    /**
     * 删除商品管理信息
     *
     * @param skuId 商品管理主键
     * @return 结果
     */
    @Override
    public int deleteSkuBySkuId(Long skuId)
    {
        // 1. 判断商品的id集合是否有关联货道
        List<Long> skuIds = new ArrayList<>();
        skuIds.add(skuId);
        int count = channelService.countChannelBySkuIds(skuIds);
        if(count > 0){
            throw new SecurityException("商品关联了货道，无法删除！");
        }
        // 2.商品未关联货道，直接删除
        return skuMapper.deleteSkuBySkuId(skuId);
    }
}
