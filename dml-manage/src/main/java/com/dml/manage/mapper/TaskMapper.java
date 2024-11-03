package com.dml.manage.mapper;

import java.util.List;

import com.dml.manage.domain.Task;
import com.dml.manage.domain.vo.TaskStatisticsVO;
import com.dml.manage.domain.vo.TaskVo;

/**
 * 工单Mapper接口
 *
 * @author opao
 * @date 2024-10-08
 */
public interface TaskMapper
{
    /**
     * 查询工单
     *
     * @param taskId 工单主键
     * @return 工单
     */
    public Task selectTaskByTaskId(Long taskId);

    /**
     * 查询工单列表
     *
     * @param task 工单
     * @return 工单集合
     */
    public List<Task> selectTaskList(Task task);

    /**
     * 新增工单
     *
     * @param task 工单
     * @return 结果
     */
    public int insertTask(Task task);

    /**
     * 修改工单
     *
     * @param task 工单
     * @return 结果
     */
    public int updateTask(Task task);

    /**
     * 删除工单
     *
     * @param taskId 工单主键
     * @return 结果
     */
    public int deleteTaskByTaskId(Long taskId);

    /**
     * 批量删除工单
     *
     * @param taskIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTaskByTaskIds(Long[] taskIds);

    /**
     * 查询运维工单列表
     * @param task
     * @return TaskVo集合
     */
    public List<TaskVo> selectTaskVoList(Task task);

    /**
     * 获取当月工单统计详情
     * @return
     */
    TaskStatisticsVO getThisMonthTaskData(TaskStatisticsVO taskStatisticsVO);
}
