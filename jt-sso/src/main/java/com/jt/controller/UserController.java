package com.jt.controller;

import com.jt.util.ObjectMapperUtil;
import com.jt.vo.SysResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.fasterxml.jackson.databind.util.JSONPObject;

import com.jt.service.UserService;
import redis.clients.jedis.JedisCluster;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("user")
public class UserController {
	
	@Autowired
	private UserService userService;

	@Autowired
	private JedisCluster jedisCluster;

	/**
	 * 实现用户信息校验
	 */
	@RequestMapping("check/{param}/{type}")
	public JSONPObject checkUser(@PathVariable String param, @PathVariable Integer type, String callback) {
		JSONPObject jsonObject = null;
		try {
			boolean flag = userService.findCheckUser(param,type);
			jsonObject = new JSONPObject(callback,SysResult.success(flag));
		} catch (Exception e){
			e.printStackTrace();
			jsonObject = new JSONPObject(callback,SysResult.fail());
		}
		return jsonObject;
	}

	@RequestMapping("query/{tickey}")
	public JSONPObject findUserByTicket(@PathVariable("tickey") String tickey,String callback){
		String userJSON = jedisCluster.get(tickey);
		JSONPObject jsonpObject = null;
		if (StringUtils.isEmpty(userJSON)){
			jsonpObject = new JSONPObject(callback,SysResult.fail());
		}else {
			jsonpObject = new JSONPObject(callback,SysResult.success(userJSON));
		}
		return jsonpObject;
	}



}
