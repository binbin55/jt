package com.jt.config;

import com.jt.pojo.User;
import com.jt.util.ObjectMapperUtil;
import com.jt.util.UserThreadLocal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import redis.clients.jedis.JedisCluster;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 拦截器,实现用户权限判断
 */
@Component
public class UserInterceptor implements HandlerInterceptor {

    @Autowired
    private JedisCluster jedisCluster;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //1.获取用户cookie信息
        Cookie[] cookies = request.getCookies();
        String ticket = null;
        if (cookies.length > 0){
            for (Cookie cookie : cookies){
                if ("JT_TICKET".equals(cookie.getName())){
                    ticket = cookie.getValue();
                    break;
                }
            }
        }
        //判断取值是否有效
        if (!StringUtils.isEmpty(ticket)){
            String userJSON = jedisCluster.get(ticket);
            if (!StringUtils.isEmpty(userJSON)){
                User user = ObjectMapperUtil.toObject(userJSON, User.class);
                UserThreadLocal.set(user);
                return true;
            }
        }

        response.sendRedirect("/user/login.html");
        return false;
    }



}
