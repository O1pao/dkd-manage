package com.dml.quartz.task;

import com.dml.common.core.domain.AjaxResult;
import com.dml.manage.controller.VendingMachineController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import com.dml.common.utils.StringUtils;

/**
 * 定时任务调度测试
 *
 * @author ruoyi
 */
@Component("ryTask")
public class RyTask
{
    @Autowired
    private VendingMachineController vendingMachineController;

    // 每分钟检测故障的vm
    public AjaxResult DetectingFaultyVM(){
        try {
            // 设置匿名用户
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken("anonymousUser", null);
            SecurityContextHolder.getContext().setAuthentication(authentication);

            return AjaxResult.success(vendingMachineController.getFaultyVendingMachineList());
        }catch (Exception e){
            return AjaxResult.error("任务执行异常", e);
        }finally {
            // 清除 SecurityContext
            SecurityContextHolder.clearContext();
        }
    }

    public void ryMultipleParams(String s, Boolean b, Long l, Double d, Integer i)
    {
        System.out.println(StringUtils.format("执行多参方法： 字符串类型{}，布尔类型{}，长整型{}，浮点型{}，整形{}", s, b, l, d, i));
    }

    public void ryParams(String params)
    {
        System.out.println("执行有参方法：" + params);
    }

    public void ryNoParams()
    {
        System.out.println("执行无参方法");
    }
}
