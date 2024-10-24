package com.dkd.manage.service;

import java.util.List;
import com.dkd.manage.domain.Node;
import com.dkd.manage.domain.vo.NodeVo;
import com.dkd.manage.domain.vo.PartnerVo;

/**
 * 点位管理Service接口
 *
 * @author op
 * @date 2024-09-23
 */
public interface INodeService
{
    /**
     * 查询点位管理
     *
     * @param id 点位管理主键
     * @return 点位管理
     */
    public Node selectNodeById(Long id);

    /**
     * 查询点位管理列表
     *
     * @param node 点位管理
     * @return 点位管理集合
     */
    public List<NodeVo> selectNodeVoList(Node node);

    /**
     * 新增点位管理
     *
     * @param node 点位管理
     * @return 结果
     */
    public int insertNode(Node node);

    /**
     * 修改点位管理
     *
     * @param node 点位管理
     * @return 结果
     */
    public int updateNode(Node node);

    /**
     * 批量删除点位管理
     *
     * @param ids 需要删除的点位管理主键集合
     * @return 结果
     */
    public int deleteNodeByIds(Long[] ids);

    /**
     * 删除点位管理信息
     *
     * @param id 点位管理主键
     * @return 结果
     */
    public int deleteNodeById(Long id);

    /**
     * 统计点位数前五的合作商
     */
    public List<PartnerVo> countNodeTop5();
}
