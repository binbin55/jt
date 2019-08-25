package com.jt.controller;

import com.jt.pojo.Item;
import com.jt.pojo.ItemDesc;
import com.jt.vo.EasyUITable;
import com.jt.vo.SysResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jt.service.ItemService;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("item")
public class ItemController {
	
	@Autowired
	private ItemService itemService;

	@RequestMapping("query")
	public EasyUITable findItemByPage(Integer page,Integer rows){
		return itemService.findItemByPage(page,rows);
	}

	/**
	 * 实现商品数据递增
	 * 异常全局处理机制
	 * @param item
	 * @return
	 */
	@RequestMapping("save")
	public SysResult saveItem(Item item, ItemDesc itemDesc){
		itemService.saveItem(item,itemDesc);
		return SysResult.success();
	}

	@RequestMapping("update")
	public SysResult updateItem(Item item,ItemDesc itemDesc){
		itemService.updateItem(item,itemDesc);
		return SysResult.success();
	}

	/**
	 * 实现商品的下架操作
	 *
	 * 规则:
	 * 		如果用户传参使用","号分割参数,则springMVC
	 * 	接受参数时可以使用数组接受,由程序内部实现自动转化
	 * @param ids
	 * @return
	 */
	@RequestMapping("instock")
	public SysResult instock(Long[] ids){
		int status = 2;//表示下架
		itemService.updateStatus(ids,status);
		return SysResult.success();
	}

	@RequestMapping("reshelf")
	public SysResult reshelf(Long[] ids){
		int status = 1;//表示下架
		itemService.updateStatus(ids,status);
		return SysResult.success();
	}

	@RequestMapping("delete")
	public SysResult delete(Long[] ids){
		itemService.deleteItemByIds(ids);
		return SysResult.success();
	}

	/**
	 * 根据商品详情信息获取服务器数据
	 */
	@RequestMapping("query/item/desc/{itemId}")
	public SysResult findItemDescById(@PathVariable("itemId")Long itemId){
		ItemDesc itemDesc = itemService.findItemDesc(itemId);
		return SysResult.success(itemDesc);
	}

}
