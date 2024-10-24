package com.dkd.manage.mapper;

import java.util.List;
import java.util.Map;

import com.dkd.manage.domain.AddrAndAmount;
import com.dkd.manage.domain.Order;
import com.dkd.manage.domain.vo.OrderStatisticsVo;
import com.dkd.manage.domain.vo.OrderVo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 订单管理Mapper接口
 *
 * @author itheima
 * @date 2024-07-29
 */
public interface OrderMapper
{
    /**
     * 查询订单管理
     *
     * @param id 订单管理主键
     * @return 订单管理
     */
    public Order selectOrderById(Long id);

    /**
     * 查询订单管理列表
     *
     * @param order 订单管理
     * @return 订单管理集合
     */
    public List<Order> selectOrderList(Order order);

    /**
     * 新增订单管理
     *
     * @param order 订单管理
     * @return 结果
     */
    public int insertOrder(Order order);

    /**
     * 修改订单管理
     *
     * @param order 订单管理
     * @return 结果
     */
    public int updateOrder(Order order);

    /**
     * 删除订单管理
     *
     * @param id 订单管理主键
     * @return 结果
     */
    public int deleteOrderById(Long id);

    /**
     * 批量删除订单管理
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteOrderByIds(Long[] ids);

    /**
     * 统计当月的订单统计信息
     * @return
     */
    OrderStatisticsVo getThisMonthData(OrderStatisticsVo orderStatisticsVo);

    /**
     * 统计当月的Top10商品信息
     * @return
     */
    List<OrderVo> getTop10();

    /**
     * 根据x轴数据日期列表获取对应日期的销售额数据
     * @param lineXAxisData
     * @return
     */
    List<Double> getSeriesDataByLineXAxisData(List<String> lineXAxisData);

    /**
     * 获取销售额前三的销售点位
     * @param startTime
     * @param endTime
     * @return
     */
    List<AddrAndAmount> getTopThreeAddrAndAmount(@Param("startTime") String startTime, @Param("endTime") String endTime);

    /**
     * 获取所有订单的id集合
     * @return
     */
    @Select("select id from tb_order")
    List<Long> selectAllOrderId();

    /**
     * 修改订单时间数据
     * @return 影响行数
     */
    Integer updateAllOrderTime(@Param("ids") List<Long> ids);

}
