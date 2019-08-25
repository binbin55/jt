package com.jt.controller;

import com.jt.anno.Cache_Find;
import com.jt.service.ItemCatService;
import com.jt.vo.EasyUITree;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("item/cat")
public class ItemCatController {

    @Autowired
    private ItemCatService itemCatService;

    @RequestMapping("queryItemName")
    public String queryItemName(Long itemCatId){
        return itemCatService.findItemCatNameByItemCid(itemCatId);
    }

    @RequestMapping("list")
    @Cache_Find
    public List<EasyUITree> findEasyUITreeList(@RequestParam(defaultValue = "0",name = "id") Long parentId){
//        Long parentId = 0L;
//        if (id != null){
//            parentId = id;
//        }
        return itemCatService.findEasyUITreeList(parentId);
    }

}
