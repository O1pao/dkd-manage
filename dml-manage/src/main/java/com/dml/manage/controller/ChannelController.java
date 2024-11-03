package com.dml.manage.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.dml.manage.domain.dto.ChannelConfigDto;
import com.dml.manage.domain.vo.ChannelVo;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.dml.common.annotation.Log;
import com.dml.common.core.controller.BaseController;
import com.dml.common.core.domain.AjaxResult;
import com.dml.common.enums.BusinessType;
import com.dml.manage.domain.Channel;
import com.dml.manage.service.IChannelService;
import com.dml.common.utils.poi.ExcelUtil;
import com.dml.common.core.page.TableDataInfo;

/**
 * 售货机货道Controller
 *
 * @author op
 * @date 2024-09-27
 */
@RestController
@RequestMapping("/manage/channel")
public class ChannelController extends BaseController
{
    @Autowired
    private IChannelService channelService;

    /**
     * 查询售货机货道列表
     */
    @PreAuthorize("@ss.hasPermi('manage:channel:list')")
    @GetMapping("/list")
    public TableDataInfo list(Channel channel)
    {
        startPage();
        List<Channel> list = channelService.selectChannelList(channel);
        return getDataTable(list);
    }

    /**
     * 根据售货机编号查询货道列表
     * @param innerCode
     * @return
     */
    @PreAuthorize("@ss.hasPermi('manage:channel:list')")
    @GetMapping("/list/{innerCode}")
    public AjaxResult selectChannelVoListByInnerCode(@PathVariable String innerCode)
    {
        List<ChannelVo> channelVos = channelService.selectChannelVoListByInnerCode(innerCode);
        return success(channelVos);
    }

    /**
     * 导出售货机货道列表
     */
    @PreAuthorize("@ss.hasPermi('manage:channel:export')")
    @Log(title = "售货机货道", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Channel channel)
    {
        List<Channel> list = channelService.selectChannelList(channel);
        ExcelUtil<Channel> util = new ExcelUtil<Channel>(Channel.class);
        util.exportExcel(response, list, "售货机货道数据");
    }

    /**
     * 获取售货机货道详细信息
     */
    @PreAuthorize("@ss.hasPermi('manage:channel:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(channelService.selectChannelById(id));
    }

    /**
     * 新增售货机货道
     */
    @PreAuthorize("@ss.hasPermi('manage:channel:add')")
    @Log(title = "售货机货道", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Channel channel)
    {
        return toAjax(channelService.insertChannel(channel));
    }

    /**
     * 修改售货机货道
     */
    @PreAuthorize("@ss.hasPermi('manage:channel:edit')")
    @Log(title = "售货机货道", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Channel channel)
    {
        return toAjax(channelService.updateChannel(channel));
    }

    /**
     * 删除售货机货道
     */
    @PreAuthorize("@ss.hasPermi('manage:channel:remove')")
    @Log(title = "售货机货道", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(channelService.deleteChannelByIds(ids));
    }

    /**
     * 设置货道关联商品
     * @param channelConfigDto
     * @return
     */
    @PreAuthorize("@ss.hasPermi('manage:channel:edit')")
    @Log(title = "售货机货道", businessType = BusinessType.UPDATE)
    @PutMapping("/config")
    public AjaxResult setChannel(@RequestBody ChannelConfigDto channelConfigDto)
    {
        return toAjax(channelService.setChannel(channelConfigDto));
    }

}