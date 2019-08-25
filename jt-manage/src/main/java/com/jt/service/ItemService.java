package com.jt.service;

import com.jt.pojo.Item;
import com.jt.pojo.ItemDesc;
import com.jt.vo.EasyUITable;

public interface ItemService {

    EasyUITable findItemByPage(Integer page, Integer rows);

    void saveItem(Item item, ItemDesc itemDesc);

    void updateItem(Item item,ItemDesc itemDesc);

    void updateStatus(Long[] ids, int status);

    void deleteItemByIds(Long[] ids);

    ItemDesc findItemDesc(Long itemId);

    Item findItemById(Long itemId);

    ItemDesc findItemDescById(Long itemId);

}
