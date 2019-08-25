package com.jt.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jt.pojo.ItemCat;
import com.jt.vo.EasyUITree;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface ItemCatMapper extends BaseMapper<ItemCat> {

//    @Select("SELECT c.name FROM tb_item i join tb_item_cart c on i.cid=c.id WHERE = i.cid=#{cid}")
//    String findItemName(@Param("cid") Long cid);



}
