package com.dml.manage.service;

import java.util.List;
import com.dml.manage.domain.Order;
import com.dml.manage.domain.vo.OrderDatesAndAmountVo;
import com.dml.manage.domain.vo.OrderStatisticsVo;

/**
 * 订单管理Service接口
 *
 * @author itheima
 * @date 2024-07-29
 */
public interface IOrderService
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
     * 批量删除订单管理
     *
     * @param ids 需要删除的订单管理主键集合
     * @return 结果
     */
    public int deleteOrderByIds(Long[] ids);

    /**
     * 删除订单管理信息
     *
     * @param id 订单管理主键
     * @return 结果
     */
    public int deleteOrderById(Long id);

    /**
     * 统计当月的订单统计信息
     * @return
     */
    public OrderStatisticsVo getThisMonthData(String start, String end);

    /**
     * 统计某个时间段每日的日期以及销售额
     * @param start
     * @param end
     * @return
     */
    OrderDatesAndAmountVo getSomeTimeData(String start, String end);

    /**
     * 修改订单时间数据
     */
    Integer updateAllOrderTime();
}
