package com.jt.service;

import com.jt.pojo.User;

public interface DubboUserService {

    void insertUser(User user);

    String doLogin(User user);

}
