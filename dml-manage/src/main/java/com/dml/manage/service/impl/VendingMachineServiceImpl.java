package com.dml.manage.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.dml.common.constant.DelContents;
import com.dml.common.utils.DateUtils;
import com.dml.common.utils.uuid.UUIDUtils;
import com.dml.manage.domain.Channel;
import com.dml.manage.domain.Node;
import com.dml.manage.domain.VmType;
import com.dml.manage.domain.vo.FaultyVendingMachineVo;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.dml.manage.mapper.VendingMachineMapper;
import com.dml.manage.domain.VendingMachine;
import com.dml.manage.service.IVendingMachineService;
import org.springframework.transaction.annotation.Transactional;

/**
 * 设备管理Service业务层处理
 *
 * @author op
 * @date 2024-09-27
 */
@Service
public class VendingMachineServiceImpl implements IVendingMachineService
{
    @Autowired
    private VendingMachineMapper vendingMachineMapper;

    @Autowired
    private VmTypeServiceImpl vmTypeService;

    @Autowired
    private NodeServiceImpl nodeService;

    @Autowired
    private ChannelServiceImpl channelService;

    /**
     * 查询设备管理
     *
     * @param id 设备管理主键
     * @return 设备管理
     */
    @Override
    public VendingMachine selectVendingMachineById(Long id)
    {
        return vendingMachineMapper.selectVendingMachineById(id);
    }

    /**
     * 查询设备管理列表
     *
     * @param vendingMachine 设备管理
     * @return 设备管理
     */
    @Override
    public List<VendingMachine> selectVendingMachineList(VendingMachine vendingMachine)
    {
        return vendingMachineMapper.selectVendingMachineList(vendingMachine);
    }

    /**
     * 新增设备管理
     *
     * @param vendingMachine 设备管理
     * @return 结果
     */
    @Override
    @Transactional
    public int insertVendingMachine(VendingMachine vendingMachine) throws IOException {
        // 1.新增设备
        // 1-1.生成内部编号
        String uuid = UUIDUtils.getUUID();
        vendingMachine.setInnerCode(uuid); // 售货机编号
        // 1-2 查询售货机类型表，补充设备容量
        VmType vmType = vmTypeService.selectVmTypeById(vendingMachine.getVmTypeId());
        vendingMachine.setChannelMaxCapacity(vmType.getChannelMaxCapacity());
        // 1-3-1 查询点位表，补充 区域、点位地址、合作商等信息
        Node node = nodeService.selectNodeById(vendingMachine.getNodeId());
        BeanUtil.copyProperties(node, vendingMachine);
        vendingMachine.setAddr(node.getAddress());
        // 1-3-2补全对应地址的经纬度
        Map<String, Double> coordinatesFromBaidu = getCoordinatesFromBaidu(vendingMachine.getAddr());
        vendingMachine.setLongitudes(coordinatesFromBaidu.get("lng"));
        vendingMachine.setLatitude(coordinatesFromBaidu.get("lat"));
        // 1-4 设备状态
        vendingMachine.setVmStatus(DelContents.VM_STATUS_NODEPLOY);
        vendingMachine.setCreateTime(DateUtils.getNowDate());
        vendingMachine.setUpdateTime(DateUtils.getNowDate());
        // 1-5 保存
        int result = vendingMachineMapper.insertVendingMachine(vendingMachine);
        // 2.新增货道
        // 2-1 声明货道集合
        ArrayList<Channel> channelList = new ArrayList<>();
        for (int i = 0; i < vmType.getVmRow(); i++) {
            for (int j = 0; j < vmType.getVmCol(); j++) {
                // 2-3 封装channel
                Channel channel = new Channel();
                channel.setChannelCode(i + "-" + j); // 货道编号
                channel.setVmId(vendingMachine.getId()); // 售货机id
                channel.setInnerCode(vendingMachine.getInnerCode()); // 售货机软编号
                channel.setMaxCapacity(vmType.getChannelMaxCapacity()); // 售货机最大容量
                channel.setCreateTime(DateUtils.getNowDate());
                channel.setUpdateTime(DateUtils.getNowDate());
                channelList.add(channel);
            }
        }
        // 2-4 批量新增
        result += channelService.batchInsertChannel(channelList);
        return result;
    }

