package com.dml.manage.mapper;

import java.util.List;
import com.dml.manage.domain.VendingMachine;
import com.dml.manage.domain.vo.FaultyVendingMachineVo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 设备管理Mapper接口
 *
 * @author op
 * @date 2024-09-27
 */
public interface VendingMachineMapper
{
    /**
     * 查询设备管理
     *
     * @param id 设备管理主键
     * @return 设备管理
     */
    public VendingMachine selectVendingMachineById(Long id);

    /**
     * 查询设备管理列表
     *
     * @param vendingMachine 设备管理
     * @return 设备管理集合
     */
    public List<VendingMachine> selectVendingMachineList(VendingMachine vendingMachine);

    /**
     * 新增设备管理
     *
     * @param vendingMachine 设备管理
     * @return 结果
     */
    public int insertVendingMachine(VendingMachine vendingMachine);

    /**
     * 修改设备管理
     *
     * @param vendingMachine 设备管理
     * @return 结果
     */
    public int updateVendingMachine(VendingMachine vendingMachine);

    /**
     * 删除设备管理
     *
     * @param id 设备管理主键
     * @return 结果
     */
    public int deleteVendingMachineById(Long id);

    /**
     * 批量删除设备管理
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteVendingMachineByIds(Long[] ids);

    /**
     * 根据 innerCode（设备编号） 查询设备
     * @param innerCode
     * @return VendingMachine
     */
    @Select("select * from tb_vending_machine where inner_code = #{innerCode}")
    public VendingMachine selectVendingMachineByInnerCode(@Param("innerCode") String innerCode);

    /**
     * 查询故障的机器
     * @return
     */
    List<FaultyVendingMachineVo> getFaultyVendingMachineList(@Param("vmStatus")Long vmStatus);
}
