package org.pcchen.es_core_first;

import com.alibaba.fastjson.JSONObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

/**
 * elasticsearch的连接以及增删改查操作
 *
 * @author cpc
 * @create 2018-11-26 15:15
 **/
public class CRUDOperate {
    private static Logger logger = LogManager.getLogger(CRUDOperate.class);

    public static void main(String[] args) {
        Settings settings = Settings.builder()
                .put("cluster.name", "elasticsearch")
                .build();

        TransportClient client = new PreBuiltTransportClient(settings)
                .addTransportAddress(new InetSocketTransportAddress(new InetSocketAddress("localhost", 9300)));
        logger.info("client信息为:" + client);

        CRUDOperate crudOperate = new CRUDOperate();
        crudOperate.getEmployee(client);        // 获取指定id的员工信息
        crudOperate.updateEmployee(client);        // 修改指定id的员工信息
        crudOperate.deleteEmployee(client);        // 删除指定id的员工信息

        client.close();
    }

    /**
     * 创建索引并且向索引添加一条数据
     *
     * @param client
     * @throws IOException
     * @throws ParseException
     */
    private void createEmployee(TransportClient client) throws IOException, ParseException {
        Employee employee = new Employee();
        employee.setName("rose");
        employee.setAge(25);
        employee.setCountry("usa");
        employee.setJoinDate("2018-10-11");
        employee.setPosition("flower");
        employee.setSalary(12000L);

        logger.info(JSONObject.toJSONString(employee));
        IndexResponse response = client.prepareIndex("company", "employee").setId("1")
                .setSource(JSONObject.toJSONString(employee))
                .get();
        logger.info("创建员工成功返回信息返回为：" + response.getResult());
    }

    /**
     * 根据id获取员工信息
     *
     * @param client
     */
    private void getEmployee(TransportClient client) {
        GetResponse response = client.prepareGet("company", "employee", "2").get();
        logger.info("查询指定id下的员工信息返回为：" + response.getSourceAsString());
    }

    /**
     * 根据指定id修改索引数据
     *
     * @param client
     */
    private void updateEmployee(TransportClient client) {
        //第一种修改
//        UpdateResponse response = client.prepareUpdate("company", "employee", "2").setDoc("name", "jack").get();
        //第二种修改
        Map<String, String> tmpMap = new HashMap<String, String>();
        tmpMap.put("name", "jackabcefg");
        UpdateResponse response = client.prepareUpdate("company", "employee", "2")
                .setDoc(JSONObject.toJSONString(tmpMap))
//                .setDoc(tmpMap)       //也可以
                .get();
        logger.info("修改指定id下的员工信息返回为：" + response.getResult());
    }

    /**
     * 删除指定id的员工
     *
     * @param client
     */
    private void deleteEmployee(TransportClient client) {
        DeleteResponse deleteResponse = client.prepareDelete("company", "employee", "1").get();
        logger.info("删除指定id下的员工信息返回为：" + deleteResponse.getResult());
    }

    //---------------------------------------------teacher code------------------------------------------------------------------------------

    /**
     * 老师笔记
     * 创建员工信息（创建一个document）
     *
     * @param client
     */
    private static void createEmployeeTeacher(TransportClient client) throws Exception {
        IndexResponse response = client.prepareIndex("company", "employee", "1")
                .setSource(XContentFactory.jsonBuilder()
                        .startObject()
                        .field("name", "jack")
                        .field("age", 27)
                        .field("position", "technique")
                        .field("country", "china")
                        .field("join_date", "2017-01-01")
                        .field("salary", 10000)
                        .endObject())
                .get();
        System.out.println(response.getResult());
    }
}
