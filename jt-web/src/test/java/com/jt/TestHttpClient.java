package com.jt;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.Test;
import org.springframework.context.annotation.Bean;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class TestHttpClient {

    @Test
    public void doGet() throws IOException {
        String url = "http://www.baidu.com";
        CloseableHttpClient client = HttpClients.createDefault();
        HttpGet get = new HttpGet(url);
        CloseableHttpResponse response = client.execute(get);
        if (200 == response.getStatusLine().getStatusCode()){
            System.out.println("请求调用成功!");
            String result = EntityUtils.toString(response.getEntity(),"UTF-8");
            System.out.println(result);
        }
    }

    private String nodes = "192.168.19.139:7000,192.168.19.139:7001,192.168.19.139:7002,192.168.19.139:7003,192.168.19.139:7004,192.168.19.139:7005";

    public JedisCluster jedisCluster(){
        Set<HostAndPort> nodes = getSet();
        JedisCluster jedisCluster = new JedisCluster(nodes);
        return jedisCluster;
    }

    private Set<HostAndPort> getSet() {
        Set<HostAndPort> set = new HashSet<>();
        String[] nodeArray = nodes.split(",");
        for (String node : nodeArray){
            String ip = node.split(":")[0];
            Integer port = Integer.parseInt(node.split(":")[1]);
            HostAndPort hostAndPort = new HostAndPort(ip,port);
            set.add(hostAndPort);
        }
        return set;
    }

    @Test
    public void testCluster(){
        JedisCluster jedisCluster = jedisCluster();
        jedisCluster.set("test","测试cluster成功!");
        System.out.println("ok");
    }

}
