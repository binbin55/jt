package com.jt.controller.web;

import com.jt.pojo.Item;
import com.jt.pojo.ItemDesc;
import com.jt.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/web/item")
public class WebItemController {

    @Autowired
    private ItemService itemService;

    @RequestMapping("findItemById")
    public Item findItemById(Long itemId){
        return itemService.findItemById(itemId);
    }

    @RequestMapping("findItemDescById")
    public ItemDesc findItemDescById(Long itemId) {
        return itemService.findItemDescById(itemId);
    }


}
