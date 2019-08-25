package com.jt.service;


import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jt.mapper.UserMapper;
import com.jt.pojo.User;
import com.jt.util.ObjectMapperUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import redis.clients.jedis.JedisCluster;

import javax.servlet.http.Cookie;
import java.util.Date;
import java.util.UUID;

@Service(timeout = 3000)
public class DubboUserServiceImpl implements DubboUserService{

    @Autowired
    private JedisCluster jedisCluster;

    @Autowired
    private UserMapper userMapper;

    @Override
    public void insertUser(User user) {
        //密码加密  注意加密和登录算法必须相同
        String md5Pass = DigestUtils.md5DigestAsHex(user.getPassword().getBytes());
        user.setPassword(md5Pass).setCreated(new Date()).setUpdated(user.getCreated());
        userMapper.insert(user);
    }

    @Override
    public String doLogin(User user) {
        String md5Pass = DigestUtils.md5DigestAsHex(user.getPassword().getBytes());
        user.setPassword(md5Pass);
        QueryWrapper<User> queryWrapper = new QueryWrapper<>(user);
        User queryUser = userMapper.selectOne(queryWrapper);
        String key = null;
        if (queryUser!=null){
            //表示用户名密码正确
            key = DigestUtils.md5DigestAsHex(UUID.randomUUID().toString().getBytes());
            queryUser.setPassword("123456");
            String json = ObjectMapperUtil.toJson(queryUser);
            jedisCluster.setex(key,7*24*3600,json);
        }
        return key;
    }
}
