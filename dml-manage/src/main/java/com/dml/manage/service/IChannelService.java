package com.dml.manage.service;

import java.util.List;
import com.dml.manage.domain.Channel;
import com.dml.manage.domain.dto.ChannelConfigDto;
import com.dml.manage.domain.vo.ChannelVo;

/**
 * 售货机货道Service接口
 *
 * @author op
 * @date 2024-09-27
 */
public interface IChannelService
{
    /**
     * 查询售货机货道
     *
     * @param id 售货机货道主键
     * @return 售货机货道
     */
    public Channel selectChannelById(Long id);

    /**
     * 查询售货机货道列表
     *
     * @param channel 售货机货道
     * @return 售货机货道集合
     */
    public List<Channel> selectChannelList(Channel channel);

    /**
     * 新增售货机货道
     *
     * @param channel 售货机货道
     * @return 结果
     */
    public int insertChannel(Channel channel);

    /**
     * 修改售货机货道
     *
     * @param channel 售货机货道
     * @return 结果
     */
    public int updateChannel(Channel channel);

    /**
     * 批量删除售货机货道
     *
     * @param ids 需要删除的售货机货道主键集合
     * @return 结果
     */
    public int deleteChannelByIds(Long[] ids);

    /**
     * 删除售货机货道信息
     *
     * @param id 售货机货道主键
     * @return 结果
     */
    public int deleteChannelById(Long id);

    /**
     * 批量新增售货机货道
     * @param channelList
     * @return 结果
     */
    public int batchInsertChannel(List<Channel> channelList);

    /**
     * 根据商品id集合查询货道
     * @param skuIds
     * @return
     */
    int countChannelBySkuIds(List<Long> skuIds);

    /**
     * 根据售货机软编号查询货道列表
     * @param innerCode
     * @return List<ChannelVo>
     */
    List<ChannelVo> selectChannelVoListByInnerCode(String innerCode);

    /**
     * 货道关联商品
     * @param channelConfigDto
     * @return
     */
    int setChannel(ChannelConfigDto channelConfigDto);

    /**
     * 根据机器id删除货道
     * @param ids
     * @return
     */
    int deleteChannelByVmIds(Long[] ids);

    /**
     * 根据机器id删除货道
     * @param id
     * @return
     */
    public int deleteChannelByVmId(Long id);
}