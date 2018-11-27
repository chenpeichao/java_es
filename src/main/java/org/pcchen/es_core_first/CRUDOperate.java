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
 * elasticsearch�������Լ���ɾ�Ĳ����
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
        logger.info("client��ϢΪ:" + client);

        CRUDOperate crudOperate = new CRUDOperate();
        crudOperate.getEmployee(client);        // ��ȡָ��id��Ա����Ϣ
        crudOperate.updateEmployee(client);        // �޸�ָ��id��Ա����Ϣ
        crudOperate.deleteEmployee(client);        // ɾ��ָ��id��Ա����Ϣ

        client.close();
    }

    /**
     * ���������������������һ������
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
        logger.info("����Ա���ɹ�������Ϣ����Ϊ��" + response.getResult());
    }

    /**
     * ����id��ȡԱ����Ϣ
     *
     * @param client
     */
    private void getEmployee(TransportClient client) {
        GetResponse response = client.prepareGet("company", "employee", "2").get();
        logger.info("��ѯָ��id�µ�Ա����Ϣ����Ϊ��" + response.getSourceAsString());
    }

    /**
     * ����ָ��id�޸���������
     *
     * @param client
     */
    private void updateEmployee(TransportClient client) {
        //��һ���޸�
//        UpdateResponse response = client.prepareUpdate("company", "employee", "2").setDoc("name", "jack").get();
        //�ڶ����޸�
        Map<String, String> tmpMap = new HashMap<String, String>();
        tmpMap.put("name", "jackabcefg");
        UpdateResponse response = client.prepareUpdate("company", "employee", "2")
                .setDoc(JSONObject.toJSONString(tmpMap))
//                .setDoc(tmpMap)       //Ҳ����
                .get();
        logger.info("�޸�ָ��id�µ�Ա����Ϣ����Ϊ��" + response.getResult());
    }

    /**
     * ɾ��ָ��id��Ա��
     *
     * @param client
     */
    private void deleteEmployee(TransportClient client) {
        DeleteResponse deleteResponse = client.prepareDelete("company", "employee", "1").get();
        logger.info("ɾ��ָ��id�µ�Ա����Ϣ����Ϊ��" + deleteResponse.getResult());
    }

    //---------------------------------------------teacher code------------------------------------------------------------------------------

    /**
     * ��ʦ�ʼ�
     * ����Ա����Ϣ������һ��document��
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