    /**
     * 修改设备管理
     *
     * @param vendingMachine 设备管理
     * @return 结果
     */
    @Override
    public int updateVendingMachine(VendingMachine vendingMachine)
    {
        if (vendingMachine.getNodeId() != null) {
            //查询点位表，补充 区域、点位、合作商等信息
            Node node = nodeService.selectNodeById(vendingMachine.getNodeId());
            BeanUtil.copyProperties(node, vendingMachine, "id");// 商圈类型、区域、合作商
            vendingMachine.setAddr(node.getAddress());// 设备地址
        }
        vendingMachine.setUpdateTime(DateUtils.getNowDate());// 更新时间
        return vendingMachineMapper.updateVendingMachine(vendingMachine);
    }

    /**
     * 批量删除设备管理
     *
     * @param ids 需要删除的设备管理主键
     * @return 结果
     */
    @Override
    @Transactional
    public int deleteVendingMachineByIds(Long[] ids)
    {
        int result;
        result = channelService.deleteChannelByVmIds(ids);
        result += vendingMachineMapper.deleteVendingMachineByIds(ids);
        return result;
    }

    /**
     * 删除设备管理信息
     *
     * @param id 设备管理主键
     * @return 结果
     */
    @Override
    @Transactional
    public int deleteVendingMachineById(Long id)
    {
        int result;

        result = channelService.deleteChannelByVmId(id);
        result += vendingMachineMapper.deleteVendingMachineById(id);
        return result;
    }

    /**
     * 根据 innerCode（设备编号） 查询设备
     * @param innerCode
     * @return VendingMachine
     */
    @Override
    public VendingMachine selectVendingMachineByInnerCode(String innerCode){
        return vendingMachineMapper.selectVendingMachineByInnerCode(innerCode);
    }

    /**
     * 查询故障的机器
     * @return
     */
    @Override
    public List<FaultyVendingMachineVo> getFaultyVendingMachineList() {
        // 获取故障值
        Long vmStatus = DelContents.VM_STATUS_Faulty;
        return vendingMachineMapper.getFaultyVendingMachineList(vmStatus);
    }

    /**
     * 根据地址查询对应的经纬度
     * @throws IOException
     */
    private Map<String, Double>  getCoordinatesFromBaidu(String address) throws IOException {
        /**
         * 地理编码 URL
         */
        String ADDRESS_TO_LONGITUDEA_URL = "http://api.map.baidu.com/geocoding/v3/?output=json&location=showLocation";
        String url = ADDRESS_TO_LONGITUDEA_URL + "&ak=" + DelContents.BAIDU_AK + "&address="+ address;
        HttpClient client = HttpClients.createDefault(); // 创建默认http连接
        HttpPost post = new HttpPost(url);// 创建一个post请求
        try {
            HttpResponse response = client.execute(post);// 用http连接去执行get请求并且获得http响应
            HttpEntity entity = response.getEntity();// 从response中取到响实体
            String html = EntityUtils.toString(entity);// 把响应实体转成文本
            // JSON转对象
            JSONObject jsonObject = JSON.parseObject(html);
            if (jsonObject != null && "0".equals(jsonObject.getString("status"))) {
                JSONObject result = jsonObject.getJSONObject("result");
                if (result != null) {
                    JSONObject location = result.getJSONObject("location");
                    if (location != null) {
                        double lng = location.getDoubleValue("lng");
                        double lat = location.getDoubleValue("lat");
                        Map<String, Double> coordinates = new HashMap<>();
                        coordinates.put("lng", lng);
                        coordinates.put("lat", lat);
                        return coordinates;
                    }
                }
            } else {
                throw new IOException("获取经纬度失败: " + jsonObject.getString("message"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
