package cn.afj.es2csv.config;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author afj
 * @Date 2020/10/10 10:15
 * @Version 1.0
 * @description:
 */


@Configuration
public class EsConfig {


    private static final String ip = "127.0.0.1";


    private static final int port = 9200;

    @Bean
    public RestClient lowLevelClient() {
        return RestClient.builder(new HttpHost(ip, port, "http"))
                .setRequestConfigCallback(builder -> builder.setConnectTimeout(10000).setSocketTimeout(50000)).build();
    }

    @Bean
    public RestHighLevelClient restHighLevelClient() {
        return new RestHighLevelClient(this.lowLevelClient());
    }

}