package com.dkd.manage.service.impl;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.dkd.common.utils.DateUtils;
import com.dkd.manage.domain.AddrAndAmount;
import com.dkd.manage.domain.vo.OrderDatesAndAmountVo;
import com.dkd.manage.domain.vo.OrderStatisticsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.dkd.manage.mapper.OrderMapper;
import com.dkd.manage.domain.Order;
import com.dkd.manage.service.IOrderService;

import javax.swing.text.DateFormatter;

/**
 * 订单管理Service业务层处理
 *
 * @author itheima
 * @date 2024-07-29
 */
@Service
public class OrderServiceImpl implements IOrderService
{
    @Autowired
    private OrderMapper orderMapper;

    /**
     * 查询订单管理
     *
     * @param id 订单管理主键
     * @return 订单管理
     */
    @Override
    public Order selectOrderById(Long id)
    {
        return orderMapper.selectOrderById(id);
    }

    /**
     * 查询订单管理列表
     *
     * @param order 订单管理
     * @return 订单管理
     */
    @Override
    public List<Order> selectOrderList(Order order)
    {
        return orderMapper.selectOrderList(order);
    }

    /**
     * 新增订单管理
     *
     * @param order 订单管理
     * @return 结果
     */
    @Override
    public int insertOrder(Order order)
    {
        order.setCreateTime(DateUtils.getNowDate());
        return orderMapper.insertOrder(order);
    }

    /**
     * 修改订单管理
     *
     * @param order 订单管理
     * @return 结果
     */
    @Override
    public int updateOrder(Order order)
    {
        order.setUpdateTime(DateUtils.getNowDate());
        return orderMapper.updateOrder(order);
    }

    /**
     * 批量删除订单管理
     *
     * @param ids 需要删除的订单管理主键
     * @return 结果
     */
    @Override
    public int deleteOrderByIds(Long[] ids)
    {
        return orderMapper.deleteOrderByIds(ids);
    }

    /**
     * 删除订单管理信息
     *
     * @param id 订单管理主键
     * @return 结果
     */
    @Override
    public int deleteOrderById(Long id)
    {
        return orderMapper.deleteOrderById(id);
    }

    /**
     * 统计当月的订单统计信息
     * @return
     */
    @Override
    public OrderStatisticsVo getThisMonthData(String start, String end) {
        OrderStatisticsVo osVo = new OrderStatisticsVo();
        osVo.setStartTime(start.replace('.', '-'));
        osVo.setEndTime(end.replace('.', '-'));
        // 获取一个时间段内的订单量、销售额
        osVo = orderMapper.getThisMonthData(osVo);
        // 获取商品销量Top10
        osVo.setTop10(orderMapper.getTop10());
        return osVo;
    }

    /**
     * 统计某个时间段每日的日期以及销售额
     * @param start
     * @param end
     * @return
     */
    @Override
    public OrderDatesAndAmountVo getSomeTimeData(String start, String end) {
        OrderDatesAndAmountVo odaAVo = new OrderDatesAndAmountVo();

        // TODO 功能未完成，先返回一周的数据
        // 设置结束日期
        odaAVo.setEndTime(end.replace('.', '-'));
        // 定义日期格式
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        // 将字符串转化为LocalDate
        LocalDate endDate = LocalDate.parse(end.replace('.', '-'), formatter);
        LocalDate startDate = endDate.minusWeeks(1);
        // 设置开始日期
        odaAVo.setStartTime(startDate.format(formatter));

        // 生成日期范围
        odaAVo.setLineXAxisData(generateDate(startDate, endDate));
        // 根据x轴数据日期列表获取对应日期的销售额趋势图数据
        odaAVo.setLineSeriesData(orderMapper.getSeriesDataByLineXAxisData(odaAVo.getLineXAxisData()));

        // 获取前三的销售点位
        List<AddrAndAmount> topThree = orderMapper.getTopThreeAddrAndAmount(odaAVo.getStartTime(), odaAVo.getEndTime());

        // 设置当周排名前三点位
        odaAVo.setBarXAxisData(topThree.stream().map(AddrAndAmount::getAddr).collect(Collectors.toList()));
        // 设置当周排名前三点位对应的销售额
        odaAVo.setBarSeriesData(topThree.stream().map(AddrAndAmount::getTotalAmount).collect(Collectors.toList()));
        return odaAVo;
    }

    /**
     * 修改订单时间数据
     */
    @Override
    public Integer updateAllOrderTime() {
        List<Long> ids = orderMapper.selectAllOrderId();
        return orderMapper.updateAllOrderTime(ids);
    }

    /**
     * 生成日期范围
     * @param startDate
     * @param endDate
     * @return
     */
    private List<String> generateDate(LocalDate startDate, LocalDate endDate){
        List<String> dateList = new ArrayList<>();
        // 定义日期格式
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        startDate = endDate.minusWeeks(1);
        // 循环生成日期并加入列表
        while (endDate.isAfter(startDate)) {
            // 将 LocalDate 格式化为字符串并添加到列表中
            dateList.add(startDate.format(formatter));
            // 日期加一天
            startDate = startDate.plusDays(1);
        }
        return dateList;
    }

}
