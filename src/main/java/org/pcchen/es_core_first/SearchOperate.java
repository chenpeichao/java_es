package org.pcchen.es_core_first;

import com.alibaba.fastjson.JSONObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import java.net.InetSocketAddress;

/**
 * ??????
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
        // ???????
        searchOperate.preCreateDate(client, new Employee("1", "jack", 27, "technique software", "china", "2017-01-01", 10000l));
        searchOperate.preCreateDate(client, new Employee("2", "marry", 35, "technique manager", "china", "2017-01-01", 12000l));
        searchOperate.preCreateDate(client, new Employee("3", "tom", 32, "senior technique software", "china", "2016-01-01", 11000l));
        searchOperate.preCreateDate(client, new Employee("4", "jen", 25, "junior finance", "usa", "2016-01-01", 7000l));
        searchOperate.preCreateDate(client, new Employee("5", "mike", 37, "finance manager", "usa", "2015-01-01", 15000l));

        // ??????????????????
//        searchOperate.searchEmployee(client);
        client.close();
    }

    /**
     * ?????????????????
     * @param client
     */
    private void searchEmployee(TransportClient client) {
        try {
            SearchHits company = client.prepareSearch("company")
                    .setQuery(QueryBuilders.wildcardQuery("position", "*t*"))
                    .setQuery(QueryBuilders.rangeQuery("age").from(35, true).to(45, true))
                    .setFrom(0)
                    .setSize(1000)
                    .get()
                    .getHits();
            System.out.println("????????????" + company.getTotalHits());

            SearchHit[] companyHits = company.getHits();
            for (SearchHit searchHitFields : companyHits) {
                System.out.print("?¡Â????" + searchHitFields.getScore());
                System.out.println("===" + searchHitFields.getSourceAsString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * ????????
     *
     * @param client        es???????
     * @param employee      ??????????
     */
    private void preCreateDate(TransportClient client, Employee employee) {
        System.out.println(JSONObject.toJSONString(employee));
        //????????????jsonObject?????????jsonArry
        IndexResponse response = client.prepareIndex("company", "employee").setId(employee.getId())
                .setSource(JSONObject.toJSONString(employee)).get();

        logger.info("????????????????" + response.getResult());
    }
}
