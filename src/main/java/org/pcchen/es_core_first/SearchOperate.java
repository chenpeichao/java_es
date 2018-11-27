package org.pcchen.es_core_first;

import com.alibaba.fastjson.JSONObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import java.net.InetSocketAddress;

/**
 * 查询练习
 *
 * @author cpc
 * @create 2018-11-27 17:03
 **/
public class SearchOperate {
    private static Logger logger = LogManager.getLogger(SearchOperate.class);

    public static void main(String[] args) {
        Settings settings = Settings.builder().put("cluster.name", "elasticsearch").build();
        TransportClient client = new PreBuiltTransportClient(settings)
                .addTransportAddress(new InetSocketTransportAddress(new InetSocketAddress("localhost", 9300)));

        SearchOperate searchOperate = new SearchOperate();
        // 准备数据
        Employee employee1 = new Employee("jack", 27, "technique software", "china", "2017-01-01", 10000l);
        Employee employee2 = new Employee("jack", 27, "technique software", "china", "2017-01-01", 10000l);
        Employee employee3 = new Employee("jack", 27, "technique software", "china", "2017-01-01", 10000l);

        // 导入数据
//        searchOperate.preCreateDate(client, employee1, employee2, employee3);
        searchOperate.preCreateDate(client, employee1);

        client.close();
    }

    /**
     * 构造数据
     *
     * @param client
     * @param employee
     */
    private void preCreateDate(TransportClient client, Employee employee) {
        System.out.println(JSONObject.toJSONString(employee));
        //一次只能插入一个jsonObject，不能插入jsonArry
        IndexResponse response = client.prepareIndex("company", "employee").setId("1")
                .setSource(JSONObject.toJSONString(employee)).get();

        logger.info("插入数据返回结果为：" + response.getResult());
    }
}
