package com.dml.manage.service.impl;

import java.util.List;
import com.dml.common.utils.DateUtils;
import com.dml.manage.domain.vo.NodeVo;
import com.dml.manage.domain.vo.PartnerVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.dml.manage.mapper.NodeMapper;
import com.dml.manage.domain.Node;
import com.dml.manage.service.INodeService;

/**
 * 点位管理Service业务层处理
 *
 * @author op
 * @date 2024-09-23
 */
@Service
public class NodeServiceImpl implements INodeService
{
    @Autowired
    private NodeMapper nodeMapper;

    /**
     * 查询点位管理
     *
     * @param id 点位管理主键
     * @return 点位管理
     */
    @Override
    public Node selectNodeById(Long id)
    {
        return nodeMapper.selectNodeById(id);
    }

    /**
     * 查询点位管理列表
     *
     * @param node 点位管理
     * @return 点位管理
     */
    @Override
    public List<NodeVo> selectNodeVoList(Node node)
    {
        return nodeMapper.selectNodeVoList(node);
    }

    /**
     * 新增点位管理
     *
     * @param node 点位管理
     * @return 结果
     */
    @Override
    public int insertNode(Node node)
    {
        node.setCreateTime(DateUtils.getNowDate());
        return nodeMapper.insertNode(node);
    }

    /**
     * 修改点位管理
     *
     * @param node 点位管理
     * @return 结果
     */
    @Override
    public int updateNode(Node node)
    {
        node.setUpdateTime(DateUtils.getNowDate());
        return nodeMapper.updateNode(node);
    }

    /**
     * 批量删除点位管理
     *
     * @param ids 需要删除的点位管理主键
     * @return 结果
     */
    @Override
    public int deleteNodeByIds(Long[] ids)
    {
        return nodeMapper.deleteNodeByIds(ids);
    }

    /**
     * 删除点位管理信息
     *
     * @param id 点位管理主键
     * @return 结果
     */
    @Override
    public int deleteNodeById(Long id)
    {
        return nodeMapper.deleteNodeById(id);
    }

    /**
     * 统计点位数前五的合作商
     */
    @Override
    public List<PartnerVo> countNodeTop5() {
        return nodeMapper.countNodeTop5();
    }
}
