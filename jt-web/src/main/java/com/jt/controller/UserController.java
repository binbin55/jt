package com.jt.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.jt.pojo.User;
import com.jt.service.DubboUserService;
import com.jt.vo.SysResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import redis.clients.jedis.JedisCluster;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("user")
public class UserController {

    @Reference(timeout = 3000,check = true)
    private DubboUserService dubboUserService;

    @Autowired
    private JedisCluster jedisCluster;

    /**
     * 实现用户页面跳转
     */
    @RequestMapping("{moduleName}")
    public String login(@PathVariable String moduleName){
        return moduleName;
    }

    @RequestMapping("/doRegister")
    @ResponseBody
    public SysResult insertUser(User user) {
        dubboUserService.insertUser(user);
        return SysResult.success();
    }

    //www.jt.com    不能转化为com.jt.pojo.User
    @RequestMapping("/doLogin")
    @ResponseBody
    public SysResult doLogin(User user, HttpServletResponse response){
        String ticket = dubboUserService.doLogin(user);
        if (StringUtils.isEmpty(ticket)){
            return SysResult.fail();
        }
        //2.需要将数据保存到cookie中
        Cookie cookie = new Cookie("JT_TICKET",ticket);
        cookie.setMaxAge(7*24*3600);
        //设定cookie的使用权限,
        //www.jt.com/user/
        cookie.setPath("/");
        //cookie共享
        cookie.setDomain("jt.com");
        response.addCookie(cookie);
        return SysResult.success();
    }

    /**
     * 1.删除Cookie	根据key
     * 2.删除redis
     * 3.重定向页面
     * @return
     */
    @RequestMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response){
        //1.获取cookie数据
        Cookie[] cookies = request.getCookies();
        String tickey = null;
        if (cookies.length>=0){
            for (Cookie cookie : cookies){
                if ("JT_TICKET".equals(cookie)){
                    tickey = cookie.getValue();
                    break;
                }
            }
        }
        if (!StringUtils.isEmpty(tickey)){
            jedisCluster.del(tickey);
        }
        Cookie cookie = new Cookie("JT_TICKET","");
        cookie.setMaxAge(0);//删除cookie
        cookie.setDomain("jt.com");
        cookie.setPath("/");
        response.addCookie(cookie);
        return "redirect:/";
    }


}
