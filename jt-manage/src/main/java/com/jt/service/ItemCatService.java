package com.jt.service;

import com.jt.vo.EasyUITree;

import java.util.List;

public interface ItemCatService {

    String findItemCatNameByItemCid(Long cid);

    List<EasyUITree> findEasyUITreeList(Long parentId);

    List<EasyUITree> findEasyUITreeListCache(Long parenId);

}
