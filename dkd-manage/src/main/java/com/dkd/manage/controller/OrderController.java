package com.dkd.manage.controller;

import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletResponse;

import cn.hutool.core.bean.BeanUtil;
import com.dkd.common.core.domain.R;
import com.dkd.manage.domain.vo.OrderDatesAndAmountVo;
import com.dkd.manage.domain.vo.OrderStatisticsVo;
import com.dkd.manage.domain.vo.OrderVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.dkd.common.annotation.Log;
import com.dkd.common.core.controller.BaseController;
import com.dkd.common.core.domain.AjaxResult;
import com.dkd.common.enums.BusinessType;
import com.dkd.manage.domain.Order;
import com.dkd.manage.service.IOrderService;
import com.dkd.common.utils.poi.ExcelUtil;
import com.dkd.common.core.page.TableDataInfo;

/**
 * 订单管理Controller
 *
 * @author itheima
 * @date 2024-07-29
 */
@RestController
@RequestMapping("/manage/order")
@Api("订单信息管理")
public class OrderController extends BaseController
{
    @Autowired
    private IOrderService orderService;

    /**
     * 查询订单管理列表
     */
    @PreAuthorize("@ss.hasPermi('manage:order:list')")
    @GetMapping("/list")
    public TableDataInfo list(Order order)
    {
        startPage();
        List<Order> orderList = orderService.selectOrderList(order);
        List<OrderVo> voList = orderList.stream().map(o -> {
            OrderVo orderVo = BeanUtil.copyProperties(o, OrderVo.class);
            orderVo.setStringId(o.getId().toString());
            return orderVo;
        }).collect(Collectors.toList());
        return getDataTable(voList);
    }

    /**
     * 导出订单管理列表
     */
    @PreAuthorize("@ss.hasPermi('manage:order:export')")
    @Log(title = "订单管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Order order)
    {
        List<Order> list = orderService.selectOrderList(order);
        ExcelUtil<Order> util = new ExcelUtil<Order>(Order.class);
        util.exportExcel(response, list, "订单管理数据");
    }

    /**
     * 获取订单管理详细信息
     */
    @PreAuthorize("@ss.hasPermi('manage:order:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(orderService.selectOrderById(id));
    }

    /**
     * 新增订单管理
     */
    @PreAuthorize("@ss.hasPermi('manage:order:add')")
    @Log(title = "订单管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Order order)
    {
        return toAjax(orderService.insertOrder(order));
    }

    /**
     * 修改订单管理
     */
    @PreAuthorize("@ss.hasPermi('manage:order:edit')")
    @Log(title = "订单管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Order order)
    {
        return toAjax(orderService.updateOrder(order));
    }

    /**
     * 删除订单管理
     */
    @PreAuthorize("@ss.hasPermi('manage:order:remove')")
    @Log(title = "订单管理", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(orderService.deleteOrderByIds(ids));
    }

    /**
     * 统计当月的订单统计信息
     * @return
     */
    @ApiOperation("统计当月的订单统计信息")
    @GetMapping("/getThisMonthData")
    public R<OrderStatisticsVo> getThisMonthData(@RequestParam String start, @RequestParam String end){
        return R.ok(orderService.getThisMonthData(start, end));
    }

    /**
     * 统计某个时间段每日的日期以及销售额
     * @return
     */
    @ApiOperation("统计某个时间段统计信息")
    @GetMapping("/getSomeTimeData")
    public R<OrderDatesAndAmountVo> getSomeTimeData(@RequestParam String start, @RequestParam String end){
        return R.ok(orderService.getSomeTimeData(start, end));
    }

    /**
     * 修改订单时间数据
     */
    @PreAuthorize("@ss.hasPermi('manage:order:edit')")
    @Log(title = "订单管理", businessType = BusinessType.UPDATE)
    @ApiOperation("修改订单时间数据")
    @PutMapping("/updateAllOrderTime")
    public R<Integer> updateAllOrderTime(){
        return R.ok(orderService.updateAllOrderTime());
    }
}
