package com.jt.vo;

import com.jt.pojo.Item;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 为了EasyUI数据回显, 封装VO对象
 */
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class EasyUITable {

    private Integer total;
    private List<Item> rows;



}
