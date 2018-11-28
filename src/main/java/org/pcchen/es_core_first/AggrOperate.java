package org.pcchen.es_core_first;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramInterval;
import org.elasticsearch.search.aggregations.bucket.histogram.Histogram;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.metrics.avg.Avg;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import java.net.InetSocketAddress;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 聚合相关测试
 *
 * @author cpc
 * @create 2018-11-28 10:45
 **/
public class AggrOperate {
    private static Logger logger = LogManager.getLogger(AggrOperate.class);

    public static void main(String[] args) {
        Settings settings = Settings.builder().put("cluster.name", "elasticsearch").build();

        TransportClient transportClient = new PreBuiltTransportClient(settings)
                .addTransportAddress(new InetSocketTransportAddress(new InetSocketAddress("localhost", 9300)));

        AggrOperate aggrOperate = new AggrOperate();
        aggrOperate.doAggrAccess(transportClient);

        System.out.println("---------------------------------------");

        aggrOperate.doAggrAccessTeacher(transportClient);

        transportClient.close();
    }

    /**
     * 聚合操作示例---myself
     *
     * @param transportClient
     */
    private void doAggrAccess(TransportClient transportClient) {
        SearchResponse searchResponse = transportClient.prepareSearch("company")
                .addAggregation(AggregationBuilders.terms("group_by_country").field("country").subAggregation(
                        AggregationBuilders
                                .dateHistogram("group_by_join_date")
                                .field("joinDate")
                                .dateHistogramInterval(DateHistogramInterval.YEAR)
                                .subAggregation(AggregationBuilders.avg("avg_salary").field("salary"))
                )).get();

        Terms terms = searchResponse.getAggregations().get("group_by_country");
        for (Terms.Bucket bucket : terms.getBuckets()) {
            logger.info(bucket.getKeyAsString() + "====" + bucket.getDocCount());

            Histogram terms2 = bucket.getAggregations().get("group_by_join_date");
            for (Histogram.Bucket bucket2 : terms2.getBuckets()) {
                logger.info("   " + bucket2.getKeyAsString() + "====" + bucket2.getDocCount());

                Avg avg = bucket2.getAggregations().get("avg_salary");
                logger.info("      " + avg.getName() + "--->" + avg.getValueAsString());
            }
        }

        Map<String, Aggregation> hits = searchResponse.getAggregations().asMap();
        System.out.println(hits);
    }

    /**
     * 聚合操作示例---teacher
     *
     * @param client
     */
    private void doAggrAccessTeacher(TransportClient client) {
        SearchResponse searchResponse = client.prepareSearch("company").addAggregation(
                AggregationBuilders.terms("group_by_country").field("country").subAggregation(
                        AggregationBuilders.dateHistogram("group_by_join_date")
                                .field("joinDate")
                                .dateHistogramInterval(DateHistogramInterval.YEAR)
                                .subAggregation(
                                        AggregationBuilders.avg("avg_salary").field("salary")
                                )
                )
        ).execute().actionGet();

        Map<String, Aggregation> aggrMap = searchResponse.getAggregations().asMap();
        Terms aggregation = (Terms) aggrMap.get("group_by_country");
        List<Terms.Bucket> buckets = aggregation.getBuckets();
        for (Terms.Bucket countryAggrBucket : aggregation.getBuckets()) {
            logger.info(countryAggrBucket.getKeyAsString() + "--->" + countryAggrBucket.getDocCount());

//            Bucket groupByCountryBucket = groupByCountryBucketIterator.next();
//            System.out.println(groupByCountryBucket.getKey() + ":" + groupByCountryBucket.getDocCount());

            Histogram groupByJoinDate = (Histogram) countryAggrBucket.getAggregations().asMap().get("group_by_join_date");
            Iterator<Histogram.Bucket> groupByJoinDateBucketIterator = groupByJoinDate.getBuckets().iterator();
            while (groupByJoinDateBucketIterator.hasNext()) {
                org.elasticsearch.search.aggregations.bucket.histogram.Histogram.Bucket groupByJoinDateBucket = groupByJoinDateBucketIterator.next();
                System.out.println("  " + groupByJoinDateBucket.getKey() + "--->" + groupByJoinDateBucket.getDocCount());

                Avg avg = (Avg) groupByJoinDateBucket.getAggregations().asMap().get("avg_salary");
                System.out.println("    " + avg.getName() + "--->" + avg.getValue());
            }
        }
    }
}
