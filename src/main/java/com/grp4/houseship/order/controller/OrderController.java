package com.grp4.houseship.order.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import com.grp4.houseship.house.model.HouseInfo;
import com.grp4.houseship.house.model.HouseService;
import com.grp4.houseship.member.model.MemberService;
import com.grp4.houseship.order.model.OrderInfo;
import com.grp4.houseship.order.model.OrderItem;
import com.grp4.houseship.order.model.OrderService;

@Controller
@RequestMapping("/order")
@SessionAttributes(names = {"deleteList","idlist"})
public class OrderController {

	@Autowired
	private OrderService orderService;
	
	@Autowired
	private MemberService memberService;
	
	@Autowired 
	private HouseService houseService;
	
	@GetMapping("/viewAll")
	public String processViewAllOrder(Model m) {
		m.addAttribute("AllOrder", orderService.findAll());
		return "order/viewAllOrder";
	}
	
	@PostMapping("/search")
	public String processFindByChkin(@RequestParam String date1, @RequestParam String date2, Model m) {
		m.addAttribute("AllOrder", orderService.findByChkindate(date1, date2));
		return "order/viewAllOrder";
	}
	
	@PostMapping("/insertOrder.checkAccount/{account}")
	@ResponseBody
	public String processInsertCheckAccount(@PathVariable("account") String account) {
		if (memberService.findByAccount(account) != null) {
			return "ok";
		}
		return null;
	}
	
	@PostMapping("/insertOrder.action")
	@ResponseBody
	public String processInsertAction(@RequestParam("account") String account, @RequestBody OrderItem orderitem) {
		OrderInfo orderinfo = new OrderInfo(memberService.findByAccount(account));
		HouseInfo houseinfo = houseService.searchById(orderitem.getInputHouseno());
		
		orderitem.setOrderinfo(orderinfo);
		orderitem.setHouseinfo(houseinfo);
		
		ArrayList<OrderItem> itemList = new ArrayList<OrderItem>();
		itemList.add(orderitem);
		orderinfo.setItems(itemList);
		
		orderService.insertOrder(orderinfo);
		orderService.inserItem(orderitem);		
		return "insert ok";
	}
	
	@GetMapping("/updateOrder/{itemid}")
	public String proccessUpdate(@PathVariable("itemid") int itemid, Model m) {
		OrderItem orderitem = orderService.findByItemid(itemid);
		m.addAttribute("orderitem", orderitem);
		return "order/order_update";
	}
	
	@PostMapping("/updateOrder.action")
	@ResponseBody
	public String processUpdate(@RequestBody OrderItem orderitem) {
		OrderItem item = orderService.updateItem(orderitem);
		System.out.println("orderid: " + item.getOrderid());
		return "update ok";
	}
	
	@GetMapping("/deleteOrder/{itemid}")
	public String processDelete(@PathVariable("itemid") int itemid, Model m) {
		OrderItem orderitem = orderService.findByItemid(itemid);
		m.addAttribute("orderitem", orderitem);
		return "order/order_delete";
	}
	
	@PostMapping("/deleteOrder.action")
	public String processDelete(@RequestParam("itemid") Integer itemid, 
			@RequestParam("orderid") Integer orderid,
			@ModelAttribute("orderitem") OrderItem deleteitem) {
		orderService.deleteItem(itemid);
		OrderInfo orderinfo = orderService.findByOrderId(orderid);
		if (orderService.findItemByOrder(orderinfo).size() == 0) {
			orderService.deleteOrder(orderid);
		}
		return "redirect:/order/viewAll";
	}
	
	@GetMapping("/deleteAllOrder.form")
	public String processDeleteAllForm() {
		return "order/order_deleteAll";
	}
			
	@PostMapping("/deleteAllOrder")
	@ResponseBody
	public String processDeleteAll(@RequestBody ArrayList<Integer> listid, Model m) {
		List<OrderInfo> orderList = orderService.findAllById(listid);
		m.addAttribute("deleteList", orderList);
		m.addAttribute("idlist", listid);
		System.out.println("=====================" + orderList.size());
		return "get delete list ok";
	}
	
	@PostMapping("/deleteAllOrder.action")
	@ResponseBody
	public String processDeleteAllAction(@RequestBody ArrayList<Integer> listid, Model m) {
		List<OrderInfo> orderList = orderService.findAllById(listid);
		for (OrderInfo order: orderList) {
			List<OrderItem> items = orderService.findItemByOrder(order);
			orderService.deleteAllItems(items);			
		}
		orderService.deleteSelection(listid);
		return "delete all";
	}
}
