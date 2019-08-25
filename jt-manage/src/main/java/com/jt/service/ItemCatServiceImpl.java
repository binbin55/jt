package com.jt.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jt.mapper.ItemCatMapper;
import com.jt.pojo.ItemCat;
import com.jt.util.ObjectMapperUtil;
import com.jt.vo.EasyUITree;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import redis.clients.jedis.JedisCluster;

import java.util.ArrayList;
import java.util.List;

@Service
public class ItemCatServiceImpl implements ItemCatService{

    @Autowired
    private ItemCatMapper itemCatMapper;

    @Autowired(required = false)
    private JedisCluster jedis;

    @Override
    public String findItemCatNameByItemCid(Long cid) {
        ItemCat itemCat = itemCatMapper.selectById(cid);
        return itemCat.getName();
    }

    /**
     * EasyUITree VO对象
     * 依赖
     * ItemCat    数据库对象
     * 思路:
     *    1.先查询数据库List信息
     *    2.将数据库对象转化为VO对象
     * @return
     */
    public List<ItemCat> findItemCatList(Long parentId){
        QueryWrapper<ItemCat> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("parent_id",parentId);
        List<ItemCat> itemCatList = itemCatMapper.selectList(queryWrapper);
        return itemCatList;
    }

    @Override
    public List<EasyUITree> findEasyUITreeList(Long parentId) {
        List<ItemCat> itemCatList = findItemCatList(parentId);
        List<EasyUITree> treeList = new ArrayList<>();
        for (ItemCat itemCat : itemCatList){
            EasyUITree easyUITree = new EasyUITree();
            String state = itemCat.getIsParent()?"closed":"open";
            easyUITree.setId(itemCat.getId())
                      .setText(itemCat.getName())
                      .setState(state);
            treeList.add(easyUITree);
        }
        return treeList;
    }

    @Override
    public List<EasyUITree> findEasyUITreeListCache(Long parenId) {
        List<EasyUITree> treeList = new ArrayList<>();
        String key = "ITEM_CAT_"+parenId;
        String result = jedis.get(key);
        if (StringUtils.isEmpty(result)){
            treeList = findEasyUITreeList(parenId);
            String value = ObjectMapperUtil.toJson(treeList);
            jedis.set(key,value);
            System.out.println("查询后台数据库!!!");
        }else {
            treeList = ObjectMapperUtil.toObject(result,treeList.getClass());
            System.out.println("查询redis缓存");
        }
        return treeList;
    }

}
