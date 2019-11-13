package com.elasticsearch;

import org.elasticsearch.common.settings.Setting;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.analysis.PreBuiltAnalyzerProviderFactory;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.junit.Test;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * description:TODO
 *
 * @author weishi.zeng
 * @version 1.0
 * @date 2019/11/13 16:12
 */
public class ElasticSearchClientTest {
    /**
     * description: 创建索引
     * @author weishi.zeng
     * @date 2019/11/13 17:53
     * @param []
     * @return void
     */
    @Test
    public void indexTest() throws UnknownHostException {
        //创建settings对象，相当于配置信息配置集群的名称
        Settings settings = Settings.builder().put("cluster.name", "my-elasticsearch").build();
        //创建client连接对象
        PreBuiltTransportClient client = new PreBuiltTransportClient(settings);
        client.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("127.0.0.1"),9301));
        client.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("127.0.0.1"),9302));
        client.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("127.0.0.1"),9303));
        //创建索引
        client.admin().indices().prepareCreate("index_hello")
                //执行操作
                .get();
        //释放资源
        client.close();
    }

    /**
     * description: 创建mapping
     * @author weishi.zeng
     * @date 2019/11/13 17:53
     * @param
     * @return
     */
    @Test
    public void mappingTest() throws IOException {
        //创建settings对象，相当于配置信息配置集群的名称
        Settings settings = Settings.builder().put("cluster.name", "my-elasticsearch").build();
        //创建client连接对象
        PreBuiltTransportClient client = new PreBuiltTransportClient(settings);
        client.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("127.0.0.1"),9301));
        client.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("127.0.0.1"),9302));
        client.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("127.0.0.1"),9303));
        //创建一个Mapping信息，应该是一个Json数据，也可以是字符串，也可以是XContextBuilder对象
        XContentBuilder xContentBuilder = XContentFactory.jsonBuilder()
                .startObject()
                    .startObject("article")
                        .startObject("properties")
                             .startObject("id")
                                .field("type", "long")
                                .field("store", true)
                             .endObject()
                             .startObject("title")
                                .field("type", "text")
                                .field("store", true)
                                .field("analyzer", "ik_smart")
                             .endObject()
                             .startObject("content")
                                .field("type", "text")
                                .field("store", true)
                                .field("analyzer", "ik_smart")
                             .endObject()
                         .endObject()
                    .endObject()
                .endObject();
        //使用client向es服务器发送mapping信息
        client.admin().indices()
                //设置要做映射的索引
                .preparePutMapping("index_hello")
                //设置要做映射的type
                .setType("article")
                //mapping信息，可以是XContentBuilder对象可以是json字符串
                .setSource(xContentBuilder)
                //执行操作
                .get();
        //关闭client
        client.close();
    }

}
