package com.jt.test;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import redis.clients.jedis.*;

import java.util.*;


@SpringBootTest
@RunWith(SpringRunner.class)
public class TestRedis {

    private Jedis jedis;

    @Autowired
    private ShardedJedis shardedJedis;

    @Before
    public void before(){
        jedis = new Jedis("192.168.19.137",6379);
    }

    @Test
    public void test01(){

//        jedis.set("bin","yehongbin");

        //如果当前key已经存在,则不能修改
        jedis.setnx("bin","剩下");

        String bin = jedis.get("bin");
        System.out.println(bin);

        //需求：1.添加超时时间   2.不允许重复操作
        jedis.set("bin","刷新姓名","nx","",10);

    }

    /**
     *
     * 操作Hash
     */
    @Test
    public void testHash(){
        jedis.hset("person","id","100");
        jedis.hset("person","name","人");
        jedis.hset("person","age","18");
        Map<String, String> person = jedis.hgetAll("person");
        System.out.println(person);
    }

    /**
     * 操作list
     */
    @Test
    public void testList(){
        jedis.lpush("list","1","2","3","4","5");
//        for (int i=0;i<6;i++){
//            String list = jedis.rpop("list");
//            System.out.println(list);
//        }
    }

    @Test
    public void testTx(){
        Transaction multi = jedis.multi();
        try {
            multi.set("dd","aaa");
//            multi.set("bb","bbb");
//            multi.set("cc","ccc");
            int a = 1/0;
            System.out.println(a);
            multi.exec();
        } catch (Exception e){
            multi.discard();
        }
    }

    /**
     * redis分片测试
     */
    @Test
    public void testShards(){
        String host = "192.168.19.137";
        List<JedisShardInfo> shardInfos = new ArrayList<>();
        JedisShardInfo jedisShardInfo = new JedisShardInfo(host, 6379);
        jedisShardInfo.setPassword("root");
        shardInfos.add(jedisShardInfo);
        JedisShardInfo jedisShardInfo1 = new JedisShardInfo(host, 6380);
        jedisShardInfo1.setPassword("root");
        shardInfos.add(jedisShardInfo1);
        JedisShardInfo jedisShardInfo2 = new JedisShardInfo(host, 6381);
        jedisShardInfo2.setPassword("root");
        shardInfos.add(jedisShardInfo2);
        ShardedJedis shardedJedis = new ShardedJedis(shardInfos);
        shardedJedis.set("dfghoeirer","分片操作");
        System.out.println(shardedJedis.get("1904"));
    }

    @Test
    public void testShardsJedis(){
        System.out.println(shardedJedis);
    }

    /**
     * 测试哨兵
     */
    @Test
    public void testSentinel(){
        Set<String> sentinels = new HashSet<>();
        sentinels.add("192.168.19.137:26379");
        JedisSentinelPool pool = new JedisSentinelPool("mymaster",sentinels);
        Jedis jedis = pool.getResource();
        jedis.set("1904","测试哨兵!");
        System.out.println(jedis.get("1904"));
    }

    @Test
    public void testCluster(){
        Set<HostAndPort> nodes = new HashSet<>();
        nodes.add(new HostAndPort("192.168.19.137",7000));
        nodes.add(new HostAndPort("192.168.19.137",7001));
        nodes.add(new HostAndPort("192.168.19.137",7002));
        nodes.add(new HostAndPort("192.168.19.137",7003));
        nodes.add(new HostAndPort("192.168.19.137",7004));
        nodes.add(new HostAndPort("192.168.19.137",7005));
        JedisCluster cluster = new JedisCluster(nodes);
        cluster.set("1904","bin连接生");
        System.out.println(cluster.get("1904"));
    }

}
