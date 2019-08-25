package com.jt.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.jt.mapper.CartMapper;
import com.jt.pojo.Cart;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;
@Service(timeout = 3000)
public class DubboCartServiceImpl implements DubboCartService {

    @Autowired
    private CartMapper cartMapper;

    @Override
    public List<Cart> findCartListByUserId(Long userId) {
        QueryWrapper<Cart> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id",userId);
        List<Cart> list = cartMapper.selectList(queryWrapper);
        return list;
    }

    @Override
    public void updateNum(Cart cart) {
        Cart cartTemp = new Cart();
        cartTemp.setNum(cart.getNum()).setUpdated(new Date());
        QueryWrapper<Cart> updateWrapper = new QueryWrapper<>();
        updateWrapper.eq("item_id",cart.getItemId()).eq("user_id",cart.getUserId());
        cartMapper.update(cartTemp,updateWrapper);
    }

    @Override
    public void deleteCart(Cart cart) {
        QueryWrapper<Cart> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("item_id",cart.getItemId());
        cartMapper.delete(queryWrapper);
    }

    @Override
    public void insertCart(Cart cart) {
        QueryWrapper<Cart> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("item_id",cart.getItemId()).eq("user_id",cart.getUserId());
        Cart cartDB = cartMapper.selectOne(queryWrapper);
        if (cartDB == null){
            cart.setCreated(new Date()).setUpdated(cart.getCreated());
            cartMapper.insert(cart);
        }else {
            int num = cart.getNum() + cartDB.getNum();
            //1.能否使用byId方法
            //2.好还是不好性能如何????
            Cart cartTemp = new Cart();
            cartTemp.setNum(num).setUpdated(new Date());
            UpdateWrapper<Cart> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("id",cartDB.getId());
            cartMapper.update(cartTemp,updateWrapper);
        }
    }



}
