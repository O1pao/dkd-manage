package com.dml.manage.domain.vo;

import com.dml.manage.domain.VendingMachine;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 故障设备类
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FaultyVendingMachineVo extends VendingMachine {
    private String createBy;
    private String updateBy;
    private String remark;
    private String vmTypeName; // 在 VendingMachine 中没有，保留
    private int businessId; // 重复，保留，但不同名字 (businessType)
    private String regionName;
    private int ownerId;
    private String ownerName;
}
