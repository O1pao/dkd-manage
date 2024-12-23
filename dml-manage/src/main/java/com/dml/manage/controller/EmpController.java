package com.dml.manage.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.dml.common.constant.DelContents;
import com.dml.manage.domain.VendingMachine;
import com.dml.manage.service.IVendingMachineService;
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
import com.dml.manage.domain.Emp;
import com.dml.manage.service.IEmpService;
import com.dml.common.utils.poi.ExcelUtil;
import com.dml.common.core.page.TableDataInfo;

/**
 * 人员列表Controller
 *
 * @author op
 * @date 2024-09-25
 */
@RestController
@RequestMapping("/manage/emp")
public class EmpController extends BaseController
{
    @Autowired
    private IEmpService empService;

    @Autowired
    private IVendingMachineService vendingMachineService;

    /**
     * 查询人员列表列表
     */
    @PreAuthorize("@ss.hasPermi('manage:emp:list')")
    @GetMapping("/list")
    public TableDataInfo list(Emp emp)
    {
        startPage();
        List<Emp> list = empService.selectEmpList(emp);
        return getDataTable(list);
    }

    /**
     * 导出人员列表列表
     */
    @PreAuthorize("@ss.hasPermi('manage:emp:export')")
    @Log(title = "人员列表", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Emp emp)
    {
        List<Emp> list = empService.selectEmpList(emp);
        ExcelUtil<Emp> util = new ExcelUtil<Emp>(Emp.class);
        util.exportExcel(response, list, "人员列表数据");
    }

    /**
     * 获取人员列表详细信息
     */
    @PreAuthorize("@ss.hasPermi('manage:emp:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(empService.selectEmpById(id));
    }

    /**
     * 新增人员列表
     */
    @PreAuthorize("@ss.hasPermi('manage:emp:add')")
    @Log(title = "人员列表", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Emp emp)
    {
        return toAjax(empService.insertEmp(emp));
    }

    /**
     * 修改人员列表
     */
    @PreAuthorize("@ss.hasPermi('manage:emp:edit')")
    @Log(title = "人员列表", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Emp emp)
    {
        return toAjax(empService.updateEmp(emp));
    }

    /**
     * 删除人员列表
     */
    @PreAuthorize("@ss.hasPermi('manage:emp:remove')")
    @Log(title = "人员列表", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(empService.deleteEmpByIds(ids));
    }

    /**
     * 根据售货机编号获取运营人员列表
     * @param innerCode
     * @return
     */
    @PreAuthorize("@ss.hasPermi('manage:emp:list')")
    @GetMapping("/businessList/{innerCode}")
    public AjaxResult businessList(@PathVariable("innerCode") String innerCode){
        // 根据售货机编号查询售货机信息
        VendingMachine vendingMachine = vendingMachineService.selectVendingMachineByInnerCode(innerCode);
        if(vendingMachine == null){
            return AjaxResult.error("未查询到该售货机信息");
        }
        // 根据区域id、角色编号、员工状态获取员工列表
        Emp emp = new Emp();
        emp.setRegionId(vendingMachine.getRegionId());
        emp.setRoleCode(DelContents.ROLE_CODE_BUSINESS);
        emp.setStatus(DelContents.EMP_STATUS_NORMAL);
        return AjaxResult.success(empService.selectEmpList(emp));
    }

    /**
     * 根据售货机编号获取运维人员列表
     * @param innerCode
     * @return
     */
    @PreAuthorize("@ss.hasPermi('manage:emp:list')")
    @GetMapping("/operationList/{innerCode}")
    public AjaxResult operationList(@PathVariable("innerCode") String innerCode){
        // 根据售货机编号查询售货机信息
        VendingMachine vendingMachine = vendingMachineService.selectVendingMachineByInnerCode(innerCode);
        if(vendingMachine == null){
            return AjaxResult.error("未查询到该售货机信息");
        }
        // 根据区域id、角色编号、员工状态获取员工列表
        Emp emp = new Emp();
        emp.setRegionId(vendingMachine.getRegionId());
        emp.setRoleCode(DelContents.ROLE_CODE_OPERATOR);
        emp.setStatus(DelContents.EMP_STATUS_NORMAL);
        return AjaxResult.success(empService.selectEmpList(emp));
    }
}
