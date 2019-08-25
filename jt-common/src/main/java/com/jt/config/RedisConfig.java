package com.jt.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;

import java.util.HashSet;
import java.util.Set;

@PropertySource("classpath:/properties/redis.properties")
@Configuration
public class RedisConfig {

    @Value("${redis.clusterNodes}")
    private String nodes;

//    @Value("${redis.auth}")
//    private String password;

//    @Value("${redis.masterName}")
//    private String master;

//    @Bean
//    public ShardedJedis shardedJedis(){
//        List<JedisShardInfo> shards = getShards();
//        return new ShardedJedis(shards);
//    }
//
//    private List<JedisShardInfo> getShards() {
//        List<JedisShardInfo> list = new ArrayList<>();
//        String[] nodeArray = nodes.split(",");
//        for (String node : nodeArray){
//            String ip = node.split(":")[0];
//            Integer port = Integer.parseInt(node.split(":")[1]);
//            JedisShardInfo jedisShardInfo = new JedisShardInfo(ip,port);
//            list.add(jedisShardInfo);
//        }
//        return list;
//    }

//    @Bean
//    public JedisSentinelPool jedisSentinelPool(){
//        Set<String> sentinels = new HashSet<>();
//        sentinels.add(nodes);
//        return new JedisSentinelPool(master,sentinels);
//    }
//
//    @Bean
//    public Jedis jedis(@Qualifier("jedisSentinelPool") JedisSentinelPool jedisSentinelPool){
//        return jedisSentinelPool.getResource();
//    }

    @Bean
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

}
