package com.dml.manage.mapper;

import java.util.List;
import com.dml.manage.domain.Channel;
import com.dml.manage.domain.vo.ChannelVo;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 售货机货道Mapper接口
 *
 * @author op
 * @date 2024-09-27
 */
public interface ChannelMapper
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
     * 批量修改售货机货道
     * @param channelList
     * @return
     */
    public int batchUpdateChannel(List<Channel> channelList);

    /**
     * 删除售货机货道
     *
     * @param id 售货机货道主键
     * @return 结果
     */
    public int deleteChannelById(Long id);

    /**
     * 批量删除售货机货道
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteChannelByIds(Long[] ids);

    /**
     * 批量新增售货机货道
     *
     * @param channelList
     * @return 结果
     */
    int batchInsertChannel(List<Channel> channelList);

    /**
     * 根据商品id集合统计货道数量
     * @param skuIds
     * @return 统计结果
     */
    int countChannelBySkuIds(List<Long> skuIds);

    /**
     * 根据售货机软编号查询货道列表
     * @param innerCode
     * @return List<ChannelVo>
     */
    List<ChannelVo> selectChannelVoListByInnerCode(String innerCode);

    /**
     * 根据售货机编号和货道编号查询货道信息
     * @param innerCode
     * @param channelCode
     * @return
     */
    @Select("select * from tb_channel where inner_code = #{innerCode} and channel_code = #{channelCode}")
    Channel getChannelInfo(@Param("innerCode") String innerCode, @Param("channelCode") String channelCode);

    /**
     * 根据机器ids删除货道
     * @param ids
     * @return
     */
    int deleteChannelByVmIds(Long[] ids);

    /**
     * 根据机器id删除货道
     * @param id
     * @return
     */
    @Delete("delete from tb_channel where vm_id = #{id}")
    int deleteChannelByVmId(@Param("id") Long id);
}
