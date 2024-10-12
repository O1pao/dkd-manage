package com.dkd.manage.service.impl;

import java.time.Duration;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.dkd.common.constant.DkdContants;
import com.dkd.common.utils.DateUtils;
import com.dkd.manage.domain.Emp;
import com.dkd.manage.domain.TaskDetails;
import com.dkd.manage.domain.VendingMachine;
import com.dkd.manage.domain.dto.TaskDetailDto;
import com.dkd.manage.domain.dto.TaskDto;
import com.dkd.manage.domain.vo.TaskVo;
import com.dkd.manage.service.IEmpService;
import com.dkd.manage.service.ITaskDetailsService;
import com.dkd.manage.service.IVendingMachineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import com.dkd.manage.mapper.TaskMapper;
import com.dkd.manage.domain.Task;
import com.dkd.manage.service.ITaskService;
import org.springframework.transaction.annotation.Transactional;

/**
 * 工单Service业务层处理
 *
 * @author opao
 * @date 2024-10-08
 */
@Service
public class TaskServiceImpl implements ITaskService
{
    @Autowired
    private TaskMapper taskMapper;

    @Autowired
    private IVendingMachineService vendingMachineService;

    @Autowired
    private IEmpService empService;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private ITaskDetailsService taskDetailsService;
    /**
     * 查询工单
     *
     * @param taskId 工单主键
     * @return 工单
     */
    @Override
    public Task selectTaskByTaskId(Long taskId)
    {
        return taskMapper.selectTaskByTaskId(taskId);
    }

    /**
     * 查询工单列表
     *
     * @param task 工单
     * @return 工单
     */
    @Override
    public List<Task> selectTaskList(Task task)
    {
        return taskMapper.selectTaskList(task);
    }

    /**
     * 新增工单
     *
     * @param task 工单
     * @return 结果
     */
    @Override
    public int insertTask(Task task)
    {
        task.setCreateTime(DateUtils.getNowDate());
        return taskMapper.insertTask(task);
    }

    /**
     * 修改工单
     *
     * @param task 工单
     * @return 结果
     */
    @Override
    public int updateTask(Task task)
    {
        task.setUpdateTime(DateUtils.getNowDate());
        return taskMapper.updateTask(task);
    }

    /**
     * 批量删除工单
     *
     * @param taskIds 需要删除的工单主键
     * @return 结果
     */
    @Override
    public int deleteTaskByTaskIds(Long[] taskIds)
    {
        return taskMapper.deleteTaskByTaskIds(taskIds);
    }

    /**
     * 删除工单信息
     *
     * @param taskId 工单主键
     * @return 结果
     */
    @Override
    public int deleteTaskByTaskId(Long taskId)
    {
        return taskMapper.deleteTaskByTaskId(taskId);
    }

    /**
     * 查询运维工单列表
     * @param task
     * @return
     */
    public List<TaskVo> selectTaskVoList(Task task){
        return taskMapper.selectTaskVoList(task);
    }

    @Override
    @Transactional
    public int insertTaskDto(TaskDto taskDto) {
        // 查询售货机是否存在
        VendingMachine vendingMachine = vendingMachineService.selectVendingMachineByInnerCode(taskDto.getInnerCode());
        if (vendingMachine == null)
            throw new SecurityException("设备不存在");
        // 校验售货机状态是否与工单类型相符
        checkCreateTask(vendingMachine.getVmStatus(), taskDto.getProductTypeId());
        // 检查设备是否有未完成同类型工单
        hasTask(taskDto);
        // 查询并校验员工是否存在
        Emp emp = empService.selectEmpById(taskDto.getUserId());
        if (emp == null)
            throw new SecurityException("员工不存在！");
        // 校验员工区域是否匹配
        if (!emp.getRegionId().equals(vendingMachine.getRegionId()))
            throw new SecurityException("员工区域与设备区域不一致，无法创建工单");
        // 将dto转化为po，保存工单
        Task task = BeanUtil.copyProperties(taskDto, Task.class); // 拷贝属性
        task.setTaskStatus(DkdContants.TASK_STATUS_CREATE); // 工单状态设置为创建
        task.setUserName(emp.getUserName()); // 执行人名称
        task.setRegionId(vendingMachine.getRegionId()); // 所属区域id
        task.setAddr(vendingMachine.getAddr()); // 地址
        task.setCreateTime(DateUtils.getNowDate()); // 创建时间
        task.setTaskCode(generateTaskCode()); // 工单编号
        int taskResult = taskMapper.insertTask(task);
        // 判断是否为补货工单
        if (taskDto.getProductTypeId().equals(DkdContants.TASK_TYPE_SUPPLY)) {
            // 保存工单详情
            List<TaskDetailDto> details = taskDto.getDetails();
            if (CollUtil.isEmpty(details)) {
                throw new SecurityException("补货工单详情为空");
            }
            // 将dto转为po补充属性
            List<TaskDetails> taskDetailsList = details.stream().map(dto -> {
                TaskDetails taskDetails = BeanUtil.copyProperties(dto, TaskDetails.class);
                taskDetails.setTaskId(task.getTaskId());
                return taskDetails;
            }).collect(Collectors.toList());
            // 批量新增
            taskDetailsService.batchInsertTaskDetails(taskDetailsList);
        }
        return taskResult;
    }

