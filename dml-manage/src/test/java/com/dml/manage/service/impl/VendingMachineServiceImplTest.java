package com.dml.manage.service.impl;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.jupiter.api.Test;

import java.io.IOException;

class VendingMachineServiceImplTest {

    @Test
    public void getCoordinatesFromBaidu() throws IOException {
        String address = "北京市朝阳区国家体育场南路1号";
        String AK = "X81b7YWJ1OqClIAO7Nowbl2fe4soK6RL";
        /**
         * 地理编码 URL
         */
         String ADDRESS_TO_LONGITUDEA_URL = "http://api.map.baidu.com/geocoding/v3/?output=json&location=showLocation";
        String url = ADDRESS_TO_LONGITUDEA_URL + "&ak=" + AK + "&address="+ address;
        HttpClient client = HttpClients.createDefault(); // 创建默认http连接
        HttpPost post = new HttpPost(url);// 创建一个post请求
        try {
            HttpResponse response = client.execute(post);// 用http连接去执行get请求并且获得http响应
            HttpEntity entity = response.getEntity();// 从response中取到响实体
            String html = EntityUtils.toString(entity);// 把响应实体转成文本
            // JSON转对象
            JSONObject jsonObject = JSON.parseObject(html);
            System.out.println("jsonObject = " + jsonObject);
        } catch (Exception e) {
            System.out.println("e = " + e);
        }

    }

}
