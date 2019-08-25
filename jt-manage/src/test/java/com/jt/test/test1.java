package com.jt.test;

import com.jt.pojo.Item;
import com.jt.service.ItemService;
import com.jt.vo.EasyUITable;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
public class test1 {

    @Autowired
    private ItemService itemService;

    @Test
    public void test11(){
        EasyUITable itemByPage = itemService.findItemByPage(1, 10);
        System.out.println("总行数="+itemByPage.getTotal());
        List<Item> rows = itemByPage.getRows();
        for (Item item : rows){
            System.err.println(item);
        }
    }

}
