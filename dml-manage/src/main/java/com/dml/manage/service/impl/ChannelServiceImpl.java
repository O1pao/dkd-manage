package com.dml.manage.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import com.dml.common.utils.DateUtils;
import com.dml.manage.domain.dto.ChannelConfigDto;
import com.dml.manage.domain.vo.ChannelVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.dml.manage.mapper.ChannelMapper;
import com.dml.manage.domain.Channel;
import com.dml.manage.service.IChannelService;

/**
 * 售货机货道Service业务层处理
 *
 * @author op
 * @date 2024-09-27
 */
@Service
public class ChannelServiceImpl implements IChannelService
{
    @Autowired
    private ChannelMapper channelMapper;

    /**
     * 查询售货机货道
     *
     * @param id 售货机货道主键
     * @return 售货机货道
     */
    @Override
    public Channel selectChannelById(Long id)
    {
        return channelMapper.selectChannelById(id);
    }

    /**
     * 查询售货机货道列表
     *
     * @param channel 售货机货道
     * @return 售货机货道
     */
    @Override
    public List<Channel> selectChannelList(Channel channel)
    {
        return channelMapper.selectChannelList(channel);
    }

    /**
     * 新增售货机货道
     *
     * @param channel 售货机货道
     * @return 结果
     */
    @Override
    public int insertChannel(Channel channel)
    {
        channel.setCreateTime(DateUtils.getNowDate());
        return channelMapper.insertChannel(channel);
    }

    /**
     * 修改售货机货道
     *
     * @param channel 售货机货道
     * @return 结果
     */
    @Override
    public int updateChannel(Channel channel)
    {
        channel.setUpdateTime(DateUtils.getNowDate());
        return channelMapper.updateChannel(channel);
    }

    /**
     * 批量删除售货机货道
     *
     * @param ids 需要删除的售货机货道主键
     * @return 结果
     */
    @Override
    public int deleteChannelByIds(Long[] ids)
    {
        return channelMapper.deleteChannelByIds(ids);
    }

    /**
     * 删除售货机货道信息
     *
     * @param id 售货机货道主键
     * @return 结果
     */
    @Override
    public int deleteChannelById(Long id)
    {
        return channelMapper.deleteChannelById(id);
    }

    /**
     * 批量新增售货机货道
     *
     * @param channelList
     * @return 结果
     */
    @Override
    public int batchInsertChannel(List<Channel> channelList) {
        return channelMapper.batchInsertChannel(channelList);
    }

    /**
     * 根据商品id集合查询货道
     * @param skuIds
     * @return
     */
    @Override
    public int countChannelBySkuIds(List<Long> skuIds){
        return channelMapper.countChannelBySkuIds(skuIds);
    }

    /**
     * 根据售货机软编号查询货道列表
     * @param innerCode
     * @return List<ChannelVo>
     */
    @Override
    public List<ChannelVo> selectChannelVoListByInnerCode(String innerCode){
        return channelMapper.selectChannelVoListByInnerCode(innerCode);
    }

    /**
     * 货道关联商品
     * @param channelConfigDto
     * @return
     */
    @Override
    public int setChannel(ChannelConfigDto channelConfigDto) {
        List<Channel> channelList = channelConfigDto.getChannelList().stream().map(dto -> {
            Channel channel = channelMapper.getChannelInfo(dto.getInnerCode(), dto.getChannelCode());
            if (channel != null) {
                channel.setSkuId(dto.getSkuId());
                channel.setUpdateTime(DateUtils.getNowDate());
            }
            return channel;
        }).collect(Collectors.toList());
        return channelMapper.batchUpdateChannel(channelList);
    }

    /**
     * 根据机器ids删除货道
     * @param ids
     * @return
     */
    @Override
    public int deleteChannelByVmIds(Long[] ids) {
        return channelMapper.deleteChannelByVmIds(ids);
    }

    /**
     * 根据机器id删除货道
     * @param id
     * @return
     */
    @Override
    public int deleteChannelByVmId(Long id) {
        return channelMapper.deleteChannelByVmId(id);
    }
}