    /**
     * 取消工单
     * @param task
     * @return
     */
    @Override
    public int cancelTask(Task task) {
        // 判断工单是否可以取消
        Task taskDb = taskMapper.selectTaskByTaskId(task.getTaskId());
        // 判断工单状态是否是已取消
        if (Objects.equals(taskDb.getTaskStatus(), DkdContants.TASK_STATUS_CANCEL))
            throw new SecurityException("工单已经是取消的了");
        // 判断工单状态是否是已完成
        if (Objects.equals(taskDb.getTaskStatus(), DkdContants.TASK_STATUS_FINISH))
            throw new SecurityException("已完成的工单不允许取消");
        // 更新字段
        task.setTaskStatus(DkdContants.TASK_STATUS_CANCEL);
        task.setUpdateTime(DateUtils.getNowDate());
        // 更新工单状态为取消
        return taskMapper.updateTask(task);
    }

    /**
     * 生成并获取当天的员工编号
     */
    private String generateTaskCode(){
        // 获取当前日期并格式化为"yyyyMMdd"
        String dateStr = DateUtils.dateTimeNow("yyyyMMdd");
        // 根据日期生成redis的键
        String key = "dkd.task.code." + dateStr;
        // 判断key是否存在
        if (!redisTemplate.hasKey(key)) {
            // 如果key不存在，设置初始值为1，并设置过期时间为1天
            redisTemplate.opsForValue().set(key, 1, Duration.ofDays(1));
            // 返回工单编号
            return dateStr + "0001";
        }
        // 如果key存在，则获取当前值，并自增1
        return dateStr + StrUtil.padPre(redisTemplate.opsForValue().increment(key).toString(), 4, '0');
    }

    /**
     * 检查设备是否有未完成同类型工单
     * @param taskDto
     */

    private void hasTask(TaskDto taskDto) {
        // 创建Task条件对象，并设置设备编号和工单类型，以及工单状态为进行中
        Task taskParam = new Task();
        taskParam.setInnerCode(taskDto.getInnerCode());
        taskParam.setProductTypeId(taskDto.getProductTypeId());
        taskParam.setTaskStatus(DkdContants.TASK_STATUS_PROGRESS);
        // 调用taskMapper查询数据库是否有符合条件的工单列表
        List<Task> taskList = taskMapper.selectTaskList(taskParam);
        if (taskList.size() > 0)
            throw new SecurityException("设备存在未完成工单，无法创建新的工单");
    }

    // 校验售货机状态是否与工单类型相符
    private void checkCreateTask(Long vmStatus, Long productTypeId){
        // 如果是投放工单，设备在运行中，抛出异常
        if (productTypeId == DkdContants.TASK_TYPE_DEPLOY && vmStatus == DkdContants.VM_STATUS_RUNNING) {
            throw new SecurityException("设备正在运行中，无法进行投放操作");
        }
        // 如果是维修工单，设备不在运行中，抛出异常
        if (productTypeId == DkdContants.TASK_TYPE_REPAIR && vmStatus != DkdContants.VM_STATUS_RUNNING) {
            throw new SecurityException("设备不在运行中，无法进行维修操作");
        }
        // 如果是补货工单，设备不在运行中，抛出异常
        if (productTypeId == DkdContants.TASK_TYPE_SUPPLY && vmStatus != DkdContants.VM_STATUS_RUNNING) {
            throw new SecurityException("设备不在运行中，无法进行补货操作");
        }
        // 如果是撤机工单，设备不在运行中，抛出异常
        if (productTypeId == DkdContants.TASK_TYPE_REVOKE && vmStatus != DkdContants.VM_STATUS_RUNNING) {
            throw new SecurityException("设备不在运行中，无法进行撤机操作");
        }
    }
}
