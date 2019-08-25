package com.jt.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jt.mapper.ItemDescMapper;
import com.jt.pojo.Item;
import com.jt.pojo.ItemDesc;
import com.jt.vo.EasyUITable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jt.mapper.ItemMapper;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Date;

@Service
public class ItemServiceImpl implements ItemService {
	
	@Autowired
	private ItemMapper itemMapper;

	@Autowired
	private ItemDescMapper itemDescMapper;

//	@Override
//	public EasyUITable findItemByPage(Integer page, Integer rows) {
//		int start = (page-1)*rows;
//		List<Item> itemByPage = itemMapper.findItemByPage(start, rows);
//		Integer integer = itemMapper.selectCount(null);
//		EasyUITable easyUITable = new EasyUITable(integer,itemByPage);
//		return easyUITable;
//	}


	@Override
	public EasyUITable findItemByPage(Integer page, Integer rows) {
		QueryWrapper<Item> itemQueryWrapper = new QueryWrapper<>();
		itemQueryWrapper.orderByDesc("updated");
		IPage<Item> itemIPage = itemMapper.selectPage(new Page<Item>((page - 1) * rows, rows), itemQueryWrapper);
		return new EasyUITable((int)itemIPage.getTotal(),itemIPage.getRecords());
	}

	@Transactional	//控制事务
	@Override
	public void saveItem(Item item, ItemDesc itemDesc) {
		item.setStatus(1).setCreated(new Date()).setUpdated(item.getCreated());
		itemMapper.insert(item);

		//完成商品详情入库
		itemDesc.setItemId(item.getId()).setCreated(item.getCreated()).setUpdated(item.getCreated());
		itemDescMapper.insert(itemDesc);
	}

	@Transactional
	@Override
	public void updateItem(Item item,ItemDesc itemDesc) {
		item.setUpdated(new Date());
		itemMapper.updateById(item);
		itemDesc.setItemId(item.getId()).setUpdated(item.getUpdated());
		itemDescMapper.updateById(itemDesc);
	}

	@Override
	public void updateStatus(Long[] ids, int status) {
		Item entity = new Item();
		entity.setStatus(status).setUpdated(new Date());
		UpdateWrapper<Item> updateWrapper = new UpdateWrapper<>();
		updateWrapper.in("id",ids);
		itemMapper.update(entity,updateWrapper);
	}

	@Override
	public void deleteItemByIds(Long[] ids) {
		itemMapper.deleteBatchIds(Arrays.asList(ids));
		itemDescMapper.deleteBatchIds(Arrays.asList(ids));
	}

	@Override
	public ItemDesc findItemDesc(Long itemId) {
		return itemDescMapper.selectById(itemId);
	}

	@Override
	public Item findItemById(Long itemId) {
		Item item = itemMapper.selectById(itemId);
		return item;
	}

	@Override
	public ItemDesc findItemDescById(Long itemId) {
		return itemDescMapper.selectById(itemId);
	}

}
